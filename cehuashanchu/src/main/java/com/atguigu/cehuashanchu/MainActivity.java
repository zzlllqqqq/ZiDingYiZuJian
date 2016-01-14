package com.atguigu.cehuashanchu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.base.CommonBaseAdapter;
import com.example.adapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ListView lv_main;
    private List<Mybean> data = new ArrayList<>();
    private SlideLayout openLayout;
    private CommonBaseAdapter<Mybean> adapter;
    private SlideLayout.OnStateChangeListener onStateChangeListener = new SlideLayout.OnStateChangeListener() {
        @Override
        public void onOpen(SlideLayout layout) {
            openLayout = layout;
        }

        @Override
        public void onClose(SlideLayout layout) {
            if(layout == openLayout) {
                openLayout = null;
            }
        }

        @Override
        public void onDown(SlideLayout layout) {
            if(openLayout != null && layout != openLayout) {
                openLayout.close();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_main = (ListView)findViewById(R.id.lv_main);
        initdata();
        adapter = new CommonBaseAdapter<Mybean>(MainActivity.this, data, R.layout.item_main) {
            @Override
            public void convert(ViewHolder holder, final int position) {
                final Mybean mybean = data.get(position);
                holder.setText(R.id.tv_item_content, mybean.content);
                holder.setOnclickListener(R.id.tv_item_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, mybean.content, Toast.LENGTH_SHORT).show();
                    }
                });
                holder.setOnclickListener(R.id.tv_item_menu, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                });
                SlideLayout slideLayout = (SlideLayout) holder.getConvertView();
                slideLayout.setOnStateChangeListener(onStateChangeListener);
            }
        };
        lv_main.setAdapter(adapter);
    }

    private void initdata() {
        for (int i = 0; i < 30; i++) {
            data.add(new Mybean("content" + i));
        }
    }
}
