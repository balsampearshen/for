package com.balsampearshz.wowyi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreference {

	private SharedPreferences mPerferences;

	public MySharedPreference(Context context) {
		mPerferences = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public String getKeyStr(String key) {
		return mPerferences.getString(key, "");
	}
	
	public Boolean getKeyBoolean(String key) {
		return mPerferences.getBoolean(key, false);
	}
	public int getKeyInt(String key) {
		return mPerferences.getInt(key, 0);
	}
	public Boolean setKeyBoolean(String key, Boolean bl) {
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putBoolean(key, bl);
		mEditor.commit();
		return true;
	}
	public Boolean setKeyInt(String key, int info) {
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.putInt(key, info);
		mEditor.commit();
		return true;
	}
	public Boolean setKeyStr(String key, String info) {
		SharedPreferences.Editor mEditor = mPerferences.edit();
		
		mEditor.putString(key, info);
		mEditor.commit();
		return true;
	}
	
	public void removeKey(String key) {
		SharedPreferences.Editor mEditor = mPerferences.edit();
		mEditor.remove(key);
		mEditor.commit();
	}

}
