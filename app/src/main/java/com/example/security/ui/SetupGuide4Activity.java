package com.example.security.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.security.receiver.MyAdminReceiver;
import com.example.sunzhaohenan.phonemanager.R;

/**
 * Created by sunzhaohenan on 2018/3/13.
 */


public class SetupGuide4Activity extends Activity implements View.OnClickListener
{
    private Button bt_pervious;
    private Button bt_finish;
    private CheckBox cb_protected;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setup_guide4);

        bt_pervious = (Button) findViewById(R.id.bt_guide_pervious);
        bt_finish = (Button) findViewById(R.id.bt_guide_finish);
        bt_finish.setOnClickListener(this);
        bt_pervious.setOnClickListener(this);

        cb_protected = (CheckBox) findViewById(R.id.cb_guide_protected);

        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        boolean isProtecting = sp.getBoolean("isProtected", false);
        if(isProtecting)
        {
            cb_protected.setText("已经开启保护");
            cb_protected.setChecked(true);
        }

        cb_protected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if(isChecked)
                    cb_protected.setText("已经开启保护");
                else
                    cb_protected.setText("没有开启保护");

                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isProtected", isChecked);
                editor.commit();
            }
        });
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bt_guide_finish :
                if(cb_protected.isChecked())
                {
                    finishSetUpGuide();
                    finish();
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("提醒");
                    builder.setMessage("强烈建议您开启保护, 是否完成设置");
                    builder.setCancelable(false);
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("setupGuide", true);//记录是否已经进行过设置向导了
                            editor.commit();
                            finish();
                        }
                    });
                    builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("setupGuide", true);//记录是否已经进行过设置向导了
                            editor.commit();
                        }
                    });
                    builder.create().show();
                }
                break;

            case R.id.bt_guide_pervious :
                Intent intent = new Intent(this, SetupGuide3Activity.class);
                finish();
                startActivity(intent);
                //这个是定义activity切换时的动画效果的
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                break;

            default :
                break;
        }
    }

    private void finishSetUpGuide(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("setupGuide", true);//记录是否已经进行过设置向导了
        editor.commit();

        DevicePolicyManager devicePolicyManager= (DevicePolicyManager)
                getSystemService(Context.DEVICE_POLICY_SERVICE);

        ComponentName componentName=new ComponentName(this, MyAdminReceiver.class);
        if(!devicePolicyManager.isAdminActive(componentName)){
            Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentName);
            startActivity(intent);
        }
    }
}
