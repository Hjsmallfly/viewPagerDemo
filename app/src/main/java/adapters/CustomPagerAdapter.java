package adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.xiaofud.viewpagerdemo.activities.CustomPagerEnum;

/**
 * Created by smallfly on 16-3-5.
 *
 */
public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }


    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
//        Log.d("viewPagerAdapter", "instantiateItem");
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(customPagerEnum.getLayoutResId(), collection, false);
//        ImageView img_view = new ImageView(mContext);
//        img_view.setImageResource(R.drawable.ic_launcher);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "position: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(mContext, "touched me @" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
//        layout.addView(img_view);
        collection.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    // instantiateItem()方法会调用该方法确定数量
    @Override
    public int getCount() {
//        Log.d("viewPagerAdapter", "getCount()");

        return CustomPagerEnum.values().length;
//        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CustomPagerEnum customPagerEnum = CustomPagerEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

}
