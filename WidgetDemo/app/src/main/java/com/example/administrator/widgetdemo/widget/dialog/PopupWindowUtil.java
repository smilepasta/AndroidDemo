package com.example.administrator.widgetdemo.widget.dialog;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Author: huangxiaoming
 * Date: 2018/5/14
 * Desc:
 * Version: 1.0
 */
public class PopupWindowUtil {



    public static void showAsDropDownStrong(PopupWindow pw, View anchor, int xoff, int yoff) {
        if (Build.VERSION.SDK_INT >= 24) {
            int[] location = new int[2];
            anchor.getLocationOnScreen(location);
            //【note!】Gets the screen height without the virtual key
            WindowManager wm = (WindowManager) pw.getContentView().getContext().getSystemService(Context.WINDOW_SERVICE);
            int screenHeight = wm.getDefaultDisplay().getHeight();
                /*
                /*
                 * PopupWindow height for match_parent,
                */
            pw.setHeight(screenHeight - location[1] - anchor.getHeight() - yoff);
            pw.showAtLocation(anchor, Gravity.NO_GRAVITY, xoff, location[1] + anchor.getHeight() + yoff);
        } else {
            pw.showAsDropDown(anchor, xoff, yoff);
        }
    }
}
