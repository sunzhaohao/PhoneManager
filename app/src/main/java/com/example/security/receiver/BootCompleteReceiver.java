package com.example.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * Created by sunzhaohenan on 2018/3/21.
 */

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sp=context.getSharedPreferences("config",Context.MODE_PRIVATE);
        boolean isProtected=sp.getBoolean("isProtected",false);

        if(isProtected){
            TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String currentSim=telephonyManager.getSimSerialNumber();
            String oldSim=sp.getString("simSerial","");

            if(currentSim.equals(oldSim)==false){
                SmsManager smsManager=SmsManager.getDefault();
                String number=sp.getString("number","");
                smsManager.sendTextMessage(number,null,"手机被盗",null,null);
            }
        }
    }
}
