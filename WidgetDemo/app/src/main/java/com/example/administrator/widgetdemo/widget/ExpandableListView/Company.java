package com.example.administrator.widgetdemo.widget.ExpandableListView;

import java.util.List;

/**
 * Author: huangxiaoming
 * Date: 2018/5/14
 * Desc:
 * Version: 1.0
 */
public class Company implements ExpandableListItem {

    public boolean mExpanded = false;
    public String name;
    public List<Department> mDepartments;

    @Override
    public List<?> getChildItemList() {
        return mDepartments;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpanded = isExpanded;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                '}';
    }
}