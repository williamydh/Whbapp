package com.wohaibao.sumecom.activity;

import com.wohaibao.sumecom.MainActivity;
import com.wohaibao.sumecom.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class GeneralBottomActivity extends Activity {
	private ImageButton indexImageButton;
	private ImageButton mallImageButton;
	private ImageButton cartImageButton;
	private ImageButton centerImageButton;
	private TextView indexTexView;
	private TextView mallTexView;
	private TextView cartTexView;
	private TextView centerTexView;

	public void initGeneralBottom(final Context context, String currentActivity) {
		indexTexView = (TextView) findViewById(R.id.general_bottom_index_textView);
		mallTexView = (TextView) findViewById(R.id.general_bottom_mall_textView);
		cartTexView = (TextView) findViewById(R.id.general_bottom_cart_textView);
		centerTexView = (TextView) findViewById(R.id.general_bottom_center_textView);

		indexImageButton = (ImageButton) findViewById(R.id.general_bottom_index_imageButton);
		mallImageButton = (ImageButton) findViewById(R.id.general_bottom_mall_imageButton);
		cartImageButton = (ImageButton) findViewById(R.id.general_bottom_cart_imageButton);
		centerImageButton = (ImageButton) findViewById(R.id.general_bottom_center_imageButton);

		if (currentActivity.equals("index")) {
			indexImageButton.setImageResource(R.drawable.index);
			indexTexView.setTextColor(this.getResources().getColor(R.drawable.general_bottom_selected));
		} else if (currentActivity.equals("mall")) {
			mallImageButton.setImageResource(R.drawable.mall);
			mallTexView.setTextColor(this.getResources().getColor(R.drawable.general_bottom_selected));
		} else if (currentActivity.equals("cart")) {
			cartImageButton.setImageResource(R.drawable.cart);
			cartTexView.setTextColor(this.getResources().getColor(R.drawable.general_bottom_selected));
		} else if (currentActivity.equals("center")) {
			centerImageButton.setImageResource(R.drawable.center);
			centerTexView.setTextColor(this.getResources().getColor(R.drawable.general_bottom_selected));
		}

		indexImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
				startActivity(intent);
			}
		});

		mallImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MallActivity.class);
				intent.putExtra("pageStr", "1");
				startActivity(intent);
			}
		});

		cartImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(MainActivity.this, "进入购物车页面！",
				// Toast.LENGTH_LONG).show();
			}
		});

		centerImageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(MainActivity.this, "进入个人中心页面！",
				// Toast.LENGTH_LONG).show();
			}
		});
	}

}