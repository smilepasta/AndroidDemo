package com.example.administrator.widgetdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.administrator.widgetdemo.R;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:全局异常处理
 * Version:1.0
 */
public class CrashHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_crash);


        findViewById(R.id.button_crash_main_thread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                throw new RuntimeException("I'm a cool exception and I crashed the main thread!");
            }
        });

    }
}
