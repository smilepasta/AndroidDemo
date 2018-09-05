package com.example.administrator.widgetdemo.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.XMApp;
import com.example.administrator.widgetdemo.widget.dialog.BottomView;
import com.example.administrator.widgetdemo.widget.wheelview.LoopScrollListener;
import com.example.administrator.widgetdemo.widget.wheelview.LoopView;
import com.example.administrator.widgetdemo.widget.wheelview.popwindow.DatePickerPopWin;
import com.example.administrator.widgetdemo.widget.wheelview.popwindow.TimePickerPopWin;

import java.util.ArrayList;
import java.util.List;

public class WheelViewActivity extends AppCompatActivity {

    Context appContext;
    private LoopView loopView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel_view);
        appContext = XMApp.getInstance();


        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog1();
            }
        });

        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog2();
            }
        });

        findViewById(R.id.btn_4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        LoopView loopView = (LoopView) findViewById(R.id.loop_view);
        loopView.setInitPosition(2);
        loopView.setCanLoop(false);
        loopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {

            }
        });
        loopView.setTextSize(25);//must be called before setDateList
        loopView.setDataList(getData("City"));

        LoopView loopView1 = (LoopView) findViewById(R.id.loop_view1);
        loopView1.setInitPosition(2);
        loopView1.setCanLoop(false);
        loopView1.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {

            }
        });
        loopView1.setTextSize(25);//must be called before setDateList
        loopView1.setDataList(getData("Init"));
    }

    private List<String> getData(String key) {
        List<String> value2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            value2.add(key + i);
        }
        return value2;
    }

    private void showDialog2() {
        BottomView bottomView = new BottomView(this, R.style.bottomViewTheme_Transparent, R.layout.layout_city_picker);
        bottomView.setAnimation(R.style.popwin_anim_style);
        View view = bottomView.getView();
        LoopView loopView = view.findViewById(R.id.l1);
        loopView.setInitPosition(2);
        loopView.setCanLoop(false);
        loopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                loopView1.setDataList(getData("HHH"));
                loopView1.setInitPosition(0);
            }
        });
        loopView.setTextSize(25);//must be called before setDateList
        loopView.setDataList(getData("City"));

        loopView1 = view.findViewById(R.id.l2);
        loopView1.setInitPosition(2);
        loopView1.setCanLoop(false);
        loopView1.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {


            }
        });
        loopView1.setTextSize(25);//must be called before setDateList
        loopView1.setDataList(getData("Street"));

        bottomView.showBottomView(true);
    }

    private void showDialog1() {
        TimePickerPopWin timePickerPopWin = new TimePickerPopWin.Builder(WheelViewActivity.this, new TimePickerPopWin.OnTimePickListener() {
            @Override
            public void onTimePickCompleted(int hour, int minute, String AM_PM, String time) {
                Toast.makeText(WheelViewActivity.this, time, Toast.LENGTH_SHORT).show();
            }
        }).textConfirm("CONFIRM")
                .textCancel("CANCEL")
                .btnTextSize(16)
                .viewTextSize(25)
                .colorCancel(Color.parseColor("#999999"))
                .colorConfirm(Color.parseColor("#009900"))
                .build();
        timePickerPopWin.showPopWin(this);
    }

    private void showDialog() {
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(WheelViewActivity.this, new DatePickerPopWin.OnDatePickedListener() {
            @Override
            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                Toast.makeText(WheelViewActivity.this, dateDesc, Toast.LENGTH_SHORT).show();
            }
        }).textConfirm("CONFIRM") //text of confirm button
                .textCancel("CANCEL") //text of cancel button
                .btnTextSize(16) // button text size
                .viewTextSize(25) // pick view text size
                .colorCancel(Color.parseColor("#999999")) //color of cancel button
                .colorConfirm(Color.parseColor("#009900"))//color of confirm button
                .minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .showDayMonthYear(true) // shows like dd mm yyyy (default is false)
                .dateChose("2013-11-11") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(this);
    }
}
