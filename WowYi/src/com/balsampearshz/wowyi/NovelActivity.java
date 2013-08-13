package com.balsampearshz.wowyi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.Novel;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshListView;

public class NovelActivity extends BaseActivity implements NetReceiveDelegate {
	private List<Novel> novelList;
	private PullToRefreshListView pullToRefreshListView;
	private Button btnBack;
	private ListView listView;
	private int totalCount = 0;
	private int page = 1;
	private int pageNum = 20;
	private Boolean isloading = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_novel);
		
		initView();
		getData();
		imageLoaderOption(-1);
		
	}
	private void initView(){
		btnBack = (Button)findViewById(R.id.btn_back);
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.lv_novel_list);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getData();
			}
		});
		
	}
	
	private void getData(){
		if(!isloading){
			isloading = true;
			NetUtil netUtil = new NetUtil();
			netUtil.setDelegate(this);
			netUtil.setTag(Net_Tag.Net_Tag_Novel_List);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("p", String.valueOf(page));
			netUtil.novelList(params);
		}
		
	}
	
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		NetUtil netUtil = (NetUtil)delegate;
		if(netUtil.getTag()==Net_Tag.Net_Tag_Novel_List){
			
			
		}
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		
	}
	
	

}
