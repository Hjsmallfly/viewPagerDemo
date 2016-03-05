package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import me.xiaofud.viewpagerdemo.R;

/**
 * Created by smallfly on 16-3-5.
 *
 */
public class BannerPagerAdapter extends PagerAdapter {

    private List<File> banners;
    private Context context;

    public BannerPagerAdapter(Context context, List<File> banners){
        this.context = context;
        this.banners = banners;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        File banner = banners.get(position);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.view_blue, container, false);
        ImageView imageView = (ImageView) viewGroup.findViewById(R.id.image2);
        Log.d("bannerPagerAdapter", banner.toString());
        Bitmap bitmap = BitmapFactory.decodeFile(banner.toString());
//        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
//        Drawable drawable = Drawable.createFromPath(banner.toString());
//        Log.d("bannerPagerAdapter", "picture width =" + drawable.getIntrinsicWidth() + " height: " + drawable.getIntrinsicHeight());
        // -----获取屏幕信息------
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
//        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;
        // -----获取屏幕信息------

        float photo_ratio = (float) bitmap.getWidth() / bitmap.getHeight();
        int new_width = width;
        int new_height = (int) (width / photo_ratio);

        bitmap = Bitmap.createScaledBitmap(bitmap, new_width, new_height, true);

        imageView.setImageBitmap(bitmap);


//        Log.d("bannerPagerAdapter", "screen width = " + width + " height = " + height);
        container.addView(viewGroup);
        return viewGroup;
    }

    @Override
    public int getCount() {
        return banners.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
