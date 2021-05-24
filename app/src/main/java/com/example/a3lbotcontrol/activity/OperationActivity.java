package com.example.a3lbotcontrol.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3lbotcontrol.R;
import com.example.a3lbotcontrol.bean.User;
import com.example.a3lbotcontrol.gen.UserDao;
import com.example.a3lbotcontrol.service.BluetoothLeService;
import com.example.a3lbotcontrol.util.CutString;
import com.example.a3lbotcontrol.util.GreenDaoManager;
import com.example.a3lbotcontrol.util.Transfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OperationActivity extends AppCompatActivity {

    RadioGroup rgCut;
    SeekBar skLeft, skRight;
    TextView tvLeft, tvRight;
    EditText etAngleLeft, etMomentLeft, etFactorLeft;
    EditText etAngleRight, etMomentRight, etFactorRight;
    View contentViewRandom, contentViewFixed;
    LinearLayout llA;
    @BindView(R.id.tv_connect_state)
    TextView tvConnectState;
    @BindView(R.id.tv_max_left)
    TextView tvMaxLeft;
    @BindView(R.id.tv_min_left)
    TextView tvMinLeft;
    @BindView(R.id.tv_mean_left)
    TextView tvMeanLeft;
    @BindView(R.id.tv_factor_left)
    TextView tvFactorLeft;
    @BindView(R.id.tv_max_right)
    TextView tvMaxRight;
    @BindView(R.id.tv_min_right)
    TextView tvMinRight;
    @BindView(R.id.tv_mean_right)
    TextView tvMeanRight;
    @BindView(R.id.tv_factor_right)
    TextView tvFactorRight;
    @BindView(R.id.rev_tv)
    TextView rev_tv;
    @BindView(R.id.tv_moment_left)
    TextView tvMomentLeft;
    @BindView(R.id.tv_moment_right)
    TextView tvMomentRight;
    @BindView(R.id.btn_fixed)
    Button btnFixed;
    @BindView(R.id.btn_random)
    Button btnRandom;
    @BindView(R.id.btn_get_info)
    Button btnGetInfo;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_person_info)
    Button btnPersonInfo;
    @BindView(R.id.rev_sv)
    ScrollView revSv;
    @BindView(R.id.btn_reconnection)
    Button btnReconnection;
    @BindView(R.id.tv_angle_left)
    TextView tvAngleLeft;
    @BindView(R.id.tv_angle_right)
    TextView tvAngleRight;
    @BindView(R.id.iv_electric)
    ImageView ivElectric;
    @BindView(R.id.rg_cut_model)
    RadioGroup rgCutModel;

    private String status = "未连接";
    private String mDeviceAddress;  //蓝牙地址
    private String rev_str = "";
    private String allString = "";
    private String strCut = "00";
    private String sexUser = "0000";
    private boolean isplay = true;

    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    public static String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
    public static String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static String EXTRAS_DEVICE_RSSI = "RSSI";

    private static BluetoothLeService mBluetoothLeService;  //蓝牙service,负责后台的蓝牙服务
    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    private static BluetoothGattCharacteristic target_chara = null;  //蓝牙特征值
    SharedPreferences sp;
    SharedPreferences.Editor edit;
    private Handler mhandler = new Handler();
    Intent gattServiceIntent;
    boolean iscon;
    AlertDialog.Builder builderRandom = null;
    AlertDialog.Builder builderFixed = null;

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    // 更新View
                    String state = msg.getData().getString("connect_state");
                    if (state.equals("connected")) {
                        tvConnectState.setText("已连接");
                        btnReconnection.setVisibility(View.INVISIBLE);
                        //   writePublic("e90000d2ef ");
                    } else if (state.equals("disconnect")) {
                        tvConnectState.setText("已断开");
                        btnReconnection.setVisibility(View.VISIBLE);
                    }

                    break;
                }
            }
            super.handleMessage(msg);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        ButterKnife.bind(this);
        //沉浸状态栏
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        /* 启动蓝牙service */
        gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);

        //蓝牙物理地址
        mDeviceAddress = getIntent().getStringExtra(EXTRAS_DEVICE_ADDRESS);
        //本地缓存
        sp = getSharedPreferences("person", MODE_PRIVATE);
        edit = sp.edit();

        tvMomentLeft.setText(sp.getString("Left", ""));
        tvMomentRight.setText(sp.getString("Right", ""));

        rgCutModel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_fixed:
                        if (iscon) {
                            writePublic("e90200640002ef");
                        } else {

                        }
                        break;
                    case R.id.rb_random:
                        if (iscon) {
                            writePublic("e90200640001ef");
                        } else {

                        }
                        break;
                }
            }
        });
    }

    @OnClick({R.id.btn_reconnection, R.id.btn_fixed, R.id.btn_random, R.id.btn_get_info, R.id.btn_start, R.id.btn_person_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reconnection:
                if (!iscon) {
                    mBluetoothLeService.connect(mDeviceAddress);
                    btnStart.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.stop), null, null);
                    btnStart.setText("启动");
                    isplay = true;
                } else {
                    Toast.makeText(getApplicationContext(), "设备已连接", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_fixed:
                if (iscon) {
                    showFixedDialog();
                }
                break;
            case R.id.btn_random:
                if (iscon) {
                    showRandomDialog();
                }
                break;
            case R.id.btn_get_info:
                if (iscon) {
                //    writePublic("e90200c10001ef");
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(40);
                                writePublic("e90000d2ef");
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            case R.id.btn_start:
                if (iscon) {
                    if (isplay) {
                        writePublic("e90800c20000008300000001ef");
                        btnStart.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.play), null, null);
                        btnStart.setText("暂停");
                    } else {
                        writePublic("e90800c20000008300000002ef");
                        btnStart.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.stop), null, null);
                        btnStart.setText("启动");
                    }
                    isplay = !isplay;
                }
                break;
            case R.id.btn_person_info:
                if (iscon) {
                    User user = GreenDaoManager.getInstance().getUserDao().queryBuilder().where(UserDao.Properties.PhoneNumber.eq("182")).build().unique();
                    if (user != null) {
                        if (user.getSex().equals("男")) {
                            sexUser = "0000";
                        } else {
                            sexUser = "0001";
                        }
                        String strHeight = String.format("%04x", user.getHeight());
                        String strWeight = String.format("%04x", user.getWeight());
                        String strAll = "e9080061" + strHeight + strWeight + sexUser + "0000ef";
                        writePublic(strAll);
                    } else {
                        Toast.makeText(this, "获取身高体重失败", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private void showRandomDialog() {
        builderRandom = new AlertDialog.Builder(this);
        initRandom();
        rgCut.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_voluntarily:
                        strCut = "00";
                        llA.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.rb_manual_operation:
                        strCut = "01";
                        llA.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        builderRandom.setView(contentViewRandom);
        builderRandom.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(etAngleLeft.getText().toString()) || TextUtils.isEmpty(etAngleRight.getText().toString())
                        || TextUtils.isEmpty(etMomentLeft.getText().toString()) || TextUtils.isEmpty(etMomentRight.getText().toString())
                        || TextUtils.isEmpty(etFactorLeft.getText().toString()) || TextUtils.isEmpty(etFactorRight.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "数据不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    //数据传输
                    writePublic(Transfrom.getTransfrom().writeRandomL(etAngleLeft.getText().toString(), etMomentLeft.getText().toString(),
                            etFactorLeft.getText().toString(), etAngleRight.getText().toString()));
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                Thread.sleep(40);
                                writePublic(Transfrom.getTransfrom().writeRandomR(etMomentRight.getText().toString(), etFactorRight.getText().toString(), strCut));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }
                edit.putString("etAngleLeft", etAngleLeft.getText().toString());
                edit.putString("etAngleRight", etAngleRight.getText().toString());
                edit.putString("etMomentLeft", etMomentLeft.getText().toString());
                edit.putString("etMomentRight", etMomentRight.getText().toString());
                edit.putString("etFactorLeft", etFactorLeft.getText().toString());
                edit.putString("etFactorRight", etFactorRight.getText().toString());
                edit.putString("rgcut", strCut);
                edit.commit();
            }
        });
        builderRandom.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builderRandom.create().show();

    }


    private void showFixedDialog() {
        builderFixed = new AlertDialog.Builder(this);
        initFixedDialog();
        builderFixed.setView(contentViewFixed);
        builderFixed.setPositiveButton("提交", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(tvLeft.getText().toString()) || TextUtils.isEmpty(tvRight.getText().toString())) {
                } else {
                    writePublic(Transfrom.getTransfrom().writeFixed(tvLeft.getText().toString(), tvRight.getText().toString()));
                }
                edit.putString("Left", tvLeft.getText().toString());
                edit.putString("Right", tvRight.getText().toString());
                edit.commit();
                tvMomentLeft.setText(tvLeft.getText().toString());
                tvMomentRight.setText(tvRight.getText().toString());

            }
        });
        builderFixed.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        skLeft.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float leftValue = seekBar.getProgress() / 5f * 4;
                tvLeft.setText(leftValue + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        skRight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float rightValue = seekBar.getProgress() / 5f * 4;
                tvRight.setText(rightValue + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        builderFixed.create().show();

    }

    /**
     * 蓝牙写数据
     */
    public void writePublic(String protocol) {
        byte[] buffStart = Transfrom.getTransfrom().hexString2Bytes(protocol);
        target_chara.setValue(buffStart);//只能一次发送20字节，所以这里要分包发送
        mBluetoothLeService.writeCharacteristic(target_chara);
    }

    /**
     * BluetoothLeService绑定的回调函数
     */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // 根据蓝牙地址，连接设备
            mBluetoothLeService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }

    };

    /**
     * 广播接收器，负责接收BluetoothLeService类发送的数据
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                //Gatt连接成功
                status = "connected";
                //更新连接状态
                iscon = true;
                updateConnectionState(status);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                //Gatt连接失败
                status = "disconnect";
                //更新连接状态
                iscon = false;
                mBluetoothLeService.disconnect();
                mBluetoothLeService.close();
                updateConnectionState(status);
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //发现GATT服务器
                // Show all the supported services and characteristics on the
                // user interface.
                //获取设备的所有蓝牙服务
                displayGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                //有效数据
                //处理发送过来的数据
                displayData(intent.getExtras().getString(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    /**
     * 更新连接状态
     */
    private void updateConnectionState(String status) {
        Message msg = new Message();
        msg.what = 1;
        Bundle b = new Bundle();
        b.putString("connect_state", status);
        msg.setData(b);
        myHandler.sendMessage(msg);
    }

    /**
     * @param @param rev_string(接受的数据)
     * @return void
     * @throws
     * @Title: displayData
     */
    private void displayData(String rev_string) {

        if (rev_string.equals("00")) {
        } else {
            rev_str += rev_string;
        }
        if (CutString.getCutString().cutEnd(rev_str).equals("FFEFAF")) {
            allString = rev_str;
            rev_str = "";
            String electric;
            switch (CutString.getCutString().cutCMD(allString)) {
                case "81":
                    Log.e("1111111", allString);
                    CutString.getCutString().cutTshow(allString);
                    tvMaxLeft.setText(CutString.strMaxLeft);
                    tvMinLeft.setText(CutString.strMinLeft);
                    tvMeanLeft.setText(CutString.strMeanLeft);
                    tvFactorLeft.setText(CutString.strFactorLeft);
                    tvMaxRight.setText(CutString.strMaxRight);
                    tvMinRight.setText(CutString.strMinRight);
                    tvMeanRight.setText(CutString.strMeanRight);
                    tvFactorRight.setText(CutString.strFactorRight);
                    tvAngleLeft.setText(CutString.strAngleLeft);
                    tvAngleRight.setText(CutString.strAngleRight);
                    break;
                case "82":
                    CutString.getCutString().cutMoment(allString);
                    edit.putString("Left", CutString.strMomentLeft);
                    edit.putString("Right", CutString.strMomentRight);
                    edit.commit();
                    tvMomentLeft.setText(CutString.strMomentLeft);
                    tvMomentRight.setText(CutString.strMomentRight);
                    break;
                case "83":
                    electric = CutString.getCutString().cutStartAndPauseElectric(allString);
                    if (electric.equals("01")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric1);
                    } else if (electric.equals("02")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric2);
                    } else if (electric.equals("03")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric3);
                    } else if (electric.equals("04")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric4);
                    }
                    Toast.makeText(getApplicationContext(), "设备启动/暂停", Toast.LENGTH_SHORT).show();
                    break;
                case "91":
                    electric = CutString.getCutString().cutElectric(allString);
                    if (electric.equals("01")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric1);
                    } else if (electric.equals("02")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric2);
                    } else if (electric.equals("03")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric3);
                    } else if (electric.equals("04")) {
                        ivElectric.setBackgroundResource(R.mipmap.iv_electric4);
                    }
                    break;
                case "92":
                    CutString.getCutString().cutHistoryData(allString);
                    GreenDaoManager.getInstance().saveBeanIH(CutString.strYear, CutString.strMonth, CutString.strDay, CutString.strTime, CutString.strStepfrequency, CutString.strAngle);
                    break;
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rev_tv.setText(allString);
                revSv.scrollTo(0, rev_tv.getMeasuredHeight());
                Log.e("+++++++++++++++", allString);
            }
        });

    }

    /**
     * @param
     * @return void
     * @throws
     * @Title: displayGattServices
     * @Description: TODO(处理蓝牙服务)
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";
        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。
            gattServiceData.add(currentServiceData);
            Log.e("Service uuid:", uuid);
            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                if (gattCharacteristic.getUuid().toString().equals(HEART_RATE_MEASUREMENT)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBluetoothLeService.readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // 设置数据内容
                    // 往蓝牙模块写入数据
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic.getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:" + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }
                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);

        }

    }

    /* 意图过滤器 */
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //初始化randomdialog
    private void initRandom() {
        contentViewRandom = View.inflate(this, R.layout.dialog_random, null);
        rgCut = (RadioGroup) contentViewRandom.findViewById(R.id.rg_cut);
        etAngleLeft = (EditText) contentViewRandom.findViewById(R.id.et_angle_left);
        etMomentLeft = (EditText) contentViewRandom.findViewById(R.id.et_moment_left);
        etFactorLeft = (EditText) contentViewRandom.findViewById(R.id.et_factor_left);
        etAngleRight = (EditText) contentViewRandom.findViewById(R.id.et_angle_right);
        etMomentRight = (EditText) contentViewRandom.findViewById(R.id.et_moment_right);
        etFactorRight = (EditText) contentViewRandom.findViewById(R.id.et_factor_right);
        llA = (LinearLayout) contentViewRandom.findViewById(R.id.ll_a);
        etAngleLeft.setText(sp.getString("etAngleLeft", ""));
        etMomentLeft.setText(sp.getString("etMomentLeft", ""));
        etFactorLeft.setText(sp.getString("etFactorLeft", ""));
        etAngleRight.setText(sp.getString("etAngleRight", ""));
        etMomentRight.setText(sp.getString("etMomentRight", ""));
        etFactorRight.setText(sp.getString("etFactorRight", ""));
        if (sp.getString("rgcut", "").equals("00")) {
            rgCut.check(R.id.rb_voluntarily);
            llA.setVisibility(View.INVISIBLE);
        } else if (sp.getString("rgcut", "").equals("01")) {
            rgCut.check(R.id.rb_manual_operation);
            llA.setVisibility(View.VISIBLE);
        }
    }

    //初始化FixedDialog
    private void initFixedDialog() {
        contentViewFixed = View.inflate(this, R.layout.dialog_seekbar, null);
        skLeft = (SeekBar) contentViewFixed.findViewById(R.id.sk_left);
        skRight = (SeekBar) contentViewFixed.findViewById(R.id.sk_right);
        tvLeft = (TextView) contentViewFixed.findViewById(R.id.tv_left);
        tvRight = (TextView) contentViewFixed.findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(sp.getString("Left", ""))) {
            skLeft.setProgress((int) Math.round(Float.parseFloat(sp.getString("Left", "")) * 5 / 4));
        }
        if (!TextUtils.isEmpty(sp.getString("Right", ""))) {
            skRight.setProgress((int) Math.round(Float.parseFloat(sp.getString("Right", "")) * 5 / 4));
        }
        tvLeft.setText(sp.getString("Left", ""));
        tvRight.setText(sp.getString("Right", ""));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //绑定广播接收器
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBluetoothLeService != null) {
            //根据蓝牙地址，建立连接
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除广播接收器
        unregisterReceiver(mGattUpdateReceiver);
        mBluetoothLeService.disconnect();
        mBluetoothLeService.close();
        mBluetoothLeService = null;
    }

}
