package com.wohaibao.sumecom.util;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

/**
 * 图片加载类
 * 
 * @author Administrator
 * 
 */
public class ImageLoader {
	private Context context;
	// private int loadingImage;
	private int errorImage;
	private LruCache<String, Bitmap> mapCache;

	@SuppressLint("NewApi")
	public ImageLoader(Context context, int errorImage) {
		this.context = context;
		// this.loadingImage = loadingImage;
		this.errorImage = errorImage;
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int mCacheSize = maxMemory / 8;
		mapCache = new LruCache<String, Bitmap>(mCacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	public void getAndSetImage(String imagePath, ImageView iv_image) {
		// 保存当前的ImageView对应的imagepath
		iv_image.setTag(imagePath);
		// iv_image.setImageResource(loadingImage);
		/*
		 * 从第一级缓存中找对应imagePath的图片 如果第一级缓存有对应图片，显示！ 如果第一级缓存没有图片，从第二级缓存中找
		 */
		Bitmap bitmap = getImageByFirstCache(imagePath);
		if (bitmap != null) {
			iv_image.setImageBitmap(bitmap);
			System.out.println("从一级缓存中找到");
			return;
		}
		/*
		 * 从第二级缓存中找对应的图片 如果有，则缓存到第一缓存中 如果没有，则从第三集缓存中找
		 */
		bitmap = getImageBySecondCache(imagePath);
		if (bitmap != null) {
			iv_image.setImageBitmap(bitmap);
			cacheInFirst(imagePath, bitmap);
			System.out.println("从二级缓存中找到");
			return;
		}

		/*
		 * 从第三级缓存中找对应的图片 如果有，则缓存到第一、二缓存中 如果没有，则显示错误的图片
		 */
		loadImageByThridCache(imagePath, iv_image);
	}

	/**
	 * 将图片缓存到一级缓存
	 * 
	 * @param imagePath
	 *            图片的url
	 * @param bitmap
	 */
	@SuppressLint("NewApi")
	private void cacheInFirst(String imagePath, Bitmap bitmap) {

		mapCache.put(imagePath, bitmap);
	}

	/**
	 * 从三级缓存中寻找图片
	 * 
	 * @param imagePath
	 *            图片的url
	 * @param
	 * @return
	 */
	private void loadImageByThridCache(final String imagePath, final ImageView iv_image) {
		new AsyncTask<String, Void, Bitmap>() {
			/**
			 * 开启异步任务前调用
			 */
			@Override
			protected void onPreExecute() {

			}

			/**
			 * 异步任务完成后调用
			 */
			@Override
			protected void onPostExecute(Bitmap result) {
				String nowImagePath = (String) iv_image.getTag();
				if (!nowImagePath.equals(imagePath)) {
					// 如果当前请求的图片路径和需要显示的图片路径不一致的话,就不显示图片
					System.out.println("不显示图片了");
					return;
				}

				if (result != null) {
					iv_image.setImageBitmap(result);
				} else {
					iv_image.setImageResource(errorImage);
				}
			}

			/**
			 * 后台进行异步任务
			 */
			@Override
			protected Bitmap doInBackground(String... params) {
				String nowImagePath = (String) iv_image.getTag();
				if (!nowImagePath.equals(params[0])) {
					// 如果当前请求的图片路径和需要显示的图片路径不一致的话,就不进行网络请求了
					System.out.println("不进行网络请求了");
					return null;
				}
				String url = params[0];
				HttpURLConnection conn = null;
				try {
					URL mUrl = new URL(url);
					conn = (HttpURLConnection) mUrl.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(6000);
					conn.setConnectTimeout(6000);
					conn.setDoInput(true);
					conn.connect();

					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(in);
						// 在分线程中缓存图片到一级和二级缓存
						cacheInFirst(url, bitmap);
						String imageName = url.substring(url.lastIndexOf("/") + 1);
						String fileName = context.getExternalFilesDir(null).getAbsolutePath() + "/" + imageName;
						bitmap.compress(CompressFormat.JPEG, 50, new FileOutputStream(fileName));
						return bitmap;
					} else {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} finally {
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}.execute(imagePath);
	}

	/**
	 * 从二级缓存中寻找图片
	 * 
	 * @param imagePath
	 *            图片的url
	 * @return
	 */
	private Bitmap getImageBySecondCache(String imagePath) {
		String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
		String fileName = context.getExternalFilesDir(null).getAbsolutePath() + "/" + imageName;
		return BitmapFactory.decodeFile(fileName);
	}

	/**
	 * 从一级缓存中寻找图片
	 * 
	 * @param imagePath
	 *            图片的url
	 * @return
	 */
	@SuppressLint("NewApi")
	private Bitmap getImageByFirstCache(String imagePath) {
		return mapCache.get(imagePath);
	}

}