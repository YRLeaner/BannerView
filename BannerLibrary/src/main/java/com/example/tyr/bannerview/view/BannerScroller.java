package com.example.tyr.bannerview.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by tyr on 2019/1/27.
 */

/**
 * 用于修改页面切换时间
 */
public class BannerScroller extends Scroller {

    //动画持续时间
    private int mScrollerDuration = 850;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuration);
    }

    public void setmScrollerDuration(int mScrollerDuration) {
        this.mScrollerDuration = mScrollerDuration;
    }
}
