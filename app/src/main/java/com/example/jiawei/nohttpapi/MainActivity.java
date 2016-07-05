package com.example.jiawei.nohttpapi;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jiawei.nohttpapi.adapter.DownListAdapter;
import com.example.jiawei.nohttpapi.appconfig.AppConfig;
import com.example.jiawei.nohttpapi.utils.Constants;
import com.example.jiawei.nohttpapi.utils.Logg;
import com.example.jiawei.nohttpapi.utils.Snackbar;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.download.DownloadListener;
import com.yolanda.nohttp.download.DownloadRequest;
import com.yolanda.nohttp.error.ArgumentError;
import com.yolanda.nohttp.error.NetworkError;
import com.yolanda.nohttp.error.ServerError;
import com.yolanda.nohttp.error.StorageReadWriteError;
import com.yolanda.nohttp.error.StorageSpaceNotEnoughError;
import com.yolanda.nohttp.error.TimeoutError;
import com.yolanda.nohttp.error.URLError;
import com.yolanda.nohttp.error.UnKnownHostError;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nohttp.HttpListener;
import nohttp.NohttpHelper;

public class MainActivity extends BaseActivity {

    private final static String PROGRESS_KEY = "download_single_progress";
    private Button downloadButton;
    private SeekBar seekBar;
    private TextView textView;
    private ListView listView;
    private List<LoadFile> mFileList;
    private DownListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadButton = (Button) findViewById(R.id.download);
        seekBar = (SeekBar) findViewById(R.id.seekbar);
        textView = (TextView) findViewById(R.id.textview);
        listView = (ListView) findViewById(R.id.listView);


    }

    public void get(View v){
        String url= Constants.URL_NOHTTP_METHOD;
        NohttpHelper.getRequestInstance().get(this, 0, url, httpListener, true, true);
    }

    public void post(View v){
        String url=Constants.URL_NOHTTP_METHOD;
        NohttpHelper.getRequestInstance().post(this, 0, url, httpListener, true, true);
    }

    public void download(View view){
        //没有传what值 这里没区分每次下载
        NohttpHelper.download(Constants.URL_DOWNLOADS[0], AppConfig.getInstance().APP_PATH_ROOT,"nohttp.apk",true,true,downloadListener);
        // 添加到队列，在没响应的时候让按钮不可用。
        downloadButton.setEnabled(false);
    }

    public void downloadList(View view){
        //每个item的bean的集合
        mFileList = new ArrayList<>();
        String [] arrayFileName=new String[Constants.URL_DOWNLOADS.length];//每一个下载文件名字的数组
        for (int i=0;i<Constants.URL_DOWNLOADS.length;i++){
            arrayFileName[i]="nohttp_list" + i + ".apk";

            // 读取每个文件的进度
            int progress = AppConfig.getInstance().getInt(PROGRESS_KEY + i, 0);

            // 设置每个文件的状态
            String title = getString(R.string.upload_file_status_wait);
            if (progress == 100)
                title = getString(R.string.download_status_finish);
            else if (progress > 0)
                title = getSProgress(progress);

            LoadFile downloadFile = new LoadFile(title, progress);
            mFileList.add(downloadFile);

        }
        List<DownloadRequest> downloadRequest=new ArrayList<>();
        NohttpHelper.downloadList(Constants.URL_DOWNLOADS,AppConfig.getInstance().APP_PATH_ROOT,arrayFileName,true,true,downloadRequest,downloadListener);
        adapter = new DownListAdapter(mFileList,downloadRequest);
        listView.setAdapter(adapter);

    }
    //把进度格式化为带文字的字符串
    private String getSProgress(int progress) {
        String sProgress = getString(R.string.download_progress);
        return String.format(Locale.getDefault(), sProgress, progress);
    }
    //下载的监听
    private DownloadListener downloadListener=new DownloadListener() {

        @Override
        public void onStart(int what, boolean isResume, long rangeSize, Headers responseHeaders, long allCount) {

            int progress = AppConfig.getInstance().getInt(PROGRESS_KEY, 0);
            if (allCount != 0) {
                progress = (int) (rangeSize * 100 / allCount);
            }
            updateProgress(what, progress);
        }

        @Override
        public void onProgress(int what, int progress, long fileCount) {
            AppConfig.getInstance().putInt(PROGRESS_KEY + what, progress);
            updateProgress(what, progress);
        }

        @Override
        public void onFinish(int what, String filePath) {
            // getText(R.string.download_status_finish)
            Snackbar.show(MainActivity.this, getText(R.string.download_status_finish));// 提示下载完成

            mFileList.get(what).setTitle(getString(R.string.download_status_finish));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onDownloadError(int what, Exception exception) {

            Logg.e(exception);
            downloadButton.setText(R.string.download_status_again_download);
            downloadButton.setEnabled(true);

            String message = getString(R.string.download_error);
            String messageContent;
            if (exception instanceof ServerError) {
                messageContent = getString(R.string.download_error_server);
            } else if (exception instanceof NetworkError) {
                messageContent = getString(R.string.download_error_network);
            } else if (exception instanceof StorageReadWriteError) {
                messageContent = getString(R.string.download_error_storage);
            } else if (exception instanceof StorageSpaceNotEnoughError) {
                messageContent = getString(R.string.download_error_space);
            } else if (exception instanceof TimeoutError) {
                messageContent = getString(R.string.download_error_timeout);
            } else if (exception instanceof UnKnownHostError) {
                messageContent = getString(R.string.download_error_un_know_host);
            } else if (exception instanceof URLError) {
                messageContent = getString(R.string.download_error_url);
            } else if (exception instanceof ArgumentError) {
                messageContent = getString(R.string.download_error_argument);
            } else {
                messageContent = getString(R.string.download_error_un);
            }
            message = String.format(Locale.getDefault(), message, messageContent);
            textView.setText(message);

        }

        @Override
        public void onCancel(int what) {
            textView.setText(R.string.download_status_be_pause);
            downloadButton.setText(R.string.download_status_resume);
            downloadButton.setEnabled(true);
        }

        //多个文件同时下载调用
        private void updateProgress(int what, int progress) {
            mFileList.get(what).setTitle(getSProgress(progress));
            mFileList.get(what).setProgress(progress);
            adapter.notifyDataSetChanged();
        }

        //单文件下载调用
        private void updateProgress(int progress) {
            seekBar.setProgress(progress);
            String sProgress = getString(R.string.download_progress);
            sProgress = String.format(Locale.getDefault(), sProgress, progress);
            textView.setText(sProgress);
        }
    };

    //请求的监听 如get post
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
