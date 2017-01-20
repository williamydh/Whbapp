package com.wohaibao.sumecom.activity;

import com.wohaibao.sumecom.R;
import com.wohaibao.sumecom.util.ImageLoader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class HistoryserchActivity extends Activity {
	private ImageView imageView1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historyserch);
		Toast.makeText(HistoryserchActivity.this, "历史搜索页", Toast.LENGTH_SHORT).show();
		
		imageView1 = (ImageView) this.findViewById(R.id.imageView1);
		ImageLoader imageLoader = new ImageLoader(HistoryserchActivity.this, R.drawable.cart);
		String imagePath = "http://bankloud.cn/image/product/201701/20170103135716_CRdQ3K21.jpg";
		imageLoader.getAndSetImage(imagePath, imageView1);
		
	}
}
