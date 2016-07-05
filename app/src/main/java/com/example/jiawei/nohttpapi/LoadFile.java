package com.example.jiawei.nohttpapi;

/**
 * Created by QYer on 2016/7/5.
 */
public class LoadFile {
    private  String title;
    private  int progress;

    public LoadFile(String title, int progress) {
        this.title = title;
        this.progress = progress;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
