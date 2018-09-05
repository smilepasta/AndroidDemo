package com.example.administrator.widgetdemo.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.administrator.widgetdemo.R;

/**
 * Author: huangxiaoming
 * Date: 2018/7/4
 * Desc: 简单环形进度条
 * Version: 1.0
 */
public class RoundProgressView extends View {
    private Paint paint; // 画笔对象的引用
    private Paint paintText;//画文字
    private int roundColor; // 圆环的颜色
    private float roundWidth; // 圆环的宽度
    private int progressColor; // 圆环进度的颜色
    private float progressWidth; // 圆环进度的宽度
    private int max; // 最大进度
    private int style; // 进度的风格，实心或者空心
    private int startAngle; // 进度条起始角度
    private float txtSize;
    private int txtColor;
    public static final int STROKE = 0; // 样式：空心
    public static final int FILL = 1; // 样式：实心
    private int progress; // 当前进度
    private String textVal;

    public RoundProgressView(Context context) {
        super(context);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        //添加文字的画加入以下代码
        paintText = new Paint();

        // 读取自定义属性的值
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressView);

        // 获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressView_srp_roundColor, Color.WHITE);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressView_srp_roundWidth, 5);
        progressColor = mTypedArray.getColor(R.styleable.RoundProgressView_srp_progressColor, Color.WHITE);
        progressWidth = mTypedArray.getDimension(R.styleable.RoundProgressView_srp_progressWidth, roundWidth);
        max = mTypedArray.getInteger(R.styleable.RoundProgressView_srp_max, 100);
        style = mTypedArray.getInt(R.styleable.RoundProgressView_srp_style, 0);
        startAngle = mTypedArray.getInt(R.styleable.RoundProgressView_srp_startAngle, 0);
        txtColor = mTypedArray.getColor(R.styleable.RoundProgressView_srp_txtColor, Color.BLACK);
        txtSize = mTypedArray.getDimension(R.styleable.RoundProgressView_srp_txtSize, 18);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX = getWidth() / 2;// 获取圆心的x坐标
        int radius = (int) (centerX - roundWidth / 2); // 圆环的半径

        //step1 画最外层的大圆环
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundColor);
        paint.setAlpha(40);//设置透明度
        paint.setAntiAlias(true); // 消除锯齿
        // 设置画笔样式
        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                break;
            case FILL:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                break;
        }
        canvas.drawCircle(centerX, centerX, radius, paint); // 画出圆环

        //step2 画圆弧-画圆弧的进度
        paint.setStrokeWidth(progressWidth);//设置画笔的宽度使用进度条的宽度
        paint.setStrokeCap(Paint.Cap.ROUND);//设置线条是圆角
        paint.setDither(true);//设置是否抖动
        paint.setColor(progressColor);
        paint.setAlpha(255);//设置透明度
        RectF oval = new RectF(centerX - radius, centerX - radius, centerX + radius, centerX + radius); // 用于定义的圆弧的形状和大小的界限
        int sweepAngle = 360 * progress / max; // 计算进度值在圆环所占的角度
        // 根据进度画圆弧
        switch (style) {
            case STROKE:
                // 空心
                canvas.drawArc(oval, startAngle, sweepAngle, false, paint);
                break;
            case FILL:
                // 实心
                canvas.drawArc(oval, startAngle, sweepAngle, true, paint);
                break;
        }

        //step 3 画文字
        paintText.setAntiAlias(true);
        paintText.setColor(txtColor);
        paintText.setTextSize(txtSize);
        textVal = progress + "%";
        Rect boundText = new Rect();
        paintText.getTextBounds(textVal, 0, textVal.length(), boundText);
        canvas.drawText(textVal, (getWidth() - boundText.width()) / 2, (getHeight() + boundText.height()) / 2, paintText);

    }

    /**
     * 设置进度的最大值
     * <p>根据需要，最大值一般设置为100，也可以设置为1000、10000等</p>
     *
     * @param max int最大值
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度
     *
     * @return int 当前进度值
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件
     *
     * @param progress 进度值
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        this.progress = progress;
        // 刷新界面调用postInvalidate()能在非UI线程刷新
        postInvalidate();
    }
}
