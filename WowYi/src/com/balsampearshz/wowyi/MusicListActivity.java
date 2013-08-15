package com.balsampearshz.wowyi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.Music;
import com.balsampearshz.wowyi.bean.MusicCategory;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshListView;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class MusicListActivity extends BaseActivity implements NetReceiveDelegate {
	private PullToRefreshListView pullToRefreshListView;
	private MusicCategory musicCategory;
	private MusicListAdapter musicListAdapter;
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
		musicCategory = (MusicCategory) intent.getExtras().getSerializable("musicCategory");
	}
	private void initView(){
		musicList = new ArrayList<Music>();
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClickListener);
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.lv_music_list);
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getData();
			}
		});
		listView = pullToRefreshListView.getRefreshableView();
		listView.setOnScrollListener(onScrollListener);
		musicListAdapter = new MusicListAdapter(this, listView, musicList);
		listView.setAdapter(musicListAdapter);
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
			try {
				JSONObject jsonObject = new JSONObject(result);
				int status = jsonObject.getInt("status");
				if(status==1){
					totalCount = jsonObject.getJSONObject("data").getInt("totalCount");
					String musicListStr = jsonObject.getJSONObject("data").getString("musicList");
					Type type = new TypeToken<List<Music>>(){}.getType();
					List<Music> list = JsonUtil.json2Any(musicListStr, type);
					if(page==1){
						musicList.clear();
					}
					if(null!=list){
						musicList.addAll(list);
						dismissDialog();
						isLoading = false;
						pullToRefreshListView.onRefreshComplete();
						musicListAdapter.notifyDataSetChanged();
					}
					
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		dismissDialog();
		isLoading = false;
	}
	
	private class MusicListAdapter extends ArrayAdapter<Music>{
		private LayoutInflater inflater;
		private List<Music> list;
		
		public MusicListAdapter(Context context, ListView listView,List<Music> list) {
			super(context, 0, list);
			inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
			this.list = list;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(null==convertView){
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_music_list_cell, null);
				viewHolder.title = (TextView)convertView.findViewById(R.id.tv_name);
				viewHolder.desc = (TextView)convertView.findViewById(R.id.tv_desc);
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Music music = list.get(position);
			viewHolder.title.setText(null==music.getM_title()?"":music.getM_title());
			viewHolder.desc.setText(null==music.getM_desc()?"":music.getM_desc());
			return convertView;
			
		}
		class ViewHolder{
			TextView title;
			TextView desc;
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
			if ((scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || scrollState == OnScrollListener.SCROLL_STATE_FLING) && !isLoading) {
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
