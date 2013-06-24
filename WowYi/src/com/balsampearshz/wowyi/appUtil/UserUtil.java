package com.balsampearshz.wowyi.appUtil;

import java.lang.reflect.Type;

import android.bluetooth.BluetoothClass.Device;
import android.content.Context;

import com.balsampearshz.wowyi.bean.User;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.balsampearshz.wowyi.util.MySharedPreference;
import com.balsampearshz.wowyi.util.StaticValue;
import com.google.gson.reflect.TypeToken;

public class UserUtil {
	
	

	public static void setMySession(Context context, String session){
		
		MySharedPreference sp = new MySharedPreference(context);
		sp.setKeyStr(StaticValue.SharePreference_UserSession, session);
	}
	
	public static String getMySession(Context context){
		
		MySharedPreference sp = new MySharedPreference(context);
	    
	   if(!"".equals(sp.getKeyStr(StaticValue.SharePreference_UserSession))){
		   return sp.getKeyStr(StaticValue.SharePreference_UserSession);
	   }
	   else {
		   return "";
	   }
	}
	
	public static void setMyUserProfile(Context context, String userProfile){
		
		MySharedPreference sp = new MySharedPreference(context);
		sp.setKeyStr(StaticValue.SharePreference_UserProfile, userProfile);
	}
	
	public static User getMyUserProfile(Context context){
		MySharedPreference sp = new MySharedPreference(context);
		 String userStr = sp.getKeyStr(StaticValue.SharePreference_UserProfile);
		   if(!"".equals(userStr)){
			   User user = null;
			   try {
				   Type type = new TypeToken<User>() {}.getType();
				   user  = JsonUtil.json2Any(userStr, type);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return user;
		   }
		   else {
			   return null;
		   }
	}
	
	public static void setMyUserDevice(Context context, String userDevice){
		MySharedPreference sp = new MySharedPreference(context);
		sp.setKeyStr(StaticValue.SharePreference_UserDevice, userDevice);
	}
	
	public static Device getMyUserDevice(Context context){
		
		MySharedPreference sp = new MySharedPreference(context);
		 String deviceStr = sp.getKeyStr(StaticValue.SharePreference_UserDevice);
		   if(!"".equals(deviceStr)){
			   
			   Type type = new TypeToken<Device>() {}.getType();
			   Device device  = JsonUtil.json2Any(deviceStr, type);
			   
			   return device;
		   }
		   else {
			   return null;
		   }
	}
}
