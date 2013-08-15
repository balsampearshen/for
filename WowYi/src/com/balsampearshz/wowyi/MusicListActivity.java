package com.balsampearshz.wowyi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.Music;
import com.balsampearshz.wowyi.bean.MusicCategory;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshListView;

public class MusicListActivity extends BaseActivity implements NetReceiveDelegate {
	private PullToRefreshListView pullToRefreshListView;
	private MusicCategory musicCategory;
	private List<Music> musicList;
	private Button btnBack;
	private ListView listView;
	
	private int page = 1;
	private int pageNum = 20;
	private int totalCount = 0;
	private Boolean isLoading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_list);
		
		initData();
		initView();
		showPrgressDialog(this, "正在加载，请稍候");
		getData();
	}
	private void initData(){
		Intent intent = getIntent();
		musicCategory = (MusicCategory) intent.getExtras().getSerializable("music");
	}
	private void initView(){
		musicList = new ArrayList<Music>();
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.lv_music_list);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getData();
			}
		});
		listView = pullToRefreshListView.getRefreshableView();
	}
	private void getData(){
		if(!isLoading){
			isLoading = true;
			NetUtil netUtil = new NetUtil();
			netUtil.setDelegate(this);
			netUtil.setTag(Net_Tag.Net_Tag_Music_List);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("p", page+"");
			params.put("mc_id", musicCategory.getMc_id());
			netUtil.musicList(params);
		}
	}
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		NetUtil netUtil = (NetUtil)delegate;
		if(netUtil.getTag()==Net_Tag.Net_Tag_Music_List){
			
		}
		
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		dismissDialog();
		isLoading = false;
	}

}
