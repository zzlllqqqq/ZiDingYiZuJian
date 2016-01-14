package com.atguigu.zidingyizujian;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    private RelativeLayout rl_main_level1;
    private RelativeLayout rl_main_level2;
    private RelativeLayout rl_main_level3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rl_main_level1 = (RelativeLayout) findViewById(R.id.rl_main_level1);
        rl_main_level2 = (RelativeLayout) findViewById(R.id.rl_main_level2);
        rl_main_level3 = (RelativeLayout) findViewById(R.id.rl_main_level3);

        //保存状态
        rl_main_level1.setTag(true);
        rl_main_level2.setTag(true);
        rl_main_level3.setTag(true);
    }

    /**
     * 判断指定的菜单是不是打开的
     *
     * @param layout
     * @return
     */
    public boolean isOpen(RelativeLayout layout) {
        boolean isOpen = (boolean) layout.getTag();
        return isOpen;
    }

    /**
     * 使用属性动画切换菜单的状态
     * @param layout
     * @param delayTime
     */
    private void switchState(RelativeLayout layout, long delayTime) {
        //创建并设置动画
        float fromDrgess = 0;
        float toDrgess = 0;
        //得到当前状态
        boolean isOpen = (boolean) layout.getTag();
        //打开——>关闭
        if(isOpen) {
            fromDrgess = 0;
            toDrgess = 180;
        } else {//关闭——>打开
            fromDrgess = 180;
            toDrgess = 360;
        }
        //设置旋转中心点
        layout.setPivotX(layout.getWidth()/2);
        layout.setPivotY(layout.getHeight());
        //保存当前状态
        layout.setTag(!isOpen);
        //属性动画特点：焦点会跟随图片移动
        //layout.setRotation();
        //这里的第二个参数是根据以上的方法得来的
        ObjectAnimator animator = ObjectAnimator.ofFloat(layout, "rotation", fromDrgess, toDrgess);
        animator.setDuration(500);
        //延迟开始时间
        animator.setStartDelay(delayTime);
        //开始动画
        animator.start();
    }

    /**
     * 切换菜单布局的状态(打开/关闭)
     * 使用一般的animation实现
     * @param menuLayout
     * @param delayTime
     */
    private void switchState2(RelativeLayout menuLayout, long delayTime) {
        //创建并设置动画
        float fromDegrees = 0;
        float toDegrees = 0;
        //得到当前的状态
        boolean isOpen = (boolean) menuLayout.getTag();
        if (isOpen) {//-->关闭
            fromDegrees = 0;
            toDegrees = 180;
        } else {
            fromDegrees = 180;
            toDegrees = 360;
        }
        //保存新的状态
        menuLayout.setTag(!isOpen);

        RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1);
        animation.setDuration(500);
        animation.setFillAfter(true);
        //开始延迟时间
        animation.setStartOffset(delayTime);

        //启动动画
        menuLayout.startAnimation(animation);

        //设置menuLayout的子view的可点击性，如果不设置该项那么即使图片消失点击图片消失前的位置依然有之前的响应
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            View childView = menuLayout.getChildAt(i);
            childView.setClickable(!isOpen);
        }
    }

    // 切换level3的状态
    public void clickMenu(View v) {
        switchState(rl_main_level3, 0);
    }

    /*
    * 切换level2的状态
    * 如果level3是打开的, 延迟300切换其状态
    */
    public void clickHome(View v) {
        switchState(rl_main_level2, 0);
        if (isOpen(rl_main_level3)) {
            switchState(rl_main_level3, 300);
        }
    }

    /*
    * 切换level1的状态
    * 如果level1是打开的, level2是关闭的, level2不切换, 其它情况都延迟300ms切换
    * 如果level3是打开的, 延迟600ms切换
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            switchState(rl_main_level1, 0);
            if (!(!isOpen(rl_main_level1) && !isOpen(rl_main_level2))) {
            //if (isOpen(rl_main_level1) || isOpen(rl_main_level2)) {
            }
            switchState(rl_main_level2, 300);
            if (isOpen(rl_main_level3)) {
                switchState(rl_main_level3, 600);
            }
        }
        return true;
    }
}
