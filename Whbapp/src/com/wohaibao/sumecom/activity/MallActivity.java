package com.wohaibao.sumecom.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.wohaibao.sumecom.Constants;
import com.wohaibao.sumecom.R;
import com.wohaibao.sumecom.util.HttpUtil;
import com.wohaibao.sumecom.util.ImageLoader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MallActivity extends GeneralBottomActivity {
	private String TAG = "MallActivity-->";
	private ListView goodsinfoListView;
	private boolean willDivPage;
	private ProgressDialog dialog;
	private GoodsinfoAdapter goodsinfoAdapter;
	private List<Map<String, Object>> goodsinfoTotalList = new ArrayList<Map<String, Object>>();// 全部商品列表
	private static int page;
	private static String currentActivity = "mall";

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.activity_mall_serchbar_editText:
			Intent intent = new Intent(MallActivity.this, HistoryserchActivity.class);
			startActivity(intent);
			break;
		case R.id.activity_mall_serchbar_previous_imageView:
			finish();
			break;
		case R.id.activity_mall_serchbar_menu_imageView:
			Toast.makeText(MallActivity.this, "show menu !!!", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mall);
		Intent getintent = getIntent();
		String pageStr = getintent.getStringExtra("pageStr");
		page = Integer.parseInt(pageStr);
		super.initGeneralBottom(MallActivity.this, currentActivity);

		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("加载中");
		goodsinfoAdapter = new GoodsinfoAdapter(goodsinfoTotalList);
		goodsinfoListView = (ListView) findViewById(R.id.activity_mall_ListView);
		goodsinfoListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		goodsinfoListView.setAdapter(goodsinfoAdapter);

		goodsinfoListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String goodsno = goodsinfoTotalList.get(position).get("goodsno").toString();
				Intent intent = new Intent(MallActivity.this, ProductdetailActivity.class);
				intent.putExtra("goodsno", goodsno);
				startActivity(intent);
			}
		});

		goodsinfoListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (willDivPage && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					new GoodsinfoTask()
							.execute(Constants.MALLPROJECT_WEB_PATH + Constants.MALLPROJECT_GOODINFO + "," + page);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				willDivPage = (firstVisibleItem + visibleItemCount == totalItemCount);
			}
		});

		new GoodsinfoTask().execute(Constants.MALLPROJECT_WEB_PATH + Constants.MALLPROJECT_GOODINFO + "," + page);

	}

	class GoodsinfoTask extends AsyncTask<String, Void, Map<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog.show();
		}

		@Override
		protected Map<String, Object> doInBackground(String... params) {

			String[] paramStr = params[0].split(",");

			Map<String, String> paramsMap = new HashMap<String, String>();
			// page=0&rows=10&sort=createtime&order=desc
			paramsMap.put("page", paramStr[1]);
			paramsMap.put("rows", Constants.GOODSCOUNT_PERPAGE);
			paramsMap.put("sort", "createtime");
			paramsMap.put("order", "desc");
			paramsMap.put("isupshelves", "1");

			// 参数值为中文的需要URLEncoder编码，参数名后面加_URLEncoder
			// paramsMap.put("goodsname__URLEncoder", "华为");

			Map<String, Object> httpResultMap = HttpUtil.sentPost(paramStr[0], "utf-8", paramsMap);
			return httpResultMap;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onPostExecute(Map<String, Object> result) {
			boolean httpResultFlag = (Boolean) result.get("flag");
			String httpResultStrt = (String) result.get("content");
			Log.i("MallActivity onPostExcute ------>", "httpResultStr is : " + httpResultStrt);
			if (httpResultFlag) {
				List<Map<String, Object>> goodsinfoList = (List<Map<String, Object>>) JSON.parse(httpResultStrt);
				Log.i(TAG, "goodsinfoList :" + goodsinfoList.getClass().getName());
				goodsinfoTotalList.addAll(goodsinfoList);
				if (page == 1) {
					goodsinfoListView.setAdapter(goodsinfoAdapter);
				}
				goodsinfoAdapter.notifyDataSetChanged();
				page++;
			} else {
				Log.i(TAG, "do false");
				Toast.makeText(MallActivity.this, httpResultStrt, Toast.LENGTH_LONG).show();
			}

			// dialog.dismiss();
		}

	}

	class GoodsinfoAdapter extends BaseAdapter {
		List<Map<String, Object>> goodsinfoList;

		public GoodsinfoAdapter(List<Map<String, Object>> goodsinfoList) {
			this.goodsinfoList = goodsinfoList;
		}

		@Override
		public int getCount() {
			return goodsinfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return goodsinfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View goodsinfoView = null;
			if (convertView == null) {
				goodsinfoView = LayoutInflater.from(MallActivity.this).inflate(R.layout.mall_goodsinfo_listview, null);
			} else {
				goodsinfoView = convertView;
			}

			ImageView mall_mainpicImageView = (ImageView) goodsinfoView
					.findViewById(R.id.mall_products_listview_imageView);
			TextView mall_goodsnameTextView = (TextView) goodsinfoView
					.findViewById(R.id.mall_products_listview_goodsname_textView);
			TextView mall_packagepriceTextView = (TextView) goodsinfoView
					.findViewById(R.id.mall_products_listview_packageprice_textView);

			mall_goodsnameTextView.setText(goodsinfoList.get(position).get("goodsname").toString());
			mall_packagepriceTextView.setText("￥" + goodsinfoList.get(position).get("packageprice").toString() + "元");

			ImageLoader imageLoader = new ImageLoader(MallActivity.this, R.drawable.cart);
			imageLoader.getAndSetImage(Constants.IMAGE_PREFIX + goodsinfoList.get(position).get("mainpic").toString(),
					mall_mainpicImageView);

			return goodsinfoView;
		}

	}

}
