package com.example.administrator.widgetdemo.widget.ExpandableListView;

import android.view.View;
import android.widget.TextView;

import com.example.administrator.widgetdemo.R;

/**
 * Author: huangxiaoming
 * Date: 2018/5/14
 * Desc:
 * Version: 1.0
 */
public class DepartmentItem extends AbstractAdapterItem {

    private TextView mName;

    @Override
    public int getLayoutResId() {
        return R.layout.item_employee;
    }

    @Override
    public void onBindViews(View root) {
        mName = (TextView) root.findViewById(R.id.tv_name);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        if (model instanceof Department) {
            Department employee = (Department) model;
            mName.setText(employee.name);
        }
    }
}