package com.atguigu.myviewpager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

    private RadioGroup rg_main_index;
    private MyViewPager mvp_main_images;
    private int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3,
            R.drawable.a4, R.drawable.a5, R.drawable.a6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rg_main_index = (RadioGroup) findViewById(R.id.rg_main_index);
        mvp_main_images = (MyViewPager) findViewById(R.id.mvp_main_images);

        for (int i = 0; i < images.length; i++) {
            RadioButton radioButton = new RadioButton(this);
            //将下标设置为id
            radioButton.setId(i);
            if (i == 0) {
                radioButton.setChecked(true);
            }
            rg_main_index.addView(radioButton);

            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(images[i]);
            mvp_main_images.addView(imageView);
        }

        RadioButton radioButton = new RadioButton(this);
        radioButton.setId(images.length);
        rg_main_index.addView(radioButton);
        View view = View.inflate(this, R.layout.test, null);
        mvp_main_images.addView(view);

        rg_main_index.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mvp_main_images.scrollToPage(checkedId);
            }
        });

        mvp_main_images.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
            @Override
            public void onPageChange(int position) {
                rg_main_index.check(position);
            }
        });
    }
}
