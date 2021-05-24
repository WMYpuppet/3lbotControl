package com.example.a3lbotcontrol.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.a3lbotcontrol.R;
import com.example.a3lbotcontrol.fragment.HomeFragment;
import com.example.a3lbotcontrol.fragment.MineFragment;
import com.example.a3lbotcontrol.fragment.ShopInformationFragment;

public class HomeActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    HomeFragment homeFragment;
    ShopInformationFragment shopInformationFragment;
    MineFragment mineFragment;
    FragmentManager fragmentManager;
    RadioGroup radioGroup;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        radioGroup = (RadioGroup) findViewById(R.id.rg);
        radioGroup.setOnCheckedChangeListener(this);
        fragmentManager = getSupportFragmentManager();
        if (homeFragment == null) {
            homeFragment = new HomeFragment();
        }
        fragmentManager.beginTransaction().add(R.id.rl__main_container, homeFragment).commit();
    }


    private void init() {
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

        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 无权限，请求申请
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            } else {
                //具有权限
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        hide(ft);
        switch (checkedId) {
            case R.id.rb_home:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    ft.add(R.id.rl__main_container, homeFragment);
                } else {
                    ft.show(homeFragment);
                }
                break;

            case R.id.rb_shop:
                if (shopInformationFragment == null) {
                    shopInformationFragment = new ShopInformationFragment();
                    ft.add(R.id.rl__main_container, shopInformationFragment);
                } else {
                    ft.show(shopInformationFragment);
                }
                break;

            case R.id.rb_mine:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    ft.add(R.id.rl__main_container, mineFragment);
                } else {
                    ft.show(mineFragment);
                }
                break;
        }
        ft.commit();
    }

    public void hide(FragmentTransaction ft) {
        if (homeFragment != null) {
            ft.hide(homeFragment);
        }
        if (shopInformationFragment != null) {
            ft.hide(shopInformationFragment);
        }
        if (mineFragment != null) {
            ft.hide(mineFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再次点击退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {

                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
