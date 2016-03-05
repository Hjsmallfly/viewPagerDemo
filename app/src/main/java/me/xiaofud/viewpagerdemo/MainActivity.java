package me.xiaofud.viewpagerdemo;

//https://www.bignerdranch.com/blog/viewpager-without-fragments/

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CustomPagerAdapter adapter;
    private UIHandler uiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new CustomPagerAdapter(this);
        viewPager.setAdapter(adapter);
        uiHandler = new UIHandler(viewPager);
        Button button = (Button) findViewById(R.id.swipe);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = new Timer("ui", true);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        int count = adapter.getCount();
                        int index = viewPager.getCurrentItem();
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("position", ++index % count);
                        message.setData(bundle);
                        MainActivity.this.uiHandler.sendMessage(message);
                    }
                };
                // delay 是多少时间以后才......开始运行,period 是周期
                timer.scheduleAtFixedRate(task, 0, 2000);
//                task	TimerTask: the task to schedule.
//                delay	long: amount of time in milliseconds before first execution.
//                period	long: amount of time in milliseconds between subsequent executions.
            }
        });
    }


    private static class UIHandler extends Handler{

        private ViewPager viewPager;

        public UIHandler(ViewPager pager){
            this.viewPager = pager;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            int position = bundle.getInt("position");
            viewPager.setCurrentItem(position);
        }
    }
}
