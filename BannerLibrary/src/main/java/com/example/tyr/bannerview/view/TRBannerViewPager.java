package com.example.tyr.bannerview.view;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tyr on 2019/1/27.
 */

public class TRBannerViewPager extends ViewPager {
    private Context mContext;
    private TRBannerAdapter mAdapter;
    private BannerItemClickListener mLisenter;
    //页面自动切换速率设置
    BannerScroller bannerScroller ;
    //实现自动轮播
    private final int SCROLL_MSG = 0x0011;
    //滚动下一张间隔时间
    private int mCutDownTime = 3500;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setCurrentItem(getCurrentItem()+1);
            //不断循环执行
            startRoll();
        }
    };

    private List<View> mConvertViews;
    public TRBannerViewPager(Context context) {
        this(context, null);
    }

    public TRBannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        activity = (Activity) context;
        //通过反射改变viewpager自动滑动的时间
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            bannerScroller= new BannerScroller(context);
            field.setAccessible(true);
            field.set(this,bannerScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mConvertViews = new ArrayList<>();
    }


    public void setAdapter(TRBannerAdapter adapter) {
        this.mAdapter = adapter;
        setAdapter(new BannerPagerAdapter());
        ((Activity)getContext()).getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    /**
     * 设置页面切换时的持续时间
     */
    public void setPageDuration(int duration){
        bannerScroller.setmScrollerDuration(duration);
    }

    /**
     * 设置自动轮播
     */
    public void startRoll(){
        //清除消息
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG,mCutDownTime);
    }

    /**
     * 处理内存泄漏
     */
    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null;
        activity.getApplication().unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
        super.onDetachedFromWindow();
    }



    public void setBannerItemClickListener(BannerItemClickListener bannerItemClickListener) {
        this.mLisenter = bannerItemClickListener;
    }

    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // 返回一个很大的值，确保可以无限轮播
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            // 这么写就对了，看了源码应该就明白
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View bannerView = mAdapter.getView(position%mAdapter.getCount(),getConvertView());
            bannerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mLisenter!=null){
                        mLisenter.click(position%mAdapter.getCount());
                    }
                }
            });
            container.addView(bannerView);

            return bannerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 销毁回调的方法  移除页面即可
            container.removeView((View) object);
            //定期清理缓存中无用的View
            if (mConvertViews.size()>4){
                mConvertViews.clear();
            }
            mConvertViews.add((View) object);
        }
    }

    /**
     * 缓存存在问题
     * @return
     */
    private View getConvertView() {
        for (int i=0;i<mConvertViews.size();i++){
            if (mConvertViews.get(i).getParent()==null){
                return  mConvertViews.get(i);
            }
        }
        return null;
    }

    public interface BannerItemClickListener{
        public void click(int position);
    }

    //内存优化
    private Activity activity;
    //管理Activity生命周期
    Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new DefaultActivityLifeCycleCallbacks(){
        @Override
        public void onActivityResumed(Activity activity) {
            if (activity==getContext()){
                mHandler.sendEmptyMessageDelayed(mCutDownTime,SCROLL_MSG);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (activity==getContext()){
                mHandler.removeMessages(SCROLL_MSG);
            }
        }
    };
}
