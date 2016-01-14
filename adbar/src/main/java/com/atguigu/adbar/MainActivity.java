package com.atguigu.adbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int WHAT_NEXT_PAGE = 1;
    private ViewPager vp_main_imags;
    private TextView tv_main_message;
    private LinearLayout ll_main_point;
    private MyAdapter adapter;
    private List<ImageView> imageViewList = new ArrayList<>();
    private List<ImageView> pointViewList = new ArrayList<>();
    private int[] images = {R.drawable.a, R.drawable.b, R.drawable.c,
            R.drawable.d, R.drawable.e, R.drawable.f};
    private String[] imageMessages = {"尚硅谷在线课堂", "7月就业名单全部曝光",
            "抱歉, 没座了!", "尚硅谷拔河争霸赛", "任性!平均薪水11345元",
            "尚硅谷新学员做客CCTV"};
    private int currentPos = 0;
    private ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.OnPageChangeListener() {
                /**
                 *  当页面滚动时不断调用
                 * @param position  下标
                 * @param positionOffset 滚动的偏移量(比例)
                 * @param positionOffsetPixels 滚动的偏移量(像素)
                 */
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Log.e("TAG", "@@@@@onPageScrolled调用@@@@@");
                }

                //当一个页面被选中的时候
                //解决   翻页与文本及提示图标同步更新
                @Override
                public void onPageSelected(int position) {
                    Log.i("TAG", "#####onPageSelected调用#####");
                    //得到真实的角标
                    position = position % images.length;
                    //更新文本
                    tv_main_message.setText(imageMessages[position]);
                    //更新圆点
                    //currentPos可以理解为滚动前那一页的角标为取模之后角标
                    //将其设置为默认圆点的灰色
                    pointViewList.get(currentPos).setEnabled(true);
                    //position为滚动后那一页即当前页角标真实角标，圆点变为白色
                    pointViewList.get(position).setEnabled(false);
                    currentPos = position;
                }

                /**
                 * 当状态改变时调用
                 * @see ViewPager#SCROLL_STATE_IDLE 空闲(不动)
                 * @see ViewPager#SCROLL_STATE_DRAGGING 拖拽(时间点)
                 * @see ViewPager#SCROLL_STATE_SETTLING 安定下来
                 * @param state
                 * 解决手动翻页与自动翻页的冲突
                 */
                //用来标识是否被拖拽过
                private boolean draged = false;
                @Override
                public void onPageScrollStateChanged(int state) {
                    Log.e("TAG", "$$$$$onPageScrollStateChanged调用$$$$$");
                    switch (state) {
                        //拖拽时
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            draged = true;
                            //移除所有消息，让自动滚动失效
                            handler.removeCallbacksAndMessages(null);
                            break;
                        //图片稳定下之前
                        case ViewPager.SCROLL_STATE_SETTLING:
                            if (draged) {
                                draged = false;
                                //发送消息继续实现自动滚动
                                handler.sendEmptyMessageDelayed(WHAT_NEXT_PAGE, 2500);
                            }
                            break;
                    }
                }
            };

    //实现定时自动滚屏
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                //实现翻页
                vp_main_imags.setCurrentItem(vp_main_imags.getCurrentItem() + 1, true);
                //实现定时
                handler.sendEmptyMessageDelayed(WHAT_NEXT_PAGE, 2500);
            }
        }
    };
    private ViewPager.OnPageChangeListener onPageChangeListener1 = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.e("TAG", "@@@@@onPageScrolled调用@@@@@" + position);
        }

        @Override
        public void onPageSelected(int position) {
            Log.i("TAG", "#####onPageSelected调用#####" + position + "######" + currentPos);
            position = position % images.length;
            tv_main_message.setText(imageMessages[position]);
            pointViewList.get(position).setEnabled(false);
            pointViewList.get(currentPos).setEnabled(true);
            currentPos = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            Log.e("TAG", "$$$$$onPageScrollStateChanged调用$$$$$");
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    handler.removeCallbacksAndMessages(null);
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vp_main_imags = (ViewPager) findViewById(R.id.vp_main_imags);
        tv_main_message = (TextView) findViewById(R.id.tv_main_message);
        ll_main_point = (LinearLayout) findViewById(R.id.ll_main_point);
        //给point的视图设置参数
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                //指定视图的宽高
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //指定边距  两边都指定为的是让整体处于父视图中间
        params.leftMargin = 5;
        params.rightMargin = 5;
        for (int i = 0; i < images.length; i++) {
            //分别向添加图片和圆点
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images[i]);
            //通过Tag传递参数
            imageView.setTag(i);
            imageViewList.add(imageView);
            //设置监听
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //通过Tag获取保存的数据
                    Toast.makeText(getApplicationContext(), "position=" + v.getTag(), Toast.LENGTH_SHORT).show();
                }
            });
            imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.point_selector);
            //第一个圆点为白色
            if (i == 0) {
                imageView.setEnabled(false);
            }
            pointViewList.add(imageView);
            ll_main_point.addView(imageView, params);
        }

        adapter = new MyAdapter();
        vp_main_imags.setAdapter(adapter);
        //初始定位在中间位置从而满足开始就可以左右划动
        //设置当前页的下标(默认0) : 如果这个值设置得太大, 就会出现向左滑动不正常的bug  800万---900万
        //vp_main_images.setCurrentItem(Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%images.length);
        vp_main_imags.setCurrentItem(images.length * 1000);
        //在该监听中实现 翻页与文本及提示图标同步更新
        vp_main_imags.addOnPageChangeListener(onPageChangeListener);
        //设置初始本文
        tv_main_message.setText(imageMessages[0]);
        //发送消息实现滚屏
        handler.sendEmptyMessageDelayed(WHAT_NEXT_PAGE, 2500);
    }

    //继承与PaperAdapter
    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            //返回显示的页数
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //该方法必须重写否则会出现异常  但是直接继承的时候并不现实此方法  需要手动添加
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //得到数据再集合中的下标
            position = position % images.length;
            ImageView imageView = imageViewList.get(position);
            //ViewPage中添加视图
            container.addView(imageView);
            return imageView;
        }

        //该方法必须重写否则会出现异常  但是直接继承的时候并不现实此方法  需要手动添加
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清除所有的监听
        vp_main_imags.clearOnPageChangeListeners();
    }

    public void cilckRun(View v) {
        handler.removeCallbacksAndMessages(null);
        vp_main_imags.clearOnPageChangeListeners();
        vp_main_imags.addOnPageChangeListener(onPageChangeListener);
        handler.sendEmptyMessageDelayed(WHAT_NEXT_PAGE, 2500);
    }

    public void clickStop(View v) {
        handler.removeCallbacksAndMessages(null);
        newListener();

    }

    public void clickNext(View v) {
        vp_main_imags.setCurrentItem(vp_main_imags.getCurrentItem() + 1, true);
        newListener();
    }

    public void clickBack(View v) {
        vp_main_imags.setCurrentItem(vp_main_imags.getCurrentItem() - 1, true);
        newListener();
    }

    private void newListener() {
        vp_main_imags.clearOnPageChangeListeners();
        vp_main_imags.addOnPageChangeListener(onPageChangeListener1);
    }
}
