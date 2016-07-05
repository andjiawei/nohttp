package com.example.jiawei.nohttpapi.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jiawei.nohttpapi.BaseApplication;
import com.example.jiawei.nohttpapi.LoadFile;
import com.example.jiawei.nohttpapi.R;
import com.yolanda.nohttp.download.DownloadRequest;

import java.util.List;

/**
 * Created by QYer on 2016/7/5.
 */
public class DownListAdapter extends BaseAdapter{

    private  List<LoadFile> mFileList;
    private  List<DownloadRequest> downloadRequest;

    public DownListAdapter(List<LoadFile> mFileList, List<DownloadRequest> downloadRequest) {
        this.mFileList = mFileList;
        this.downloadRequest = downloadRequest;
    }

    @Override
    public int getCount() {
        return mFileList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View item = View.inflate(BaseApplication.getInstance(), R.layout.item, null);
        TextView title = (TextView) item.findViewById(R.id.title);
//        TextView message = (TextView) item.findViewById(R.id.message);
        SeekBar seekBar= (SeekBar) item.findViewById(R.id.seekBar);

        title.setText(mFileList.get(position).getTitle());
        seekBar.setProgress(mFileList.get(position).getProgress());

        return item;
    }
}
