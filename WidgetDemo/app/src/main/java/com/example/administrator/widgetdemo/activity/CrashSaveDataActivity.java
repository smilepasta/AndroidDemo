package com.example.administrator.widgetdemo.activity;

import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.example.administrator.widgetdemo.R;

/**
 * (1)、当用户按下HOME键时。
 * 　　这是显而易见的，系统不知道你按下HOME后要运行多少其他的程序，自然也不知道activity A是否会被销毁，因此系统会调用onSaveInstanceState()，让用户有机会保存某些非永久性的数据。以下几种情况的分析都遵循该原则
 * 　　(2)、长按HOME键，选择运行其他的程序时。
 * 　　(3)、按下电源按键（关闭屏幕显示）时。
 * 　　(4)、从activity A中启动一个新的activity时。
 * 　　(5)、屏幕方向切换时，例如从竖屏切换到横屏时。
 */
public class CrashSaveDataActivity extends AppCompatActivity {

    private AppCompatEditText nameEditText;
    private AppCompatEditText ageEditText;
    private AppCompatEditText idealEditText;
    private AppCompatButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_save_data);

        Log.i("===", "onCreate");
        initView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("===", "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("===", "onStart");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("===", "onRestoreInstanceState");

        nameEditText.setText(savedInstanceState.getString("name"));
        ageEditText.setText(savedInstanceState.getString("age"));
        idealEditText.setText(savedInstanceState.getString("ideal"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("===", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("===", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("===", "onStop");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", nameEditText.getText().toString());
        outState.putString("age", ageEditText.getText().toString() + 10);
        outState.putString("ideal", idealEditText.getText().toString());
        Log.i("===", "onSaveInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("===", "onDestroy");
    }

    private void initView() {
        nameEditText = findViewById(R.id.et_name);
        ageEditText = findViewById(R.id.et_age);
        idealEditText = findViewById(R.id.et_ideal);
        submitButton = findViewById(R.id.btn_submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] ii = new int[1];
                Log.i("===", ii[10] + "");
            }
        });
    }

}
