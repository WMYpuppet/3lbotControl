package com.example.a3lbotcontrol.util;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.example.a3lbotcontrol.MyApplication;
import com.example.a3lbotcontrol.bean.InformationHistory;
import com.example.a3lbotcontrol.bean.User;
import com.example.a3lbotcontrol.gen.DaoMaster;
import com.example.a3lbotcontrol.gen.DaoSession;
import com.example.a3lbotcontrol.gen.InformationHistoryDao;
import com.example.a3lbotcontrol.gen.UserDao;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：Created by Administrator on 2020/9/9.
 * 邮箱：
 */
public class GreenDaoManager {
    private static DaoSession mDaoSession, mDaoSessionIH;
    private static UserDao mUserDao;
    private static InformationHistoryDao mIHDao;
    private static GreenDaoManager instance;

    private GreenDaoManager() {

    }

    public static GreenDaoManager getInstance() {
        if (instance == null) {
            synchronized (GreenDaoManager.class) {
                if (instance == null) {
                    instance = new GreenDaoManager();
                }
            }
        }
        return instance;
    }

    public static void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApplication.getContext(), "user.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
        mUserDao = mDaoSession.getUserDao();

        DaoMaster.DevOpenHelper helperIH = new DaoMaster.DevOpenHelper(MyApplication.getContext(), "informationhistory.db");
        SQLiteDatabase dbIH = helperIH.getWritableDatabase();
        DaoMaster daoMasterIH = new DaoMaster(dbIH);
        mDaoSessionIH = daoMasterIH.newSession();
        mIHDao = mDaoSessionIH.getInformationHistoryDao();
    }

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            throw new RuntimeException("亲初始化");
        }
        return mDaoSession;
    }

    public UserDao getUserDao() {
        if (mUserDao == null) {
            throw new RuntimeException("亲初始化");
        }
        return mUserDao;
    }

    public InformationHistoryDao getInformationHistoryDao() {
        if (mIHDao == null) {
            throw new RuntimeException("初始化IH");
        }
        return mIHDao;
    }

    //插入数据IH
    public void saveBeanIH(int yearIH, int monthIH, int daylongIH, int timeIH, int stepfrequencyIH, int angleIH) {
        getInformationHistoryDao().insert(new InformationHistory(yearIH, monthIH, daylongIH, timeIH, stepfrequencyIH, angleIH));
    }

    public void queryWhereDayIH(int year, int month) {
        List<InformationHistory> informationHistoryList = getInformationHistoryDao().queryBuilder().where(InformationHistoryDao.Properties.YearIH.eq(year),
                InformationHistoryDao.Properties.MonthIH.eq(month)).build().list();
        ArrayList<Integer> listDay = new ArrayList<>();
        ArrayList<Integer> listTime = new ArrayList<>();
        ArrayList<Integer> listStepfrequency = new ArrayList<>();
        ArrayList<Integer> listAngle = new ArrayList<>();
        for (InformationHistory ih : informationHistoryList) {
            listDay.add(ih.getDaylongIH());
            listTime.add(ih.getTimeIH());
            listStepfrequency.add(ih.getStepfrequencyIH());
            listAngle.add(ih.getAngleIH());
        }

    }

    //插入数据
    public void saveBean(int h, int w, String s, String p) {
        getUserDao().insert(new User(h, w, s, p));
    }

    //条件查询
    public void queryWhere(String iphone) {
        User user = getUserDao().queryBuilder().
                where(UserDao.Properties.PhoneNumber.eq(iphone)).build().unique();
        if (user != null) {
            Log.e("query22222", "电话：" + user.getPhoneNumber() + " 身高：" + user.getHeight() + "体重：" + user.getWeight() + " 性别：" + user.getSex());
        } else {
            Toast.makeText(MyApplication.getContext(), "该用户不存在", Toast.LENGTH_SHORT).show();
        }
    }

    //条件修改
    public void updateWhere(int h, int w, String s, String p) {
        User user = getUserDao().queryBuilder().where(UserDao.Properties.PhoneNumber.eq(p)).build().unique();
        if (user == null) {
            Toast.makeText(MyApplication.getContext(), "该用户不存在", Toast.LENGTH_SHORT).show();
        } else {
            user.setHeight(h);
            user.setWeight(w);
            user.setSex(s);
            user.setPhoneNumber(p);
            GreenDaoManager.getInstance().getUserDao().updateInTx(user);
        }
    }

}
