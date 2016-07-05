package com.example.jiawei.nohttpapi;

import android.app.Application;

import com.example.jiawei.nohttpapi.appconfig.AppConfig;
import com.example.jiawei.nohttpapi.utils.Logg;
import com.yolanda.nohttp.NoHttp;

/**
 * Created by QYer on 2016/7/4.
 */
public class BaseApplication extends Application {

    private static Application _instance;
    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        NoHttp.initialize(this);//初始化nohttp
        //设置tag和show log
        Logg.setTag("NoHttpSample");
        Logg.setDebug(true);
        AppConfig.getInstance();//这里不写后边会报空指针
    }

    public static Application getInstance() {
        return _instance;
    }

    /**
     * Created by QYer on 2016/7/5.
     */
    public static class DownListAdapter {
    }
}
