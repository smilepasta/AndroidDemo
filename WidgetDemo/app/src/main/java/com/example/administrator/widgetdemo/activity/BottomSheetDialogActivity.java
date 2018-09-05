package com.example.administrator.widgetdemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.adpater.MenuListAdapter;
import com.example.administrator.widgetdemo.bean.MenuBean;
import com.example.administrator.widgetdemo.fragment.FullSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetDialogActivity extends AppCompatActivity {

    BottomSheetBehavior behavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_sheet_dialog);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet状态的改变
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });


        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(BottomSheetDialogActivity.this);
                View view = getLayoutInflater().inflate(R.layout.dialog_sheet, null);
                RecyclerView recyclerView = view.findViewById(R.id.rv_list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BottomSheetDialogActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                List<MenuBean> menuBeanList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    MenuBean menuBean = new MenuBean(i, "条目" + i, false);
                    menuBeanList.add(menuBean);
                }
                MenuListAdapter menuListAdapter = new MenuListAdapter(BottomSheetDialogActivity.this, menuBeanList);
                recyclerView.setAdapter(menuListAdapter);
                mBottomSheetDialog.setContentView(view);
                mBottomSheetDialog.show();
            }
        });

        findViewById(R.id.btn_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FullSheetDialogFragment().show(getSupportFragmentManager(), "dialog");
            }
        });

    }
}
