package com.atguigu.cehuashanchu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by admin on 2016/1/14.
 */
public class SlideLayout extends FrameLayout {

    private View contentView, menuView;
    private Scroller scroller;
    private int contentWidth, menuWidth, viewHight;

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHight = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHight);
    }

    int lastX, lastY, downX, downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int evenX = (int) event.getRawX();
        int evenY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = lastX = evenX;
                downY = lastY = evenY;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = evenX - lastX;
                int scrollX = getScrollX() - dx;
                if(scrollX > menuWidth) {
                    scrollX = menuWidth;
                } else if(scrollX < 0) {
                    scrollX = 0;
                }
                scrollTo(scrollX, getScrollY());
                lastX = evenX;

                int totalDX = Math.abs(evenX - downX);
                int totalDY = Math.abs(evenY - downY);
                if(totalDX > totalDY && totalDX > 8) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
            case MotionEvent.ACTION_UP:
                int totalX = getScrollX();
                if(totalX < menuWidth/2) {
                    close();
                } else {
                    open();
                }
                break;
        }
        return true;
    }

    public void open() {
        scroller.startScroll(getScrollX(), getScrollY(), menuWidth - getScrollX(), 0);
        invalidate();
        if(onStateChangeListener != null) {
            onStateChangeListener.onOpen(this);
        }
    }

    public void close() {
        scroller.startScroll(getScrollX(), getScrollY(), -getScrollX(), -getScrollY());
        invalidate();
        if(onStateChangeListener != null) {
            onStateChangeListener.onClose(this);
        }
    }

    @Override
    public void computeScroll() {
        if(scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = false;
        int eventX = (int) ev.getRawX();
        int eventY = (int) ev.getRawY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = downX = eventX;
                lastY = downY = eventY;
                if(onStateChangeListener != null) {
                    onStateChangeListener.onDown(this);
                }
            break;
            case MotionEvent.ACTION_MOVE:
                int X = Math.abs(eventX - downY);
                if(X > 8) {
                    intercept = true;
                }
                break;
        }
        return intercept;
    }

    private OnStateChangeListener onStateChangeListener;

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public interface OnStateChangeListener{
        public void onOpen(SlideLayout layout);

        public void onClose(SlideLayout layout);

        public void onDown(SlideLayout layout);
    }
}
