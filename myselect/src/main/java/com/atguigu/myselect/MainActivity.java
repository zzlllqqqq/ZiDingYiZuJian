package com.atguigu.myselect;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private EditText et_main_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_main_name = (EditText)findViewById(R.id.et_main_name);
    }

    private Myadapter adapter;
    private PopupWindow pw;
    private List<String> data = new ArrayList<>();
    private ListView listView;
    //图片添加点击响应
    public void clickArrow(View v) {
        //弹出窗口只用加载一次popupWindow
        if(pw == null) {
            pw = new PopupWindow(this);
            //往data中添加数据
            initListView();
            //popupWindow中添加数据
            pw.setContentView(listView);
            //设置弹出窗口的宽高
            pw.setWidth(et_main_name.getWidth());
            //200dp——>？？px
            pw.setHeight(DensityUtils.dip2px(this, 200));
            //设置焦点否则点击弹出窗口以外的区域不能使其消失、并且点击弹出数据不能将数据带回在EditText中显示
            pw.setFocusable(true);
        }
        //弹出窗挂载在EditText下显示
        pw.showAsDropDown(et_main_name);
    }

    private void initListView() {
        //给弹出窗口中添加数据
        listView = new ListView(this);
        //设置背景图片否则默认的为灰色背景
        listView.setBackgroundResource(R.drawable.listview_background);
        //添加item点击监听
        listView.setOnItemClickListener(this);
        for (int i = 0; i < 30; i++) {
            data.add("A1111111" + i);
        }
        adapter = new Myadapter();
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //在监听中实现给文本框赋值
        et_main_name.setText(data.get(position));
        //让弹出窗口消失
        pw.dismiss();
    }

    private class Myadapter extends BaseAdapter implements View.OnClickListener {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this, R.layout.item_main, null);
                holder.textView = (TextView) convertView.findViewById(R.id.tv_item_name);
                holder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.textView.setText(data.get(position));
            //将当前位置设置位标识保存在tag中
            holder.imageView.setTag(position);
            //给item中的imageView设置点击监听
            holder.imageView.setOnClickListener(this);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            //通过Vire对象将保存在其中的位置标识取出
            int position = (int) v.getTag();
            //根据标识删除
            data.remove(position);
            //更新列表  adapter可以省略
            adapter.notifyDataSetChanged();
        }

        class ViewHolder{
            TextView textView;
            ImageView imageView;
        }
    }
}
