package me.xiaofud.viewpagerdemo.activities;

//https://www.bignerdranch.com/blog/viewpager-without-fragments/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
                        int count = customPagerAdapter.getCount();
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
            if (fileList == null)
                fileList = new ArrayList<>();
            fileList.add(file);

            // --------debug---------

//            ImageView imageView = (ImageView) findViewById(R.id.photo);
//            Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
////            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
////            Log.d("bannerPagerAdapter", "picture width =" + drawable.getIntrinsicWidth() + " height: " + drawable.getIntrinsicHeight());
//            DisplayMetrics displaymetrics = new DisplayMetrics();
//            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//            windowManager.getDefaultDisplay().getMetrics(displaymetrics);
////            int height = displaymetrics.heightPixels;
//            int width = displaymetrics.widthPixels;
//
//            float photo_ratio = (float) bitmap.getWidth() / bitmap.getHeight();
//            int new_width = width;
//            int new_height = (int) (width / photo_ratio);
//
//            bitmap = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);
//
//            imageView.setImageBitmap(bitmap);

            // --------debug---------

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
