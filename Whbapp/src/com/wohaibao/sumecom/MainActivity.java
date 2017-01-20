package com.wohaibao.sumecom;

import java.util.ArrayList;
import java.util.List;

import com.wohaibao.sumecom.activity.GeneralBottomActivity;
import com.wohaibao.sumecom.adapter.BannerAdapter;
import com.wohaibao.sumecom.viewpager.WrapContentHeightViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends GeneralBottomActivity {
	// private String TAG = "mainActivity-->";
	private static String currentActivity = "index";

	// private ViewPager mViewPager;
	private WrapContentHeightViewPager mViewPager;
	private List<ImageView> mlist;
	private TextView mTextView;
	private LinearLayout mLinearLayout;
	//private Handler mHandler;
	//private int sleepTime = 3000;

	// 广告图素材
	private int[] bannerImages = { R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4 };
	// 广告语
	private String[] bannerTexts = { "因为专业 所以卓越", "坚持创新 行业领跑", "诚信 专业 双赢", "精细 和谐 大气 开放" };

	// ViewPager适配器与监听器
	private BannerAdapter mAdapter;
	private BannerListener bannerListener;
	private int timeSleep = 2000;

	// 圆圈标志位
	private int pointIndex = 0;
	// 线程标志
	public boolean isStop = false;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		super.initGeneralBottom(MainActivity.this, currentActivity);

        initView();
        initData();
        initAction();
 
        // 开启新线程，2秒一次更新Banner
        new Thread(new Runnable() {
 
            @Override
            public void run() {
                while (!isStop) {
                    SystemClock.sleep(timeSleep);
                    runOnUiThread(new Runnable() {
 
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                }
            }
        }).start();

	}
	
	@Override
	protected void onStop() {
		//mHandler.removeCallbacksAndMessages(null);
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// 关闭定时器
		isStop = true;
		//mHandler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}


	/**
	 * 初始化事件
	 */
	private void initAction() {
		bannerListener = new BannerListener();
		mViewPager.setOnPageChangeListener(bannerListener);
		// 取中间数来作为起始位置
		int index = (Integer.MAX_VALUE / 2) - (Integer.MAX_VALUE / 2 % mlist.size());
		// 用来出发监听器
		mViewPager.setCurrentItem(index);
		mLinearLayout.getChildAt(pointIndex).setEnabled(true);
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mlist = new ArrayList<ImageView>();
		View view;
		LayoutParams params;
		for (int i = 0; i < bannerImages.length; i++) {
			// 设置广告图
			ImageView imageView = new ImageView(MainActivity.this);
			imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setBackgroundResource(bannerImages[i]);
			mlist.add(imageView);
			// 设置圆圈点
			view = new View(MainActivity.this);
			params = new LayoutParams(5, 5);
			params.leftMargin = 10;
			view.setBackgroundResource(R.drawable.point_background);
			view.setLayoutParams(params);
			view.setEnabled(false);

			mLinearLayout.addView(view);
		}
		mAdapter = new BannerAdapter(mlist,this);
		mViewPager.setAdapter(mAdapter);
	}

	/**
	 * 初始化View操作
	 */
	private void initView() {
		mViewPager = (WrapContentHeightViewPager) findViewById(R.id.viewpager);
		mTextView = (TextView) findViewById(R.id.tv_bannertext);
		mLinearLayout = (LinearLayout) findViewById(R.id.points);
	}

	// 实现VierPager监听器接口
	class BannerListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			int newPosition = position % bannerImages.length;
			mTextView.setText(bannerTexts[newPosition]);
			mLinearLayout.getChildAt(newPosition).setEnabled(true);
			mLinearLayout.getChildAt(pointIndex).setEnabled(false);
			// 更新标志位
			pointIndex = newPosition;

		}
	}

}
