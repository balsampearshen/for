package com.balsampearshz.wowyi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.ImageCategory;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshGridView;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class ImagesActivity extends BaseActivity implements NetReceiveDelegate {
	private PullToRefreshGridView pullToRefreshGridView;
	private GridView gridView;
	private List<ImageCategory> imageCategories;
	private ImageListAdapter imageListAdapter;
	private Button btnBack;
	private int pageNum = 20;
	private int totalCount = 0;
	private int page = 1;
	private boolean isLoading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);
		
		initView();
		showPrgressDialog(this,"正在加载");
		getData();
	}
	
	private void initView(){
		imageLoaderOption(R.drawable.images_default);
		pullToRefreshGridView = (PullToRefreshGridView)findViewById(R.id.gv_image_list);
		pullToRefreshGridView.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				page = 1;
				getData();
			}
		});
		gridView = pullToRefreshGridView.getRefreshableView();
		imageCategories = new ArrayList<ImageCategory>();
		imageListAdapter= new ImageListAdapter(imageCategories,gridView);
		gridView.setAdapter(imageListAdapter);
		gridView.setOnScrollListener(onScrollListener);
		gridView.setOnItemClickListener(onItemClickListener);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClickListener);
	}
	
	private void getData(){
		if(!isLoading){
			isLoading = true;
			Map<String, String> params = new HashMap<String, String>();
			NetUtil netUtil = new NetUtil();
			netUtil.setDelegate(this);
			netUtil.setTag(Net_Tag.Net_Tag_Images_Category);
			
			params.put("p", page+"");
			netUtil.imageList(params);
		}
	}
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		NetUtil netUtil = (NetUtil)delegate;
		if(netUtil.getTag()==Net_Tag.Net_Tag_Images_Category){
			parseData(result);
		}
		
	}
	
	private void parseData(String result){
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			String info = jsonObject.getString("info");
			if(status==1){
				String imageCategory = jsonObject.getJSONObject("data").getString("imageCategory");
				String totalCountString = jsonObject.getJSONObject("data").getString("totalCount");
				totalCount = Integer.parseInt(totalCountString);
				Type type = new TypeToken<List<ImageCategory>>(){}.getType();
				List<ImageCategory> list = JsonUtil.json2Any(imageCategory, type);
				if(page==1){
					imageCategories.clear();
				}
				if(list!=null){
					imageCategories.addAll(list);
					pullToRefreshGridView.onRefreshComplete();
					imageListAdapter.notifyDataSetChanged();
					dismissDialog();
					isLoading = false;
				}
				
			}
			else{
				showToast(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		dismissDialog();
		isLoading = false;
	}
	
	
	private class ImageListAdapter extends BaseAdapter{
		private List<ImageCategory> list;
		private GridView gridView;
		private View firstView;
		private LayoutInflater layoutInflater;
		public ImageListAdapter(List<ImageCategory> list,GridView gridView){
			this.list = list;
			this.gridView = gridView;
			layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(position==0&&firstView!=null){
				return firstView;
			}
			convertView = layoutInflater.inflate(R.layout.item_image_list_cell, null);
			ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_image);
			TextView textView = (TextView)convertView.findViewById(R.id.tv_title);
			
			ImageCategory  imageCategory = list.get(position);
			String coverString = imageCategory.getIc_cover()==null?"":imageCategory.getIc_cover();
			String titleString = imageCategory.getIc_name()==null?"":imageCategory.getIc_name();
			imageLoader.displayImage(coverString, imageView, options);
			textView.setText(titleString);
			if(position==0){
				firstView = convertView;
			}
			return convertView;
			
		}
		
		
		
	}
	
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
	
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			ImageCategory imageCategory = imageCategories.get(position);
			Intent intent = new Intent(ImagesActivity.this, ImagesCategoryDetail.class);
			intent.putExtra("images", imageCategory);
			startActivity(intent);
		}
	};
	
	

}
