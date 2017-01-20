package com.wohaibao.sumecom;

import java.io.InputStream;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.wohaibao.sumecom.activity.GeneralBottomActivity;
import com.wohaibao.sumecom.util.HttpUtil;
import com.wohaibao.sumecom.util.RSAUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity2 extends GeneralBottomActivity {
	private String TAG = "mainActivity-->";
	private Button gotoPayButton;
	private ProgressDialog dialog;
	private EditText editText1;
	private static String currentActivity = "index";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("加载中");
		editText1 = (EditText) findViewById(R.id.activity_pay_editTextOrderid);

		super.initGeneralBottom(MainActivity2.this, currentActivity);
		gotoPayButton = (Button) findViewById(R.id.gotoPayButton);
		gotoPayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String orderid = editText1.getText().toString();
				if (orderid != null && !orderid.equals("")) {
					new OrderinfoTask()
							.execute(Constants.MALLPROJECT_WEB_PATH + Constants.MALLPROJECT_NAME + "," + orderid);
				} else {
					Toast.makeText(MainActivity2.this, "订单号不能为空！", Toast.LENGTH_LONG).show();
				}
			}
		});

	}

	class OrderinfoTask extends AsyncTask<String, Void, Map<String, Object>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// dialog.show();
		}

		@Override
		protected Map<String, Object> doInBackground(String... params) {

			Log.i(TAG, "params[0] is : " + params[0]);
			String[] paramStr = params[0].split(",");
			Log.i(TAG, paramStr[0]);
			String orderidStr = paramStr[1];

			Map<String, String> paramsMap = new HashMap<String, String>();
			paramsMap.put("orderid", orderidStr);
			paramsMap.put("recname", "测试1");
			Map<String, Object> httpResultMap = HttpUtil.sentPost(paramStr[0], "utf-8", paramsMap);
			return httpResultMap;

		}


		@Override
		protected void onPostExecute(Map<String, Object> result) {
			Log.i(TAG, "http result map is : " + result.toString());
			boolean httpResultFlag = (Boolean) result.get("flag");
			String httpResultStrt = (String) result.get("content");
			if (httpResultFlag) {
				try {
					// 从文件中得到私钥
					InputStream inPrivate = getResources().getAssets().open("sg_private_key.pem");
					PrivateKey privateKey = RSAUtils.loadPrivateKey(inPrivate);
					String decryptStr = RSAUtils.rsadecypt(httpResultStrt, inPrivate, privateKey);
					String jsonStr = java.net.URLDecoder.decode(decryptStr, "UTF-8");

					//Map<String, Object> map = JSONUtil.mapFromJSONString(jsonStr);
					@SuppressWarnings("unchecked")
					Map<String, Object> map = (Map<String, Object>) JSON.parse(jsonStr);
					boolean flag = (Boolean) map.get("flag");
					if (flag) {
						Intent intent = new Intent(MainActivity2.this, PayActivity.class);
						intent.putExtra("jsonStr", jsonStr);
						startActivity(intent);
					} else {
						Toast.makeText(MainActivity2.this, "没有此单号的订单！", Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// dialog.dismiss();
				}
			} else {
				Log.i(TAG, "do false");
				Toast.makeText(MainActivity2.this, httpResultStrt, Toast.LENGTH_LONG).show();

			}

		}

	}

}
