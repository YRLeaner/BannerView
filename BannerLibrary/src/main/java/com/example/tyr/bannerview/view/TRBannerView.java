package com.example.tyr.bannerview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.tyr.bannerview.R;


/**
 * Created by tyr on 2019/1/27.
 */

public class TRBannerView extends RelativeLayout{
    //自定义viewpager
    private TRBannerViewPager trBannerViewPager;
    private TRBannerAdapter trBannerAdapter;
    //轮播描述
    private TextView trBannerText;
    //自定义Bannerview圆点容器
    private LinearLayout mDotContainerView;
    private RelativeLayout mBottomContainer;
    private Context context;

    //当前位置
    private int mCurrentPosition = 0;


    //初始属性
    private int mDotGravity = 1;
    private Drawable mIndicatiorFoucusDrawable;
    private Drawable mIndicatiorNotFoucusDrawable;
    private int mDotSize = 8;
    private int mDotDistance = 8;
    private int mBottomColor = Color.TRANSPARENT;
    //宽高比例
    private float widthProportion = 0;
    private float heightProportion = 0;
    public TRBannerView(Context context) {
        this(context,null);
    }

    public TRBannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TRBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.tr_ui_banner,this);
        this.context = context;
        initAttribute(attrs);
        initView();



    }


    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.TRBannerView);
        mDotGravity = typedArray.getInt(R.styleable.TRBannerView_dotGravity,mDotGravity);
        mIndicatiorFoucusDrawable = typedArray.getDrawable(R.styleable.TRBannerView_dotIndicatorFocus);
        if (mIndicatiorFoucusDrawable==null){
            mIndicatiorFoucusDrawable = new ColorDrawable(Color.RED);
        }
        mIndicatiorNotFoucusDrawable = typedArray.getDrawable(R.styleable.TRBannerView_dotIndicatorNormal);
        if (mIndicatiorNotFoucusDrawable==null){
            mIndicatiorNotFoucusDrawable = new ColorDrawable(Color.WHITE);
        }
        mDotSize = (int) typedArray.getDimension(R.styleable.TRBannerView_dotSize,dip2px(mDotSize));
        mDotDistance = (int) typedArray.getDimension(R.styleable.TRBannerView_dotDistance,dip2px(mDotDistance));
        mBottomColor = typedArray.getColor(R.styleable.TRBannerView_bottomColor,mBottomColor);
        widthProportion = typedArray.getFloat(R.styleable.TRBannerView_widthproportion,widthProportion);
        heightProportion = typedArray.getFloat(R.styleable.TRBannerView_heightproportion,heightProportion);

        typedArray.recycle();
    }

    private void initView() {
        trBannerViewPager = (TRBannerViewPager)findViewById(R.id.tr_banner);
        trBannerViewPager.setOffscreenPageLimit(2);
        trBannerText = (TextView)findViewById(R.id.banner_text);
        mDotContainerView = (LinearLayout)findViewById(R.id.dot_container);
        mBottomContainer = (RelativeLayout) findViewById(R.id.bottom_container);
        mBottomContainer.setBackgroundColor(mBottomColor);
    }


    public void setAdapter(TRBannerAdapter adaper){
        this.trBannerAdapter = adaper;
        trBannerViewPager.setAdapter(adaper);
        //初始化点的指示器
        initDotIndicator();
        trBannerViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                //监听当前选中的位置
                pageSelect(position);
            }
        });

        String firstDesc = trBannerAdapter.getDesc(0);
        trBannerText.setText(firstDesc);

      /* int width  = getWidth();
        int height  = getMeasuredHeight();
        if (height==0){
            if (heightProportion==0||widthProportion==0){
                return;
            }
            height = (int)(width*heightProportion/widthProportion);
            getLayoutParams().height =height;
        }*/
    }

    /**
     * 页面切换的回调
     * @param position
     */
    private void pageSelect(int position) {
        DotIndicatiorView oldDotIndicatiorView =
                (DotIndicatiorView) mDotContainerView.getChildAt(mCurrentPosition);
        oldDotIndicatiorView.setDrawable(mIndicatiorNotFoucusDrawable);
        mCurrentPosition = position%trBannerAdapter.getCount();

        DotIndicatiorView currentDotIndicatiorView =
                (DotIndicatiorView) mDotContainerView.getChildAt(mCurrentPosition);
        currentDotIndicatiorView.setDrawable(mIndicatiorFoucusDrawable);

        String bannerDesc = trBannerAdapter.getDesc(mCurrentPosition);
        trBannerText.setText(bannerDesc);



    }

    private void initDotIndicator() {
        int count = trBannerAdapter.getCount();
        mDotContainerView.setGravity(getDotGravity());
        for (int i=0;i<count;i++){
            DotIndicatiorView dotIndicatiorView = new DotIndicatiorView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize,mDotSize);
            params.leftMargin = mDotDistance;
            dotIndicatiorView.setLayoutParams(params);
            if (i==0){
                dotIndicatiorView.setDrawable(mIndicatiorFoucusDrawable);
            }else {
                dotIndicatiorView.setDrawable(mIndicatiorNotFoucusDrawable);
            }
            mDotContainerView.addView(dotIndicatiorView);
        }
    }

    private int getDotGravity() {
        switch (mDotGravity){
            case 0:
                return Gravity.CENTER;
            case -1:
                return Gravity.LEFT;
            case 1:
                return Gravity.RIGHT;
            default:
                try {
                    throw new Exception("不存在的Gravity");
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return 0;
    }

    private int dip2px(int i) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,i,getResources().getDisplayMetrics());
    }

    public void startRoll(){
        trBannerViewPager.startRoll();
    }


    public void setOnBannerItemClickListener(TRBannerViewPager.BannerItemClickListener bannerItemClickListener) {
        trBannerViewPager.setBannerItemClickListener(bannerItemClickListener);
    }
}
