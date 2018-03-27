package com.example.security.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by sunzhaohenan on 2018/3/27.
 */

public class AddressService extends Service {
    private TelephonyManager telephonyManager;
    private MyPhoneListener listener;
    private WindowManager windowManager;
    private TextView tv;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        listener = new MyPhoneListener();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //停止监听
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

    //显示归属地的窗体
    private void showLocation(String address)
    {
        /*WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE //无法获取焦点
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE //无法点击
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//保持屏幕亮
        params.format = PixelFormat.TRANSLUCENT;//设置成半透明的
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");

        tv = new TextView(AddressService.this);
        tv.setText("归属地： " + address);
        windowManager.addView(tv, params);
        */
        Toast.makeText(this,address,Toast.LENGTH_SHORT).show();
    }

    //========================================================================

    private class MyPhoneListener extends PhoneStateListener
    {
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            super.onCallStateChanged(state, incomingNumber);

            switch(state)
            {
                case TelephonyManager.CALL_STATE_IDLE : //空闲状态
                    if(tv != null)
                    {
                       // windowManager.removeView(tv);//移除显示归属地的那个view
                        tv = null;
                    }
                    break;

                case TelephonyManager.CALL_STATE_OFFHOOK : //接通电话
                    if(tv != null)
                    {
                       // windowManager.removeView(tv);//移除显示归属地的那个view
                        tv = null;
                    }
                    break;

                case TelephonyManager.CALL_STATE_RINGING : //铃响状态
                    //String address = NumberAddressService.getAddress(incomingNumber);
                    String address="ok";
                    showLocation(incomingNumber);
                    break;

                default :
                    break;
            }
        }
    }

}
