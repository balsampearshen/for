package com.balsampearshz.wowyi;

import com.balsampearshz.wowyi.base.BaseTabActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TabHost;

public class MainActivity extends BaseTabActivity {
	private TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host);
		
		initView();
	}
	private void initView(){
		tabHost = getTabHost();
		LayoutInflater li = LayoutInflater.from(this);
		// 搜索
		View foodlayout = li.inflate(R.layout.tab_search, null);
		Intent intent1 = new Intent(this, SearchActivity.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(foodlayout).setContent(intent1));

		// 首页
		View ranklayout = li.inflate(R.layout.tab_home, null);
		Intent intent5 = new Intent();
		intent5.setClass(this, HomeActivity.class);
		tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator(ranklayout).setContent(intent5));
		tabHost.setCurrentTab(1);
		// 更多
		View travellayout = li.inflate(R.layout.tab_app_recommend, null);
		Intent intent4 = new Intent();
		intent4.setClass(this, AppRecommendActivity.class);
		intent4.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(travellayout).setContent(intent4));
		
		
	}



}
