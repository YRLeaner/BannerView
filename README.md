#TR BannerView

##如何引用：
###Add it in your root build.gradle at the end of repositories:
  allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
  Step 2. Add the dependency
  dependencies {
	        implementation 'com.github.sidan26:BannerView:Tag'
	}
 
###在xml中应用
  <com.example.tyr.bannerview.view.TRBannerView
        android:id="@+id/banner"
        android:layout_width="match_parent"
        <!--小圆点大小-->
        app:dotSize="8dp"
         <!--小圆点距离-->
        app:dotDistance="10dp"
         <!--小圆点位置-->
        app:dotGravity="center"
         <!--底边栏颜色-->
        app:bottomColor="#80000000"
         <!--小圆点选中颜色-->
        app:dotIndicatorFocus="@android:color/holo_blue_dark"
         <!--小圆点未选中颜色-->
        app:dotIndicatorNormal="@android:color/white"
        android:layout_height="140dp">
    </com.example.tyr.bannerview.view.TRBannerView

###在java代码中：
   //传入匿名的TRBannerAdapter
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
        //设置banner自动滚动
        trBannerView.startRoll();
        //设置每一页的点击事件
        trBannerView.setOnBannerItemClickListener(this);
