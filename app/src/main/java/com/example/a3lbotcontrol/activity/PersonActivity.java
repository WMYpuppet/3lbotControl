package com.example.a3lbotcontrol.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a3lbotcontrol.MyApplication;
import com.example.a3lbotcontrol.R;
import com.example.a3lbotcontrol.bean.User;
import com.example.a3lbotcontrol.gen.UserDao;
import com.example.a3lbotcontrol.util.GreenDaoManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonActivity extends AppCompatActivity {
    String strSex, strIphone, intentIphone;
    int height, weight;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.et_iphone)
    EditText etIphone;
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.et_sex)
    EditText etSex;
    @BindView(R.id.btn_update)
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
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

        initView();
    }

    //初始化
    private void initView() {
        intentIphone = getIntent().getStringExtra("iphone");
        if (!TextUtils.isEmpty(intentIphone)) {
            User user = GreenDaoManager.getInstance().getUserDao().queryBuilder().where(UserDao.Properties.PhoneNumber.eq(intentIphone)).build().unique();
            if (user != null) {
                etIphone.setText(intentIphone);
                etHeight.setText(Integer.toString(user.getHeight()));
                etWeight.setText(Integer.toString(user.getWeight()));
                etSex.setText(user.getSex());
            } else {
                Toast.makeText(MyApplication.getContext(), "该用户不存在", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @OnClick({R.id.btn_save, R.id.btn_update})
    public void onViewClicked(View view) {
        strSex = etSex.getText().toString();
        strIphone = etIphone.getText().toString();
        if (!TextUtils.isEmpty(etHeight.getText().toString())) {
            height = Integer.parseInt(etHeight.getText().toString());
        }
        if (!TextUtils.isEmpty(etWeight.getText().toString())) {
            weight = Integer.parseInt(etWeight.getText().toString());
        }
        switch (view.getId()) {
            case R.id.btn_save:
                if (!TextUtils.isEmpty(strIphone)) {
                    User user = GreenDaoManager.getInstance().getUserDao().queryBuilder().
                            where(UserDao.Properties.PhoneNumber.eq(strIphone)).build().unique();
                    if (user == null) {
                        GreenDaoManager.getInstance().saveBean(height, weight, strSex, strIphone);
                    } else {
                        Toast.makeText(this, "该用户已存在", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_update:
                GreenDaoManager.getInstance().updateWhere(height, weight, strSex, strIphone);
                break;
        }
    }


}
