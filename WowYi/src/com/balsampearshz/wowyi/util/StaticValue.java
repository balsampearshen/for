package com.balsampearshz.wowyi.util;

import android.content.Context;
import android.os.Environment;

public class StaticValue {
	public static final String PACKAGE_NAME = "com.balsampearshz.wowyi";
	//appName
	public static final String APP_NAME = "魔兽忆";
	//en
	public static final String APP_NAME_EN = "wowyi";
	//version
	public static final String VERSION = "1.0";
	//deviceid
	public static String DEVICEID = "000000000000000";
	//传输参数TTID
	public static final String TTID = "400000_21390984@zxf_Android_1.1";
	//传输参数SID
	public static String SID = "t400000122718501";
	//设备类型
	public static String DEVICE_TYPE="android";
	
	

	//数据接口
	public static final String URL = "http://1.wowyi.sinaapp.com/index.php/Mobile/";
	//获取本地图片位置
	public static String getPicCachePath() {
		String sdState = Environment.getExternalStorageState();// 获得sd卡的状态
		String picCachePath = "";
		if (sdState.equals(Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
			picCachePath = "/sdcard-ext/Android/data/"+PACKAGE_NAME+"/cache";
		}else{
			picCachePath = Environment.getExternalStorageDirectory() + "/Android/data/"+PACKAGE_NAME+"/cache";
		}
		
		return picCachePath;
	}
	
	//获取本地保存位置
	public static String getPicSavePath(Context context) {
		String sdState = Environment.getExternalStorageState();// 获得sd卡的状态
		String picCachePath = "";
		if (!sdState.equals(Environment.MEDIA_MOUNTED)) { // 判断SD卡是否存在
			picCachePath = "/sdcard-ext/dcim/"+PACKAGE_NAME;
		}else{
			picCachePath = Environment.getExternalStorageDirectory() + "/dcim/"+PACKAGE_NAME;
		}
		
		return picCachePath;
	}
	
	//sharepreference
	public static final String SharePreference_UserSession  = "share_prefrence_user_session";
	public static final String SharePreference_UserProfile = "share_prefrence_user_profile";
	public static final String SharePreference_UserDevice = "share_prefrence_user_device";
	
	public static final String SharePreference_TBSID = "share_prefrence_tb_sid";
	
	public static final String SharePreference_Passcode = "share_prefrence_passcode";
	public static final String SharePreference_AlwaysLoadHDImage = "share_prefrence_always_load_HD_image";
	
	public static final String SharePreference_ItemCategoryCache = "share_preference_item_category_cache";
	public static final String SharePreference_ShopsTopicCache_SaveTime = "share_preference_shops_topic_cache_time";
	
	public static final long saveCacheTime = 30*60*1000;//缓存存储时间
	
	public static int NOCRYPT = 1; //0:开启des解码，  1：关闭des解码
}
