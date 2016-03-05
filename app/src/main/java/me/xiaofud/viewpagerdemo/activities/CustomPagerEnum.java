package me.xiaofud.viewpagerdemo.activities;

import me.xiaofud.viewpagerdemo.R;

/**
 * Created by smallfly on 16-3-5.
 */
public enum CustomPagerEnum {
    RED(R.string.red, R.layout.view_red),
    BLUE(R.string.blue, R.layout.view_blue);
//    ORANGE(R.string.orange, R.layout.view_orange);

    private int mTitleResId;
    private int mLayoutResId;

    CustomPagerEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
