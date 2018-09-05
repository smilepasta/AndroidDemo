package com.example.administrator.widgetdemo.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.widget.RoundProgressView;
import com.example.administrator.widgetdemo.widget.waveview.WaveHelper;
import com.example.administrator.widgetdemo.widget.waveview.WaveView;

public class CustomViewActivity extends AppCompatActivity {

    private RoundProgressView roundProgressView;

    private final static int MSG_REFRESH_PROGRESS = 101;
    private int percent = 56;

    private WaveHelper waveHelper;

    private int borderColor = Color.parseColor("#44FFFFFF");
    private int borderWidth = 0;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_REFRESH_PROGRESS:
                    roundProgressView.setProgress(msg.arg1);
                    break;
            }
        }

    };
    private WaveView waveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);

        initView();
    }

    private void initView() {
        roundProgressView = findViewById(R.id.rpv_progress);
        initProgressView();

        waveView = (WaveView) findViewById(R.id.wave);
        waveView.setBorder(borderWidth, borderColor);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaveShiftRatio(0.8f);
        waveHelper = new WaveHelper(waveView);


    }

    private void initProgressView() {
        new Thread(new Runnable() {
            Message msg = null;

            @Override
            public void run() {
                int start = 0;
                while (start <= percent) {
                    msg = new Message();
                    msg.what = MSG_REFRESH_PROGRESS;
                    msg.arg1 = start;
                    handler.sendMessage(msg);
                    start++;
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        waveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        waveHelper.start();
    }
}
