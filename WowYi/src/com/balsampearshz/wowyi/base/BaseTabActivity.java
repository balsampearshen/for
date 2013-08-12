package com.balsampearshz.wowyi.base;

import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

@SuppressWarnings("deprecation")
public class BaseTabActivity extends TabActivity {

	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
//		MobclickAgent.updateOnlineConfig(this);

	}

	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onResume(mContext);
	}

	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPause(mContext);
	}
}
