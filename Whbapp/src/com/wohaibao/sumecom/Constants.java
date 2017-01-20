package com.wohaibao.sumecom;

public class Constants {
	//支付
	public static final String PAYPROJECT_WEB_PATH = "http://192.168.3.31";
	public static final String PAYPROJECT_NAME ="/payproject/payAction!wechatPay.action";

	//商城
	public static final String MALLPROJECT_WEB_PATH = "http://192.168.3.31";
	public static final String MALLPROJECT_NAME ="/mallproject/orderinfoAction!getOrderinfoByOrderid.action";
	public static final String MALLPROJECT_GOODINFO ="/mallproject/goodsinfoAction!mallGoodsinfo.action";
	public static final String IMAGE_PREFIX = "http://bankloud.cn/image";
	public static final String GOODSCOUNT_PERPAGE = "10";
	
	public static final String HTTP_URL_CONNECTION_ERR = "网络链接失败，请稍后重试！";

	// 订单号 1483669739224
	//1474978511030
	//生产环境
/*	public static String appid = "wx20d563cd53c0529e";  //微信公众平台appid
	public static String partnerId="1326053501";  //商户号
*/
//测试环境
	public static String app_wx_appid = "wx43c9b99c57cabde8";  //微信公众平台appid
}
