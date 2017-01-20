package com.wohaibao.sumecom;

import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wohaibao.sumecom.util.HttpUtil;
import com.wohaibao.sumecom.util.RSAUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayActivity extends Activity {
	private static final String TAG = "PayActivity-->";
	private IWXAPI api;

	private Button checkWxPaySupportbutton;
	private Button callPayButton;
	private EditText editTextOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay);
		api = WXAPIFactory.createWXAPI(this, Constants.app_wx_appid, true);
		// 将该app注册到微信
		api.registerApp(Constants.app_wx_appid);
		
		Intent getintent =  getIntent();
		String jsonStr = getintent.getStringExtra("jsonStr");
		@SuppressWarnings("unchecked")
		Map<String, Object> orderMap = (Map<String, Object>) JSON.parse(jsonStr);
		//Map<String, Object> orderMap = (Map<String, Object>) JSONUtil.mapFromJSONString(jsonStr).get("object");
		Log.i(TAG,"order id  is : "+ orderMap.get("orderid"));
		final String out_trade_no = (String) orderMap.get("orderid");
		editTextOrder = (EditText) findViewById(R.id.activity_pay_editTextOrderid);
		editTextOrder.setText(out_trade_no);
		checkWxPaySupportbutton = (Button) findViewById(R.id.checkWxPaySupportbutton);
		checkWxPaySupportbutton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
				String msg = "";
				if (isPaySupported) {
					msg = "当前版本支持微信支付！";
				} else {
					msg = "当前版本不支持微信支付，请更新版本！！";
				}

				Toast.makeText(PayActivity.this, msg, Toast.LENGTH_LONG).show();

			}
		});

		callPayButton = (Button) findViewById(R.id.callPayButton);
		callPayButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new MyTask().execute(Constants.PAYPROJECT_WEB_PATH + Constants.PAYPROJECT_NAME+"," + out_trade_no);

				callPayButton.setEnabled(false);
				Toast.makeText(PayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
			}

		});
	}

	class MyTask extends AsyncTask<String, Void, Map<String, Object>> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Map<String, Object> doInBackground(String... params) {
			Log.i(TAG, "to do in background!!");
			String[] paramStr = params[0].split(",");
			String path = paramStr[0];
			Log.i(TAG, "path is : "+ path);
			Log.i(TAG, "paramStr is : "+ paramStr[1]);			
			Map<String, String> mapParam = new HashMap<String, String>();
			mapParam.put("body", "测试订单1012");
			mapParam.put("out_trade_no", UUID.randomUUID().toString().replaceAll("-", ""));
			mapParam.put("paytype", "wechat_APP");
			mapParam.put("total_fee", "1");
			JSONObject json = new JSONObject(mapParam);

			try {
				String mapStr = json.toString();
				Log.i(TAG, "map string is "+mapStr);
				// 从文件中得到公钥
				InputStream inPublic = getResources().getAssets().open("sg_public_key.pem");
				PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
				String afterencrypt = RSAUtils.rsaencypt(mapStr, inPublic, publicKey);				
				Map<String, String> paramsMap = new HashMap<String, String>();
				paramsMap.put("payParameter", afterencrypt);
				return HttpUtil.sentPost(path, "utf-8", paramsMap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(Map<String, Object> result) {
			super.onPostExecute(result);
			
			boolean httpResultFlag = (Boolean) result.get("flag");
			String httpResultStrt = (String) result.get("content");
			if (httpResultFlag) {
				Log.i(TAG, "从服务器获取的jsonSting字串： " + httpResultStrt);
				try {
					// 从文件中得到私钥
					InputStream inPrivate = getResources().getAssets().open("sg_private_key.pem");
					PrivateKey privateKey = RSAUtils.loadPrivateKey(inPrivate);

					String decryptStr = RSAUtils.rsadecypt(httpResultStrt, inPrivate, privateKey);

					Log.i(TAG, "decryptStr is : " + decryptStr);
					JSONObject json = new JSONObject(decryptStr);

					if (json != null) {

						PayReq req = new PayReq();
						req.appId = json.getString("appid");
						req.partnerId = json.getString("partnerid");
						req.prepayId = json.getString("prepayid");
						req.nonceStr = json.getString("noncestr");
						req.timeStamp = json.getString("timestamp");
						req.packageValue = json.getString("package");
						req.sign = json.getString("sign");
						req.extData = "app data"; // optional
//						Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();

						// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
						api.sendReq(req);
					}
					callPayButton.setEnabled(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(PayActivity.this, httpResultStrt, Toast.LENGTH_SHORT).show();
			}

		}
	}

}
