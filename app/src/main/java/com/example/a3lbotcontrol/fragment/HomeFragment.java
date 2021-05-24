package com.example.a3lbotcontrol.fragment;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a3lbotcontrol.R;
import com.example.a3lbotcontrol.activity.OperationActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View view;
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;

    // 蓝牙适配器
    BluetoothAdapter mBluetoothAdapter;
    @BindView(R.id.btn_scan)
    Button btnScan;
    Unbinder unbinder;
    @BindView(R.id.lv)
    ListView lv;

    private ArrayList<Integer> rssis;  // 蓝牙信号强度
    LeDeviceListAdapter mleDeviceListAdapter;   // 自定义Adapter
    private boolean mScanning;  // 描述扫描蓝牙的状态
    private boolean scan_flag;
    private Handler mHandler;
    int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;   // 蓝牙扫描时间

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mHandler = new Handler();
        // 初始化蓝牙
        init_ble();
        unbinder = ButterKnife.bind(this, view);

        scan_flag = true;
        // 自定义适配器
        mleDeviceListAdapter = new LeDeviceListAdapter();
        // 为listview指定适配器
        lv.setAdapter(mleDeviceListAdapter);
        /* listview点击函数 */
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                                    long id) {
                // TODO Auto-generated method stub
                final BluetoothDevice device = mleDeviceListAdapter.getDevice(position);
                if (device == null)
                    return;
                final Intent intent = new Intent(getContext(), OperationActivity.class);
                intent.putExtra(OperationActivity.EXTRAS_DEVICE_NAME, device.getName());
                intent.putExtra(OperationActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
                intent.putExtra(OperationActivity.EXTRAS_DEVICE_RSSI, rssis.get(position).toString());
                if (mScanning) {
                    /* 停止扫描设备 */
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }

                try {
                    // 启动Ble_Activity
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }

            }
        });
        return view;
    }


    @OnClick(R.id.btn_scan)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_scan:
                if (scan_flag) {
                    scanLeDevice(true);
                } else {
                    scanLeDevice(false);
                    btnScan.setText("扫描设备");
                }
                break;
            default:
                break;
        }
    }


    //初始化蓝牙
    private void init_ble() {
        // 手机硬件支持蓝牙
        if (!getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getContext(), "不支持BLE", Toast.LENGTH_SHORT).show();
            //finish();
        }
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    /**
     * @param enable (扫描使能，true:扫描开始,false:扫描停止)
     * @return void
     * @throws
     * @Title: scanLeDevice
     * @Description: TODO(扫描蓝牙设备)
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                    btnScan.setText("扫描设备");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
            /* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
            mScanning = true;
            scan_flag = false;
            btnScan.setText("停止扫描");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
        }
    }

    /**
     * 蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
     **/
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            // TODO Auto-generated method stub
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /* 讲扫描到设备的信息输出到listview的适配器 */
                    String devicegetName = device.getName() + "";
                    if (devicegetName.indexOf("HC") != -1) {
                        mleDeviceListAdapter.addDevice(device, rssi);
                        mleDeviceListAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    };

    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            rssis = new ArrayList<Integer>();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device, int rssi) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
                rssis.add(rssi);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
            rssis.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        /**
         * 重写getview
         **/
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            // General ListView optimization code.
            // 加载listview每一项的视图
            view = mInflator.inflate(R.layout.ble_list, null);
            // 初始化三个textview显示蓝牙信息
            TextView deviceAddress = (TextView) view.findViewById(R.id.tv_deviceAddr);
            TextView deviceName = (TextView) view.findViewById(R.id.tv_deviceName);
            TextView rssi = (TextView) view.findViewById(R.id.tv_rssi);
            BluetoothDevice device = mLeDevices.get(i);
            deviceAddress.setText(device.getAddress());
            deviceName.setText(device.getName());
            rssi.setText("" + rssis.get(i));
            return view;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
