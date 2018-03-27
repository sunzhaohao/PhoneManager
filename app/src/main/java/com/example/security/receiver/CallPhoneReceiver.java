package com.example.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.security.service.AddressService;
import com.example.sunzhaohenan.phonemanager.LostProtectedActivity;
import com.example.sunzhaohenan.phonemanager.R;

/**
 * Created by sunzhaohenan on 2018/3/11.
 */

public class CallPhoneReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber=this.getResultData();

        if(context.getResources().getString(R.string.lostNumber).equals(phoneNumber)){
            Intent lostIntent=new Intent(context,LostProtectedActivity.class);
            // 需要开启一个新的task，以便存放lostActivity
            lostIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(lostIntent);
            abortBroadcast(); //中断广播，不会再响比它有优先级低得广播再传播下去
            setResultData(null);
        }


    }
}
