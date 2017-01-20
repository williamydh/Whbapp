package com.wohaibao.sumecom.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author william
 *
 */
public class HttpUtil {
	private static final Integer TIMEOUT = 5000;
	
	/**
	 * @param String
	 *            paramMap
	 * @param String
	 *            encode, if import null or empty , default UTF-8
	 * @return resultParamMap
	 */
	private static Map<String, Object> getParamStr(Map<String, String> paramMap, String encode) {
		Map<String, Object> resultParamMap = new HashMap<String, Object>();
		boolean flag = false;
		String content = "";
		String mapKey= "";
		String mapValue= "";

		if (encode == null || encode.equals("")) {
			encode = "UTF-8";
		}

		if (paramMap == null || paramMap.size() == 0) {
			flag = true;

		} else {
			String prefixStr = "";
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				if (content.length() > 0) {
					prefixStr = "&";
				}
				try {
					mapKey = entry.getKey();
					mapValue = entry.getValue();
					
					if(mapKey.indexOf("_URLEncoder")>0){
						mapKey = mapKey.replace("_URLEncoder", "");
						mapValue = URLEncoder.encode(mapValue, encode);
					}
					
					content = content + prefixStr + mapKey + "=" + mapValue;
					flag = true;
				} catch (UnsupportedEncodingException e) {
					content = "不支持" + encode + "编码，请核查！";
				}
			}
		}

		resultParamMap.put("flag", flag);
		resultParamMap.put("content", content);
		return resultParamMap;
	}

	/**
	 * @param String path
	 * @param String encode, if import null or empty , default UTF-8
	 * @param Map<String, String> paramMap
	 * @return resultMap(boolean flag,String content)
	 */
	public static Map<String, Object> sentPost(String path, String encode, Map<String, String> paramMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean flag = false;
		String content = "";

		try {
			// 根据地址创建URL对象
			URL url = new URL(path);
			HttpURLConnection urlConnection = null;
			try {
				// 根据URL对象打开链接
				urlConnection = (HttpURLConnection) url.openConnection();
				// 设置请求的方式
				urlConnection.setRequestMethod("POST");
				// 设置请求的超时时间
				urlConnection.setReadTimeout(TIMEOUT);
				urlConnection.setConnectTimeout(TIMEOUT);
				// 设置请求的头
				urlConnection.setRequestProperty("Connection", "keep-alive");
				// 设置请求的头
				// urlConnection.setRequestProperty("content-type",
				// "application/x-www-form-urlencoded;charset=UTF-8");
				urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
				urlConnection.setRequestProperty("Accept-Charset", "utf-8");
				urlConnection.setRequestProperty("contentType", "utf-8");

				Map<String, Object> resultParamMap = getParamStr(paramMap, encode);
				String resultParamMapContent = (String) resultParamMap.get("content");
				boolean resultParamMapFlag = (Boolean) resultParamMap.get("flag");
				if (!resultParamMapFlag) {
					content = resultParamMapContent;
				} else {
					String paramStr = resultParamMapContent;

					// 设置请求的头 (post 参数）
					urlConnection.setRequestProperty("Content-Length", String.valueOf(paramStr.getBytes().length));
					// 设置请求的头
					urlConnection.setRequestProperty("User-Agent",
							"Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

					urlConnection.setDoOutput(true); // 发送POST请求必须设置允许输出
					urlConnection.setDoInput(true); // 发送POST请求必须设置允许输入，setDoInput的默认值就是true
					// 获取输出流
					OutputStream os = urlConnection.getOutputStream();
					os.write(paramStr.getBytes());
					os.flush();

					if (urlConnection.getResponseCode() == 200) {
						// 获取响应的输入流对象
						InputStream is = urlConnection.getInputStream();
						// 创建字节输出流对象
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						// 定义读取的长度
						int len = 0;
						// 定义缓冲区
						byte buffer[] = new byte[1024];
						// 按照缓冲区的大小，循环读取
						while ((len = is.read(buffer)) != -1) {
							// 根据读取的长度写入到os对象中
							baos.write(buffer, 0, len);
						}
						// 释放资源
						is.close();
						baos.close();
						// 返回字符串
						content = new String(baos.toByteArray());
						flag = true;
					}
				}
			} catch (IOException e) {
				content = "网络链接失败，请稍后重试！";
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
			}
		} catch (MalformedURLException e) {
			content = "输入的路径错误，请核查！";
		}

		resultMap.put("flag", flag);
		resultMap.put("content", content);
		return resultMap;
	}

}
