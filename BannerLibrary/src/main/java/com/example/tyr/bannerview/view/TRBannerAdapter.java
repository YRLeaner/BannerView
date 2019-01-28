package com.example.tyr.bannerview.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by tyr on 2019/1/27.
 */

public abstract class TRBannerAdapter{

    /**
     * 根据位置获取viewpager的子view
     * @param position
     * @return
     */
    public abstract View getView(int position,View converView);

    /**
     * 获取轮播数量
     * @return
     */
    public abstract int getCount();

    /**
     * 图片描述
     * @return
     */
    public abstract String getDesc(int position);

}
