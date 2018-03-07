package com.example.sunzhaohenan.phonemanager;

import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.security.service.UpdateInfoService;
import com.example.security.util.UpdateInfo;

import org.w3c.dom.Text;

public class SplashActivity extends AppCompatActivity {

    private TextView versionTextView;
    private LinearLayout splashLayout;

    private UpdateInfo info=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 不显示标题栏 :该行代码必须位于setContentView() 之前
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);


        // 全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);

        versionTextView=(TextView)findViewById(R.id.tv_splash_version);
        String version=getVersion();
        versionTextView.setText("version: "+version);

        // 动画效果
        splashLayout=(LinearLayout)findViewById(R.id.ll_splash_main);

        AlphaAnimation alphaAnimation=new AlphaAnimation(0.0f,1.0f);
        alphaAnimation.setDuration(2000);

        splashLayout.startAnimation(alphaAnimation);

        if(isNeedUpdate( version )){
            Toast.makeText(this,"begin update",Toast.LENGTH_SHORT).show();
            showUpdateDialog();
        }
    }

    private void showUpdateDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("更新提醒");
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SplashActivity.this,"更新",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SplashActivity.this,"取消更新",Toast.LENGTH_SHORT).show();

            }
        });
        builder.create().show();
    }
    private boolean  isNeedUpdate(String version){

        Runnable connectServer=new Runnable() {

            @Override
            public void run() {
                UpdateInfoService service=new UpdateInfoService(SplashActivity.this);
                try{
                    info=service.getUpdateInfo(R.string.serverUrl);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SplashActivity.this,"查找最新版本程序过程中出现异常，请稍后重试",
                             Toast.LENGTH_SHORT).show();
                }
            }
        };
        new Thread(connectServer).start();

        while(info==null);

        if(info.getVersion().equals(version)){
            Log.d("updateFlag","当前已是最新版本");
            return false;
        }
        else{
            Log.d("updateFlag","可以更新到最新版本");
            return true;
        }


    }
    private String getVersion(){

        try{
            PackageManager packageManager=getPackageManager();
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        }catch (Exception e){
            e.printStackTrace();
            return "未知";
        }

    }
}
