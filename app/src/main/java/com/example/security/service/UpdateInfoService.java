package com.example.security.service;

import android.content.Context;

import com.example.security.util.UpdateInfo;
import com.example.security.util.UpdateInfoParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sunzhaohenan on 2018/3/7.
 */

public class UpdateInfoService {

    private Context context;

    public UpdateInfoService(Context context){
        this.context=context;
    }

    public UpdateInfo getUpdateInfo(int urlId)throws Exception{
        String path=context.getResources().getString(urlId);
        URL url=new URL(path);

        HttpURLConnection connection=(HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(5000);
        connection.setRequestMethod("GET");

        InputStream inputStream=connection.getInputStream();

        return UpdateInfoParser.getUpdateInfo(inputStream);
    }
}
