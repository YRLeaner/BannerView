package com.example.tyr.sample;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tyr.bannerview.view.TRBannerAdapter;
import com.example.tyr.bannerview.view.TRBannerView;
import com.example.tyr.bannerview.view.TRBannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TRBannerViewPager.BannerItemClickListener {

    private TRBannerView trBannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trBannerView = (TRBannerView)findViewById(R.id.banner);


        final List<Integer> list = new ArrayList<>();
        list.add(R.drawable.link0);
        list.add(R.drawable.link);
        trBannerView.setAdapter(new TRBannerAdapter() {
            @Override
            public View getView(int position, View mConvertView) {
                ImageView bannerIv = null;
                if (mConvertView==null){
                    bannerIv = new ImageView(MainActivity.this);
                    bannerIv.setScaleType(ImageView.ScaleType.FIT_XY);
                }else {
                    bannerIv = (ImageView)mConvertView;
                }

                bannerIv.setImageBitmap(BitmapFactory.decodeResource(getResources(),list.get(position)));
                return bannerIv;
            }

            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public String getDesc(int position) {
                return position+" ";
            }
        });
        trBannerView.startRoll();
        trBannerView.setOnBannerItemClickListener(this);
    }

    @Override
    public void click(int position) {
        Toast.makeText(MainActivity.this,position+" dd",Toast.LENGTH_SHORT).show();
    }
}
