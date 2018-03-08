package com.example.security.service;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.*;

/**
 * Created by sunzhaohenan on 2018/3/8.
 */

public class DownloadTask  {

    public static File downloadFile(String path, String filePath, ProgressDialog progressBar)throws Exception{
        URL url=new URL(path);
        HttpURLConnection conn=(HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);

        if(conn.getResponseCode()==200){

            int total=conn.getContentLength();
            progressBar.setMax(total);

            InputStream is=conn.getInputStream();
            File file=new File(filePath);

            file.createNewFile();
            FileOutputStream out=new FileOutputStream(file);

            byte[] data=new byte[1024];
            int progress=0;
            int len=0;
            while( (len=is.read(data))!=-1){
                out.write(data,0,len);
                progress+=len;
                progressBar.setProgress(progress);
            }

            out.flush();
            out.close();
            is.close();;
            return file;
        }
        return null;
    }
}
