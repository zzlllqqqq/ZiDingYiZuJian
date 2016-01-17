package com.atguigu.kuaisusuoyin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by admin on 2016/1/14.
 */
public class QuickIndexView extends View {

    private Paint paint;
    private int viewWidth, ViewHeight;
    private int currentIndex = -1;
    private String[] words = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    public QuickIndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setTextSize(30);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        ViewHeight = getMeasuredHeight()/26;
        viewWidth = getMeasuredWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if(currentIndex == i) {
                paint.setTextSize(40);
                paint.setColor(Color.GRAY);
            } else {
                paint.setTextSize(30);
                paint.setColor(Color.WHITE);
            }
            Rect bounds = new Rect();
            paint.getTextBounds(word, 0, word.length(), bounds);
            int width = bounds.width();
            int height = bounds.width();
            float wordX = viewWidth/2 - width/2;
            float wordY = ViewHeight/2 + height/2 + ViewHeight*i;
            canvas.drawText(word, wordX, wordY, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int index = (int) (event.getY()/ViewHeight);
                if(currentIndex != index) {
                    currentIndex = index;
                    invalidate();
                    if(onIndexChangeListener != null) {
                        onIndexChangeListener.onIndexChange(words[index]);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                currentIndex = -1;
                invalidate();
                if(onIndexChangeListener!=null) {
                    onIndexChangeListener.onUp();
                }
                break;
        }
        return true;
    }

    private OnIndexChangeListener onIndexChangeListener;

    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }

    public interface OnIndexChangeListener{
        public void onIndexChange(String word);
        public void onUp();
    }
}
