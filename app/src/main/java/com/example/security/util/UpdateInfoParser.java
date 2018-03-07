package com.example.security.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

/**
 * Created by sunzhaohenan on 2018/3/7.
 */

public class UpdateInfoParser {

    public static UpdateInfo getUpdateInfo(InputStream is)throws  Exception{

        UpdateInfo updateInfo=new UpdateInfo();
        XmlPullParser xmlPullParser= Xml.newPullParser();
        xmlPullParser.setInput(is,"utf-8");

        int type=xmlPullParser.getEventType();
        while( type != XmlPullParser.END_DOCUMENT){
            switch (type){
                case XmlPullParser.START_TAG:
                    if(xmlPullParser.getName().equals("version"))
                        updateInfo.setVersion(xmlPullParser.nextText());

                    if(xmlPullParser.getName().equals("description"))
                        updateInfo.setDescription(xmlPullParser.nextText());

                    if(xmlPullParser.getName().equals("apkurl"))
                        updateInfo.setApkUrl(xmlPullParser.nextText());
                    break;
                default:
                    break;

            }
            type=xmlPullParser.next();
        }
        return updateInfo;
    }
}
