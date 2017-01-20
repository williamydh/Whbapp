package com.wohaibao.sumecom.util;

import java.io.IOException;
import java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wohaibao.sumecom.filter.FastjsonFilter;

public class FastJsonUtil {
	public String object2JsonByFilter(Object object, String[] includesProperties, String[] excludesProperties) {
		String json = "";
		FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
		if (excludesProperties != null && excludesProperties.length > 0) {
			filter.getExcludes().addAll(Arrays.<String>asList(excludesProperties));
		}
		if (includesProperties != null && includesProperties.length > 0) {
			filter.getIncludes().addAll(Arrays.<String>asList(includesProperties));
		}
		// includesProperties + "]");

		// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd
		// hh24:mi:ss
		// 使用SerializerFeature.DisableCircularReferenceDetect特性
		// 关闭引用检测和生成并禁用循环引用对象
		json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat,
				SerializerFeature.DisableCircularReferenceDetect);

		return json;
	}
	
	public String writeJsonByFilter(Object object, String[] includesProperties, String[] excludesProperties) {
	
			String json = object2JsonByFilter(object, includesProperties, excludesProperties);
			return json;
	}
	
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @throws IOException
	 */
	public void writeJson(Object object) {
		writeJsonByFilter(object, null, null);
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @param includesProperties
	 *            需要转换的属性
	 */
	public void writeJsonByIncludesProperties(Object object, String[] includesProperties) {
		writeJsonByFilter(object, includesProperties, null);
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object
	 * @param excludesProperties
	 *            不需要转换的属性
	 */
	public void writeJsonByExcludesProperties(Object object, String[] excludesProperties) {
		writeJsonByFilter(object, null, excludesProperties);
	}
	
}
