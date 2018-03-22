package com.example.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.example.security.service.GPSInfoProvider;

/**
 * Created by sunzhaohenan on 2018/3/22.
 */

public class SmsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu:pdus){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            //拿到短信内容
            String content = smsMessage.getMessageBody();
            //拿到发送人的电话号码
            String sender = smsMessage.getOriginatingAddress();

            if(content.equals("#location#")){
                abortBroadcast();

                GPSInfoProvider gpsInfoProvider=GPSInfoProvider.getInstance(context);
                String location=gpsInfoProvider.getLocation();

                if(location.equals("")==false) {
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(sender,null,"位置："+location,null,null);

                }

            }
        }
    }
}
