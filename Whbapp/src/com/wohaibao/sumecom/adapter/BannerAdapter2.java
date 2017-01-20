package com.wohaibao.sumecom.adapter;

import java.util.List;

import com.wohaibao.sumecom.MainActivity;
import com.wohaibao.sumecom.activity.ProductdetailActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * ViewPager 适配器
 * 
 * @author william
 *
 */
public class BannerAdapter2 extends PagerAdapter {
	// 数据源
	private List<ImageView> mList;
	// private Context context;
	private MainActivity act;
	private long downTime = 0 ;
	private long upTime = 0;
	
	public BannerAdapter2(List<ImageView> list, Activity act) {
		this.mList = list;
		// this.context = context;
		this.act = (MainActivity) act;
	}

	@Override
	public int getCount() {
		// 取超大的数，实现无线循环效果
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == obj;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position = position % mList.size();
		View view = mList.get(position);
		container.addView(view);
		
		final int positionTemp = position;
		view.setOnTouchListener(new View.OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int actionValue = event.getAction();
				act.isStop = true;
				
				if(actionValue == MotionEvent.ACTION_DOWN){
					downTime = System.currentTimeMillis();
					Log.i("-----------BannerAdapter-->", "MotionEvent is ACTION_DOWN" );
					Log.i("-----------BannerAdapter-->", "position is " + positionTemp);
					Log.i("-----------BannerAdapter-->", "downTime " + downTime);

				} else if(actionValue==MotionEvent.ACTION_UP){
					upTime = System.currentTimeMillis() ;
					Log.i("-----------BannerAdapter-->", "upTime " + upTime);
					Log.i("-----------BannerAdapter-->", "downTime " + downTime);

					Log.i("-----------BannerAdapter-->", "upTime - downTime " + (upTime-downTime));
					if(upTime-downTime<200){
						Intent intent = new Intent(act, ProductdetailActivity.class);
						intent.putExtra("goodsno", positionTemp + "");
						act.startActivity(intent);
					}

				} else if(actionValue==MotionEvent.ACTION_MOVE){
					Log.i("-----------BannerAdapter-->", "MotionEvent is ACTION_MOVE  position is : " + positionTemp);

				}
				return true;
			}
		});

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(mList.get(position % mList.size()));
	}

}
