package com.example.security.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;

import com.example.sunzhaohenan.phonemanager.R;

/**
 * Created by sunzhaohenan on 2018/3/25.
 */

public class QueryNumberActivity extends Activity {

    private EditText queryNumber_et;
    private Button query_bt;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queryNumber_et=(EditText)findViewById(R.id.et_query_number);
        query_bt=(Button)findViewById(R.id.bt_query);
    }
}
