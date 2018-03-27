package com.example.sunzhaohenan.phonemanager;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.security.service.DownloadTask;
import com.example.security.service.UpdateInfoService;
import com.example.security.service.UpdateThread;
import com.example.security.util.UpdateInfo;

import org.w3c.dom.Text;

import java.io.EOFException;
import java.io.File;


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

        checkPermissions();
        obj=this;
        if(isNeedUpdate( version )){
            Toast.makeText(this,"begin update",Toast.LENGTH_SHORT).show();
            showUpdateDialog();
        }
        else
        {
            Toast.makeText(this,"what",Toast.LENGTH_SHORT).show();
            loadMainUI();
        }

    }

    private void checkPermissions(){
        checkPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        checkPermission(this,Manifest.permission.PROCESS_OUTGOING_CALLS);
        checkPermission(this,Manifest.permission.READ_PHONE_STATE);
        checkPermission(this,Manifest.permission.READ_CONTACTS);
        checkPermission(this,Manifest.permission.WRITE_CONTACTS);

        checkPermission(this,Manifest.permission.RECEIVE_BOOT_COMPLETED);
        checkPermission(this,Manifest.permission.SEND_SMS);

        checkPermission(this,Manifest.permission.RECEIVE_SMS);
        checkPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION);
        checkPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);

        checkPermission(this,Manifest.permission.BIND_DEVICE_ADMIN);
        checkPermission(this,Manifest.permission.REBOOT);

        checkPermission(this,Manifest.permission.READ_PHONE_STATE);
    }

    private void checkPermission(Activity activity,String permission){
        if(ContextCompat.checkSelfPermission(activity ,
                permission)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{permission},1);
        }
    }

    public static SplashActivity obj;
    private void showUpdateDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("更新提醒");
        builder.setMessage(info.getDescription());
        builder.setCancelable(false);

        builder.setPositiveButton("现在更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                    String path="/security/update";
                    File dir=new File(Environment.getExternalStorageDirectory(),path);
                    if(dir.exists()==false)
                      dir.mkdirs();

                    boolean flag=dir.exists();

                    String apkPath=Environment.getExternalStorageDirectory()+path+"/new.apk";

                    // 下载文件线程
                    UpdateThread thread=new UpdateThread();
                    thread.setPaths(info.getApkUrl(),apkPath);
                    thread.setActivity(obj);
                    ProgressDialog progressBar=new ProgressDialog(SplashActivity.this);
                    thread.setProgressBar(progressBar);
                    progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressBar.setMessage("正在下载...");

                    progressBar.show();
                    new Thread(thread).start();

                }else{
                    Toast.makeText(SplashActivity.this,"SD 卡不可用！",Toast.LENGTH_SHORT);
                    loadMainUI();
                }
            }

        });

        builder.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(SplashActivity.this,"取消更新",Toast.LENGTH_SHORT).show();
                loadMainUI();
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
                    Log.e("serverErr","cannot connect to server");

                }
            }
        };
        new Thread(connectServer).start();

        while(info==null);

        if(info.getVersion().equals(version)){
            Log.d("updateFlag","当前已是最新版本");
            loadMainUI();
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

    private void loadMainUI(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void install(File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);

        // 由于没有在Activity环境下启动Activity,设置下面的标签
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Log.e("fileProvider",FileProvider.class+"");
        Uri apkUri = FileProvider.getUriForFile(this, "com.example.sunzhaohenan.phonemanager", file);

        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

        startActivity(intent);
        finish();

    }
}
