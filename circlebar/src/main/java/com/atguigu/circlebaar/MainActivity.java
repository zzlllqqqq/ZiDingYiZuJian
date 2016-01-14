package com.atguigu.circlebaar;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class MainActivity extends Activity {

    private CircleBar cb_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cb_main = (CircleBar) findViewById(R.id.cb_main);
    }

    private boolean isDownLonding = false;

    public void startdownload(View v) {
        if (isDownLonding) {
            return;
        }
        new Thread() {
            public void run() {
                isDownLonding = true;
                cb_main.setProgress(0);
                cb_main.setMax(100);
                for (int i = 0; i < 100; i++) {
                    SystemClock.sleep(60);
                    cb_main.setProgress(cb_main.getProgress() + 1);
                }
                isDownLonding = false;
            }
        }.start();
    }
}
