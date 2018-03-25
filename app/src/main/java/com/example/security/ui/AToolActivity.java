package com.example.security.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.sunzhaohenan.phonemanager.R;

import java.lang.reflect.AccessibleObject;

/**
 * Created by sunzhaohenan on 2018/3/25.
 */

public class AToolActivity extends Activity implements View.OnClickListener {
    private TextView query_tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atool);

        query_tv=(TextView)findViewById(R.id.tv_atool_query);
        query_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_atool_query:
                Intent intent=new Intent(this,QueryNumberActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
