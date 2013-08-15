package com.balsampearshz.wowyi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.Novel;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshListView;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class NovelActivity extends BaseActivity implements NetReceiveDelegate {
	private List<Novel> novelList;
	private PullToRefreshListView pullToRefreshListView;
	private Button btnBack;
	private ListView listView;
	
	private NovelAdapter novelAdapter;
	private int totalCount = 0;
	private int page = 1;
	private int pageNum = 20;
	private Boolean isloading = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_novel);
		
		initView();
		showPrgressDialog(this, "正在加载");
		getData();
		imageLoaderOption(R.drawable.images_default);
		
	}
	private void initView(){
		novelList = new ArrayList<Novel>();
		novelAdapter = new NovelAdapter(novelList);
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClickListener);
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.lv_novel_list);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getData();
			}
		});
		listView = pullToRefreshListView.getRefreshableView();
		listView.setAdapter(novelAdapter);
		listView.setOnScrollListener(onScrollListener);
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
			parseData(result);
		}
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		dismissDialog();
		isloading = false;
	}
	
	private void parseData(String result){
		try {
			JSONObject jsonObject = new JSONObject(result);
			totalCount = jsonObject.getJSONObject("data").getInt("totalCount");
			String novelListString = jsonObject.getJSONObject("data").getString("novelList");
			Type type = new TypeToken<List<Novel>>(){}.getType();
			List<Novel> list = JsonUtil.json2Any(novelListString, type);
			if(page==1){
				novelList.clear();
			}
			if(list!=null){
				novelList.addAll(list);
				pullToRefreshListView.onRefreshComplete();
				dismissDialog();
				isloading = false;
				novelAdapter.notifyDataSetChanged();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private class NovelAdapter extends ArrayAdapter<Novel>{
		private List<Novel> list;
		private LayoutInflater layoutInflater;
		public NovelAdapter(List<Novel> list){
			super(NovelActivity.this, 0, list);
			this.list = list;
			layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NovelHolder novelHolder;
			if(convertView==null){
				novelHolder = new NovelHolder();
				convertView = layoutInflater.inflate(R.layout.item_novel_detail, null);
				novelHolder.novelCover = (ImageView) convertView.findViewById(R.id.iv_cover);
				novelHolder.downloadButton = (Button) convertView.findViewById(R.id.btn_download);
				novelHolder.novelName = (TextView)convertView.findViewById(R.id.tv_novel_name);
				novelHolder.novelDesc = (TextView)convertView.findViewById(R.id.tv_desc);
				novelHolder.viewCount = (TextView)convertView.findViewById(R.id.tv_view_count);
				convertView.setTag(novelHolder);
			}
			else{
				novelHolder = (NovelHolder) convertView.getTag();
			}
			final Novel novel = list.get(position);
			imageLoader.displayImage(novel.getN_logo(), novelHolder.novelCover, options);
			novelHolder.downloadButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String downloadUrl = novel.getN_download_url();
					if(null!=downloadUrl&&!"".equals(downloadUrl)){
						Uri uri = Uri.parse(downloadUrl);
						Intent intent = new Intent(Intent.ACTION_VIEW,uri);
						startActivity(intent);
					}
					else{
						showToast("很抱歉，链接已经失效");
					}
				}
			});
			novelHolder.novelName.setText(novel.getN_title()==null?"":novel.getN_title());
			novelHolder.novelDesc.setText(novel.getN_desc()==null?"":novel.getN_desc());
			novelHolder.viewCount.setText("下载次数"+novel.getN_download_count()==null?"":"下载次数"+novel.getN_download_count());
			return convertView;
		}
		class NovelHolder{
			ImageView novelCover;
			Button downloadButton;
			TextView novelName;
			TextView novelDesc;
			TextView viewCount;
		}
	}
	/**
	 * 滚动刷新监听器
	 */
	private OnScrollListener onScrollListener = new OnScrollListener() {
		int scrollState;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			this.scrollState = scrollState;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// 满足下列条件后可开始加载下一页
			if ((scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || scrollState == OnScrollListener.SCROLL_STATE_FLING) && !isloading) {
				if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalCount > page * pageNum) {
					page++;
					getData();
				}
			}
		}
	};
	
	private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_back:
				finish();
				break;
			default:
				break;
			}
		}
	};
	

}
