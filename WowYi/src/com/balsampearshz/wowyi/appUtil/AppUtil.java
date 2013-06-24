package com.balsampearshz.wowyi.appUtil;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.view.View;

import com.balsampearshz.wowyi.util.MySharedPreference;
import com.balsampearshz.wowyi.util.StaticValue;


public class AppUtil {
	/**
	 * 设置标题背景
	 * 
	 * @param activity
	 * @param view
	 */
	public static void setTitleViewBackground(Activity activity,View view,Drawable drawable) {
		// 获取屏幕宽度
		int screenWidth = getSreenWidth(activity);
		// 得到平铺后的bitmap
		Bitmap bitmap = BitmapUtil.createRepeater(screenWidth, BitmapUtil.drawable2Bitmap(drawable));
		view.setBackgroundDrawable(BitmapUtil.bitmap2Drawable(bitmap));
	}

	public static void initApp(Context context) {
		MySharedPreference sp = new MySharedPreference(context);
		if ("".equals(sp.getKeyStr(StaticValue.SharePreference_TBSID))) {
			StringBuffer sb = new StringBuffer();
			sb.append("t");
			for (int i = 0; i < 4; i++) {
				int randomInt = (int) (Math.random() * 90000 + 10000);
				sb.append(randomInt + "");
			}
			sp.setKeyStr(StaticValue.SharePreference_TBSID, sb.substring(0, 16).toString());
		}
	}

	/**
	 * 退出app
	 */
	public static void exitApp() {
		System.exit(0);
	}

	/**
	 * 判断是否是平板
	 * 
	 * @param mContext
	 * @return
	 */
	public static boolean isTabletDevice(Context mContext) {
		TelephonyManager telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		int type = telephony.getPhoneType();
		if (type == TelephonyManager.PHONE_TYPE_NONE) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取当前应用的版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {

		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return StaticValue.VERSION;
	}

	public static void setHDImageStatus(Context context, boolean loadHD) {
		MySharedPreference sp = new MySharedPreference(context);
		sp.setKeyBoolean(StaticValue.SharePreference_AlwaysLoadHDImage, loadHD);
	}

	public static boolean getHDImageStatus(Context context) {
		MySharedPreference sp = new MySharedPreference(context);
		return sp.getKeyBoolean(StaticValue.SharePreference_AlwaysLoadHDImage);
	}

	/**
	 * 匹配图片
	 */
	public static String fitImage(Context context, String imageUrl, int needWidth, int needHeight) {
		if (imageUrl == null || "".equals(imageUrl))
			return "http://www.xf69.com/";
		String finalImgUrlStr = imageUrl;

		// boolean loadHDImg = false;
		//
		// if(new
		// MySharedPreference(context).getKeyBoolean(StaticValue.SharePreference_AlwaysLoadHDImage)){
		// loadHDImg = true;
		// }
		// if("WIFI".equals(getNetState(context))){
		// loadHDImg = true;
		// }
		try {
			URL url = new URL(imageUrl);
			String hostStr = url.getHost().substring(url.getHost().indexOf('.') + 1);
			// 是否加载高清图片
			if (!"WIFI".equals(getNetState(context)) && !getHDImageStatus(context)) {
				needWidth = needWidth / 2;
				needHeight = needHeight / 2;
			}

			if ("taobaocdn.com".equals(hostStr)) {
				if (needWidth < 100) {
					finalImgUrlStr = imageUrl + "_100x100.jpg";
				} else if (needWidth <= 120) {
					finalImgUrlStr = imageUrl + "_120x120.jpg";
				} else if (needWidth <= 160) {
					finalImgUrlStr = imageUrl + "_160x160.jpg";
				} else if (needWidth <= 220) {
					finalImgUrlStr = imageUrl + "_220x220.jpg";
				} else if (needWidth <= 310) {
					finalImgUrlStr = imageUrl + "_310x310.jpg";
				} else if (needWidth <= 400) {
					finalImgUrlStr = imageUrl + "_400x400.jpg";
				} else if (needWidth <= 620) {
					finalImgUrlStr = imageUrl + "_620x10000.jpg";
				} else {
					finalImgUrlStr = imageUrl;
				}
			} else if ("xf69.com".equals(hostStr)) {
				finalImgUrlStr = imageUrl + "." + needWidth + "x" + needHeight + ".jpg";
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return finalImgUrlStr;
	}

	/**
	 * 获取手机联网状态
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetState(Context context) {

		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mConnectivity.getActiveNetworkInfo();
		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			return null;
		}
		if (info.isConnected()) {
			int netType = info.getType();
			int netSubtype = info.getSubtype();

			if (netType == ConnectivityManager.TYPE_WIFI) {
				return "WIFI";
			} else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !tm.isNetworkRoaming()) {
				return "3G";
			} else if (netType == ConnectivityManager.TYPE_MOBILE) {
				return "GPRS";
			} else {
				return "未知";
			}
		} else {
			return null;
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getSreenWidth(Activity activity) {
		String width_key = "share_prefrence_screent_width";
		MySharedPreference sp = new MySharedPreference(activity);
		// if(0!=sp.getKeyInt(width_key))
		// {
		// return sp.getKeyInt(width_key);
		// }else {
		int screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
		sp.setKeyInt(width_key, screenWidth);
		return screenWidth;
		// }
	}
}
