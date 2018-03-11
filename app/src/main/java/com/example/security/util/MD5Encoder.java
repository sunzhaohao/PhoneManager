package com.example.security.util;

import android.util.Log;

import java.security.MessageDigest;

/**
 * Created by sunzhaohenan on 2018/3/11.
 */

public class MD5Encoder {

    public static String encode(String password){
        StringBuilder sb=new StringBuilder();
       try{
           MessageDigest mdiges=MessageDigest.getInstance("MD5") ;
           byte[] bytes=mdiges.digest(password.getBytes());
           String tmp=null;
           for (int i=0;i<bytes.length;i++){
               tmp=Integer.toHexString(0xff & bytes[i]);
               if(tmp.length()==1)
                   sb.append("0"+tmp);
               else
                   sb.append(tmp);
           }
       }catch (Exception e){
           e.printStackTrace();
           Log.e("encodeErr","没有该类型加密算法");
       }
       return sb.toString();
    }
}
