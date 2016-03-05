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
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.BannerPagerAdapter;
import adapters.CustomPagerAdapter;
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

    private CustomPagerAdapter customPagerAdapter;
    private BannerPagerAdapter bannerPagerAdapter;
    private UIHandler uiHandler;

    private List<File> fileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup_views();
    }

    private void setup_views(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        picture_url_edit = (EditText) findViewById(R.id.picture_url);
//        adapter = new CustomPagerAdapter(this);
//        viewPager.setAdapter(adapter);
        uiHandler = new UIHandler(viewPager);

        auto_scroll_button = (Button) findViewById(R.id.auto_scroll_button);

        auto_scroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Timer timer = new Timer("ui", true);
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        int count = bannerPagerAdapter.getCount();
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
//                String img_url = picture_url_edit.getText().toString();
                String[] urls = picture_url_edit.getText().toString().split(";");
                List<String> url_list = new ArrayList<String>();
                List<String> filenames = new ArrayList<String>();
                for(int i = 0 ; i < urls.length ; ++i){
                    url_list.add(urls[i]);
                    int index = urls[i].lastIndexOf("/");
                    String filename = urls[i].substring(index);
                    filenames.add(filename);

                }
                DownloadTask downloadTask =
                        new DownloadTask(url_list, "download", filenames, MainActivity.this, 4000);
                downloadTask.execute();
            }
        });

    }

    @Override
    public void handle_downloaded_file(List<File> file) {
        if (file != null){
            if (fileList == null)
                fileList = new ArrayList<>();
            fileList.addAll(file);

            if (bannerPagerAdapter == null){
                bannerPagerAdapter = new BannerPagerAdapter(this, fileList);
                if (viewPager.getAdapter() == null)
                    viewPager.setAdapter(bannerPagerAdapter);
                else
                    bannerPagerAdapter.notifyDataSetChanged();
            }else{
                bannerPagerAdapter.notifyDataSetChanged();
            }
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
