package com.example.security.service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.example.security.domain.ContactInfo;
import com.example.sunzhaohenan.phonemanager.SplashActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunzhaohenan on 2018/3/13.
 */

public class ContactInfoService {
    private Context context;
    public ContactInfoService(Context context){
        this.context=context;
    }

    public List<ContactInfo> getContactInfos(){
        List<ContactInfo> infos=new ArrayList<>();
        ContactInfo info;

        ContentResolver contentResolver=context.getContentResolver();
        Uri uri= Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri=Uri.parse("content://com.android.contacts/data");

        Cursor cursor=contentResolver.query(uri,null,null,null,null);
        while(cursor.moveToNext()){
            info=new ContactInfo();
            String id=cursor.getString(cursor.getColumnIndex("_id"));
            String name=cursor.getString(cursor.getColumnIndex("display_name"));
            info.setName(name);

            Cursor dataCursor = contentResolver.query(dataUri, null, "raw_contact_id = ? ", new String[] {id}, null);
            while(dataCursor.moveToNext())
            {
                String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
                //根据类型，只要电话这种类型的数据
                if(type.equals("vnd.android.cursor.item/phone_v2"))
                {
                    String number = dataCursor.getString(dataCursor.getColumnIndex("data1"));//拿到数据
                    info.setPhone(number);
                }
            }
            dataCursor.close();
            infos.add(info);
            info = null;
        }
        cursor.close();
        return infos;
    }

}
