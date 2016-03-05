package me.xiaofud.viewpagerdemo.activities;

//https://www.bignerdranch.com/blog/viewpager-without-fragments/

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import helpers.DownloadTask;
import interfaces.FileDownloadedHandle;
import me.xiaofud.viewpagerdemo.R;

public class MainActivity extends AppCompatActivity implements FileDownloadedHandle {

    //----------views------------

    private ViewPager viewPager;
    private EditText picture_url_edit;
    private Button auto_scroll_button;
    private Button download_button;

    //----------views------------

    private CustomPagerAdapter adapter;
    private UIHandler uiHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup_views();
    }

    private void setup_views(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        picture_url_edit = (EditText) findViewById(R.id.picture_url);
        adapter = new CustomPagerAdapter(this);
        viewPager.setAdapter(adapter);
        uiHandler = new UIHandler(viewPager);

        auto_scroll_button = (Button) findViewById(R.id.auto_scroll_button);

        auto_scroll_button.setOnClickListener(new View.OnClickListener() {
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

        download_button = (Button) findViewById(R.id.download_button);
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img_url = picture_url_edit.getText().toString();
                int index = img_url.lastIndexOf("/");
                String filename = img_url.substring(index);
                DownloadTask downloadTask = new DownloadTask(img_url, "download", filename, MainActivity.this, 4000);
                downloadTask.execute();
            }
        });

    }

    @Override
    public void handle_downloaded_file(File file) {
        if (file != null){
            Toast.makeText(MainActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(MainActivity.this, "文件下载失败,请查看日志", Toast.LENGTH_SHORT).show();
        }
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
