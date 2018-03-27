package com.example.security.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.security.service.AddressService;

/**
 * Created by sunzhaohenan on 2018/3/27.
 */

public class PhoneCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String data=this.getResultData();

        Intent service=new Intent(context, AddressService.class);
        context.startService(service);
    }
}
