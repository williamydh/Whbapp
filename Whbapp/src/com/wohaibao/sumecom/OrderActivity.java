package com.wohaibao.sumecom;

import com.wohaibao.sumecom.util.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class OrderActivity extends Activity {
	/** Called when the activity is first created. */
	String imageUrl = "http://images.csdn.net/20130609/zhuanti.jpg";
	private ImageView imgview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		imgview = (ImageView) findViewById(R.id.item_productImageView);
		//new NormalLoadPictrue().getPicture(imageUrl, imgview);
		ImageLoader imageLoader = new ImageLoader(OrderActivity.this, R.drawable.cart);
		imageLoader.getAndSetImage(imageUrl, imgview);
	}

}
