package com.atguigu.circlebaar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2016/1/14.
 */
public class CircleBar extends View {
    private int progress; //当前进度
    private int max = 100; //最大进度
    private int roundColor = Color.BLUE; //圆环的颜色
    private int roundProgressColor = Color.RED; //圆环进度的颜色
    private float roundWidth = 10; //圆环的宽度
    private float textSize = 20; //文字的大小
    private int textColor = Color.BLACK; //文字的颜色

    private Paint paint;
    private float viewWidth;

    public CircleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float radius = viewWidth / 2 - roundWidth / 2;
        paint.setColor(roundColor);
        paint.setStrokeWidth(roundWidth);//控制进度环宽度
        //paint.setStyle(Paint.Style.STROKE);//控制是否是空心
        canvas.drawCircle(viewWidth / 2, viewWidth / 2, radius, paint);

        RectF rectf = new RectF(roundWidth / 2, roundWidth / 2, viewWidth - roundWidth / 2, viewWidth - roundWidth / 2);
        paint.setColor(roundProgressColor);
        canvas.drawArc(rectf, 0, 360 * progress / max, true, paint);//扇形两边是否存在
        //canvas.drawArc(rectf, 0, 360 * progress / max, false, paint);

        String text = progress * 100 / max + "%";
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(0);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        float textX = viewWidth / 2 - bounds.width() / 2;
        float textY = viewWidth / 2 + bounds.height() / 2;
        canvas.drawText(text, textX, textY, paint);
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
