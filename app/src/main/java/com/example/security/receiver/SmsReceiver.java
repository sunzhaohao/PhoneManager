package com.example.security.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.example.security.service.GPSInfoProvider;
import com.example.sunzhaohenan.phonemanager.R;

/**
 * Created by sunzhaohenan on 2018/3/22.
 */

public class SmsReceiver extends BroadcastReceiver {
    private  Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object pdu:pdus){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
            //拿到短信内容
            String content = smsMessage.getMessageBody();
            //拿到发送人的电话号码
            String sender = smsMessage.getOriginatingAddress();


            switch (content){
                case "#location#":
                    sendLocation(sender);
                    break;
                case "#lockScreen#":
                    lockScreen();
                    break;
                case "#wipe#":
                    wipe();
                    break;
                case "#alerm#":
                    alerm();
                    break;
                default:
                    break;
            }
        }
    }

    private void sendLocation(String sender){
        abortBroadcast();

        GPSInfoProvider gpsInfoProvider=GPSInfoProvider.getInstance(context);
        String location=gpsInfoProvider.getLocation();

        if(location.equals("")==false) {
            SmsManager smsManager=SmsManager.getDefault();
            smsManager.sendTextMessage(sender,null,"位置："+location,null,null);

        }
    }

    private void lockScreen(){
        DevicePolicyManager devicePolicyManager=(DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);


        //devicePolicyManager.resetPassword("12345",0);
        devicePolicyManager.lockNow();
        abortBroadcast();
    }

    private void wipe(){
        DevicePolicyManager devicePolicyManager=(DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);

        Log.e("wipeErr","begin");
        devicePolicyManager.wipeData(0);
        abortBroadcast();
    }
    private void alerm(){
        MediaPlayer mediaPlayer=MediaPlayer.create(context, R.raw.ok);

        mediaPlayer.setVolume(1,1);
        mediaPlayer.start();
    }

}
