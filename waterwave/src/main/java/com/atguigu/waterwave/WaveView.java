package com.atguigu.waterwave;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by admin on 2016/1/15.
 */
public class WaveView extends View {

    private Circle circle;
    private int[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK};
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //更新circle
                Paint paint = circle.getPaint();
                //根据透明度判断如何更新
                int alpha = paint.getAlpha();
                if (alpha == 0) {
                    circle = null;
                    return;
                }
                //跟新内部数据
                circle.setRadius(circle.getRadius() + 3);
                paint.setStrokeWidth(paint.getStrokeWidth() + 2);
                paint.setAlpha(alpha - 5);
                invalidate();
                //使用Handler发延迟消息重绘 实现循环
                handler.sendEmptyMessageDelayed(1, 50);
            }
        }
    };

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            //case MotionEvent.ACTION_MOVE:
                //显示圆环
                showCircle(event);
                break;
        }
        return true;
    }

    /**
     * 显示圆环
     *
     * @param event
     */
    private void showCircle(MotionEvent event) {
        if (circle != null) {
            return;
        }
        //创建圆环对象
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setAlpha(255);
//        paint.setStrokeWidth(2);
//        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(colors[new Random().nextInt(colors.length)]);
        //Math.random()*colors.length
        circle = new Circle(event.getX(), event.getY(), 10, paint);
        invalidate();

        //使用Handler发延迟消息重绘
        handler.sendEmptyMessageDelayed(1, 50);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (circle != null) {
            canvas.drawCircle(circle.getX(), circle.getY(), circle.getRadius(), circle.getPaint());
        }
    }
}
