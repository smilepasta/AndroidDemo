package com.example.administrator.widgetdemo.bean;

import android.graphics.drawable.Drawable;

import java.util.List;

/**
 * Author: huangxiaoming
 * Date: 2018/5/14
 * Desc:
 * Version: 1.0
 */
public class MenuBean {
    private int id;
    private String name;
    private int iconId;
    private boolean isChecked;

    private List<MenuBean> childMenuList;

    public MenuBean(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    public MenuBean(int id, String name, int iconId) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getIcon() {
        return iconId;
    }

    public void setIcon(int iconId) {
        this.iconId = iconId;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public List<MenuBean> getChildMenuList() {
        return childMenuList;
    }

    public void setChildMenuList(List<MenuBean> childMenuList) {
        this.childMenuList = childMenuList;
    }
}
