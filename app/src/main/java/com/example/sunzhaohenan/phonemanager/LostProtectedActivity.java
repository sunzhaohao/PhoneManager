package com.example.sunzhaohenan.phonemanager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.security.ui.SetupGuide1Activity;
import com.example.security.util.MD5Encoder;

public class LostProtectedActivity extends AppCompatActivity implements View.OnClickListener{

    private SharedPreferences sp;
    private Dialog dialog;
    private EditText password;
    private EditText confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        sp = getSharedPreferences("cofig", Context.MODE_PRIVATE);

        if(isSetPassword())
        {
            showLoginDialog();
        }
        else
        {
            showFirstDialog();
        }
    }

    private void showLoginDialog()
    {
        dialog = new Dialog(this, R.style.MyDialog);
        View view = View.inflate(this, R.layout.login_dialog, null);
        password = (EditText) view.findViewById(R.id.et_protected_password);

        Button yes = (Button) view.findViewById(R.id.bt_protected_login_yes);
        Button cancel = (Button) view.findViewById(R.id.bt_protected_login_no);

        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void showFirstDialog()
    {
        dialog = new Dialog(this, R.style.MyDialog);
        //dialog.setContentView(R.layout.first_dialog);
        View view = View.inflate(this, R.layout.first_dialog, null);//这种填充布局的方式比较方便，峭用拿到一个LayoutInflate对象

        password = (EditText) view.findViewById(R.id.et_protected_first_password);
        confirmPassword = (EditText) view.findViewById(R.id.et_protected_confirm_password);

        Button yes = (Button) view.findViewById(R.id.bt_protected_first_yes);
        Button cancel = (Button) view.findViewById(R.id.bt_protected_first_no);

        yes.setOnClickListener(this);
        cancel.setOnClickListener(this);

        dialog.setContentView(view);
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean isSetPassword()
    {
        String pwd = sp.getString("password", "");
        if( pwd == null|| pwd.equals("") )
            return false;

        return true;
    }

    private boolean isSetupGuide()
    {
        return sp.getBoolean("setupGuide", false);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.bt_protected_first_yes :
                String fp = password.getText().toString().trim();
                String cp = confirmPassword.getText().toString().trim();
                if("".equals(fp) || "".equals(cp))
                {
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    if(fp.equals(cp))
                    {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", MD5Encoder.encode(fp));
                        editor.commit();
                        dialog.dismiss();

                        if(!isSetupGuide())
                        {
                            finish();
                            Intent intent = new Intent(this, SetupGuide1Activity.class);
                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Toast.makeText(this, "两次密码不相同", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                dialog.dismiss();
                break;

            case R.id.bt_protected_first_no :
                dialog.dismiss();
                finish();
                break;

            case R.id.bt_protected_login_yes :
                String pwd = password.getText().toString().toString();
                if(pwd.equals(""))
                {
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String str = sp.getString("password", "");
                    if(MD5Encoder.encode(pwd).equals(str))
                    {
                        if(!isSetupGuide())
                        {
                            finish();
                            Intent intent = new Intent(this, SetupGuide1Activity.class);
                            startActivity(intent);
                        }
                        dialog.dismiss();
                    }
                    else
                    {
                        Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.bt_protected_login_no :
                dialog.dismiss();
                finish();
                break;

            default :
                break;
        }
    }

}
