package com.wohaibao.sumecom.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wohaibao.sumecom.R;
import com.wohaibao.sumecom.util.ImageLoader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class OrderdetailsActivity extends Activity{

	private ListView orderdetailsListView;
	private OrderdetailsAdapter orderdetailsAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_orderdetails);
		orderdetailsListView = (ListView) findViewById(R.id.activity_orderdetails_ListView1);
		orderdetailsAdapter = new OrderdetailsAdapter(getData());
		orderdetailsListView.setAdapter(orderdetailsAdapter);
	}
	
	private List<Map<String, Object>> getData(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for(int i=1;i<101; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ordername","订单名称"+i);
			Double orderprice = (double) (i/100.0);
			map.put("orderprice", orderprice);
			list.add(map);
		}
		return list;
	}
	
	private class OrderdetailsAdapter extends BaseAdapter{
		private List<Map<String, Object>> orderdetailsList;
		
		public OrderdetailsAdapter(List<Map<String, Object>> orderdetailsList) {
			this.orderdetailsList = orderdetailsList;
		}

		@Override
		public int getCount() {
			return orderdetailsList.size();
		}

		@Override
		public Object getItem(int position) {
			return orderdetailsList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint({ "ViewHolder", "InflateParams" })
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			if(convertView==null){
				view = LayoutInflater.from(OrderdetailsActivity.this).inflate(R.layout.gallery_item, null);
			} else {
				view = convertView;
			}
			
			TextView ordernameTextView = (TextView) view.findViewById(R.id.item_ordernameTextView);
			ordernameTextView.setText(orderdetailsList.get(position).get("ordername").toString());

			TextView orderpriceTextView = (TextView) view.findViewById(R.id.item_orderpriceTextView);
			orderpriceTextView.setText(orderdetailsList.get(position).get("orderprice").toString());
			
			ImageView productImageView = (ImageView) view.findViewById(R.id.item_productImageView);
			//new NormalLoadPictrue().getPicture("http://images.csdn.net/20130609/zhuanti.jpg", productImageView);
			
			ImageLoader imageLoader = new ImageLoader(OrderdetailsActivity.this, R.drawable.cart);
			String imagePath = "http://images.csdn.net/20130609/zhuanti.jpg";
			imageLoader.getAndSetImage(imagePath, productImageView);
			
			return view;
		}
		
	}
	
}
