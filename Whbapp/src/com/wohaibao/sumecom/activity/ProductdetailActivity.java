package com.wohaibao.sumecom.activity;

import com.wohaibao.sumecom.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class ProductdetailActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_productdetail);
		Intent getintent =  getIntent();
		String goodsno = getintent.getStringExtra("goodsno"); 
		Toast.makeText(ProductdetailActivity.this, "产品详情页，产品编号是:  " +goodsno, Toast.LENGTH_SHORT).show();
	}
}
