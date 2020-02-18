package com.example.myapplication;


import android.app.Application;

import org.xutils.x;

/**
 * Application类，
 * 单例方法，数据共享，
 */

public class MyApp extends Application {

    /**
     * 实现单例，任何一个页面都能拿到这个类的数据和对象
     */
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);//注册xUtils
        instance = this;
    }


}