package com.example.jiawei.nohttpapi;

import android.os.Bundle;
import android.view.View;

import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Response;

import nohttp.HttpListener;
import nohttp.NohttpHelper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void get(View v){
        String url=Constants.URL_NOHTTP_METHOD;
        NohttpHelper.getRequestInstance().get(this, 0, url, httpListener, true, true);
    }

    public void post(View v){
        String url=Constants.URL_NOHTTP_METHOD;
        NohttpHelper.getRequestInstance().get(this, 0, url, httpListener, true, true);
    }

    private HttpListener<String> httpListener = new HttpListener<String>() {

        @Override
        public void onSucceed(int what, Response<String> response) {
            int responseCode = response.getHeaders().getResponseCode();// 服务器响应码
            if (responseCode == 200) {
                if (RequestMethod.HEAD == response.getRequestMethod())// 请求方法为HEAD时没有响应内容
                    showMessageDialog("请求成功","head无响应");
                else
                    showMessageDialog("请求成功", response.get());
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            showMessageDialog("请求失败", exception.getMessage());
        }
    };


    @Override
    public void onBackPressed() {
        // 程序退出时取消所有请求
        NohttpHelper.getRequestInstance().cancelAll();

        // 程序退出时停止请求队列，如果这里的NoHttpRequestQueue是单例模式，NoHttp所在的进程没杀死而停止了队列，会导致再打开app不能请求网络
        NohttpHelper.getRequestInstance().stopAll();

        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
