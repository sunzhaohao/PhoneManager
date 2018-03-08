package com.example.security.service;

import android.app.Activity;
import android.app.ProgressDialog;

import android.util.Log;
import android.widget.ProgressBar;

import com.example.sunzhaohenan.phonemanager.SplashActivity;

import java.io.*;
/**
 * Created by sunzhaohenan on 2018/3/8.
 */

public class UpdateThread implements Runnable {
    private String urlPath;
    private String filePath;

    private ProgressDialog progressBar;
    private SplashActivity activity;
    public void setPaths(String urlPath,String filePath){
        this.urlPath=urlPath;
        this.filePath=filePath;
    }
    public void setProgressBar(ProgressDialog progressBar){
        this.progressBar=progressBar;
    }
    public void setActivity(SplashActivity splashActivity){
        this.activity=splashActivity;
    }
    @Override
    public void run() {
        try{
            File file=DownloadTask.downloadFile(urlPath,filePath,progressBar);
            Log.e("dsf",file.getAbsolutePath());

            progressBar.dismiss();

            String command = "chmod " + "777" + " " + file.getAbsolutePath();
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);

            activity.install(file);

        }catch ( Exception e){
            e.printStackTrace();
            Log.e("downloadErr","download failed");
        }

        Log.e("downloadSucc","success");
    }
}
