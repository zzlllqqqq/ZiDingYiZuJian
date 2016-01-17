package com.atguigu.myviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * Created by admin on 2016/1/15.
 */
public class MyViewPager extends ViewGroup {
    private int viewWidth, viewHeight;
    private Scroller scroller;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.layout(i * viewWidth, 0, (i + 1) * viewWidth, viewHeight);
        }
    }

    int lastX, downX;
    private int currentPos = 0;//当前页下标

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int eventX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = downX = eventX;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = eventX - lastX;
                int scrollX = getScrollX() - dx;
                scrollTo(scrollX, getScrollY());
                lastX = eventX;
                break;
            case MotionEvent.ACTION_UP:
                int totalX = eventX - downX;
                if (totalX < -viewWidth / 2) {
                    scrollToPage(currentPos + 1);
                } else if (totalX > viewWidth / 2) {
                    scrollToPage(currentPos - 1);
                } else {
                    scrollToPage(currentPos);
                }
                break;
        }
        return true;
    }

    /**
     * 平滑滚动指定的页
     *
     * @param position
     */
    public void scrollToPage(int position) {
        if (position < 0) {
            position = 0;
        } else if (position > getChildCount() - 1) {
            position = getChildCount() - 1;
        }
        if (currentPos != position) {
            currentPos = position;
            if (onPageChangeListener != null) {
                onPageChangeListener.onPageChange(position);
            }
        }
        //平滑滚动 --->position*viewWidth
        scroller.startScroll(getScrollX(), getScrollY(), position * viewWidth - getScrollX(), 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }

    private OnPageChangeListener onPageChangeListener;

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {
        public void onPageChange(int position);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        int evX = (int) ev.getRawX();
        boolean intercept = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastX = evX;
                break;
            case MotionEvent.ACTION_MOVE:
                int totalX = Math.abs(evX - downX);
                if(totalX > 8) {
                    intercept = true;
                }
        }
        return intercept;
    }
}
