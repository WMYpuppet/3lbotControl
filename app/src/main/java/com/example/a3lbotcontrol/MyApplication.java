package com.example.a3lbotcontrol;

import android.app.Application;
import android.content.Context;

import com.example.a3lbotcontrol.util.GreenDaoManager;

/**
 * 作者：Created by Administrator on 2020/9/9.
 * 邮箱：
 */
public class MyApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        GreenDaoManager.initGreenDao();
    }
    public  static  Context getContext(){
        return  mContext;
    }
}
