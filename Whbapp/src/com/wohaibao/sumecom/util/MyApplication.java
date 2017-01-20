package com.wohaibao.sumecom.util;

import android.app.Application;

/**
 *  Application类，提供全局上下文对象
 * @author william
 *
 */
public class MyApplication extends Application {

	public static String TAG;
	public static MyApplication myApplication;

	public static MyApplication newInstance() {
		return myApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		TAG = this.getClass().getSimpleName();
		myApplication = this;

	}
}
