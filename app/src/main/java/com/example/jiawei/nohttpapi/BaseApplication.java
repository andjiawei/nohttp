package com.example.jiawei.nohttpapi;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by QYer on 2016/7/4.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
    }
}
