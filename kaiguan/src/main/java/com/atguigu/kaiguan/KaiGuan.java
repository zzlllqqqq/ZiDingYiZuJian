package com.atguigu.kaiguan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by admin on 2016/1/15.
 */
public class KaiGuan extends View implements View.OnClickListener {

    private Bitmap bgBitmap;
    private Bitmap slideBitmap;
    private Paint paint;
    private float slideX = 0;//滑杆图片左上角的x坐标
    private float maxSlideX;

    public KaiGuan(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();
        paint.setAntiAlias(true);
        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slideBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        maxSlideX = bgBitmap.getWidth() - slideBitmap.getWidth();
        setOnClickListener(this);
    }

    //视图的宽高要与背景图片一致
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(bgBitmap.getWidth(), bgBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bgBitmap, 0, 0, paint);
        canvas.drawBitmap(slideBitmap, slideX, 0, paint);
    }

    private float downX, lastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //执行父类的方法, 能响应点击
        super.onTouchEvent(event);
        float eventX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickable = true;
                lastX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = eventX - lastX;
                //滑动滑杆
                slideX += dx;
                //限制slideX [0,maxSlideX]
                if (slideX < 0) {
                    slideX = 0;
                } else if (slideX > maxSlideX) {
                    slideX = maxSlideX;
                }
                invalidate();
                lastX = eventX;

                float totalDx = Math.abs(eventX - downX);
                if (totalDx > 8) {
                    clickable = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (slideX < maxSlideX / 2) {
                    close();
                    open = false;
                } else {
                    open();
                    open = true;
                }
                break;
        }

        return true;
    }

    private void close() {
        slideX = 0;
        invalidate();
    }

    private void open() {
        slideX = maxSlideX;
        invalidate();
    }

    private boolean clickable = true;
    private boolean open = false;

    @Override
    public void onClick(View v) {
        if (clickable) {
            if (open) {
                close();
            } else {
                open();
            }
        }
    }
}
