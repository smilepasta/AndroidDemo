package com.example.administrator.widgetdemo.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.administrator.widgetdemo.R;

import java.io.File;

/**
 * Author:huangxiaoming
 * Date:2016/12/20 0020
 * Desc:
 */

public class GlideUtil {
    public static void loadImage(String url, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(url)
                .error(R.mipmap.error)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadImage(File file, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(file)
                .error(R.mipmap.error)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadImage(int resourceId, ImageView imageView) {
        Context context = imageView.getContext();
        Glide.with(context)
                .load(resourceId)
                .error(R.mipmap.error)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }
}
