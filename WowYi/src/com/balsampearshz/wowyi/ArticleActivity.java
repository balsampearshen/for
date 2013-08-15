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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.balsampearshz.wowyi.bean.Article;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshListView;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class ArticleActivity extends BaseActivity implements NetReceiveDelegate {
	private PullToRefreshListView pullToRefreshListView;
	private ArticleListAdapter articleListAdapter;
	private ListView listView;
	private List<Article> articlesList;
	private Button btnBack;
	private int pageNum = 20;
	private int totalCount = 0;
	private int page = 1;
	private boolean isLoading = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article);
		
		initView();
		showPrgressDialog(this, "正在加载，请稍候");
		getData();
		imageLoaderOption(-1);
	}
	
	private void initView(){
		articlesList = new ArrayList<Article>();
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.lv_article_list); 
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				getData();
			}
		});
		articleListAdapter = new ArticleListAdapter(this, listView, articlesList);
		listView = pullToRefreshListView.getRefreshableView();
		listView.setOnScrollListener(onScrollListener);
		listView.setAdapter(articleListAdapter);
		listView.setOnItemClickListener(onItemClickListener);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClickListener);
		
	}
	/**
	 * 获取数据
	 */
	private void getData(){
		if(!isLoading){
			isLoading = true;
			Map<String, String> params = new HashMap<String, String>();
			params.put("p", String.valueOf(page));
			NetUtil netUtil = new NetUtil();
			netUtil.setTag(Net_Tag.Tag_Article_List);
			netUtil.setDelegate(this);
			netUtil.articleList(params);
			
		}
	}
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		parseData(result);
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		isLoading = false;
		dismissDialog();
	}
	/**
	 * 解析数据
	 */
	private void parseData(String result){
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status = jsonObject.getInt("status");
			if(status==1){
				String data = jsonObject.getJSONObject("data").getString("articleList");
				String totalCountString = jsonObject.getJSONObject("data").getString("totalCount");
				totalCount = Integer.valueOf(totalCountString);
				Type type = new TypeToken<List<Article>>(){}.getType();
				ArrayList<Article> list = JsonUtil.json2Any(data, type);
				if(page==1){
					articlesList.clear();
				}
				if(null!=articlesList){
					articlesList.addAll(list);
					articleListAdapter.notifyDataSetChanged();
					pullToRefreshListView.onRefreshComplete();
					dismissDialog();
					isLoading = false;
				}
					
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	private class ArticleListAdapter extends ArrayAdapter<Article>{
		private LayoutInflater inflater;
		private List<Article> list;
		
		public ArticleListAdapter(Context context, ListView listView,List<Article> list) {
			super(context, 0, list);
			inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
			this.list = list;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(null==convertView){
				viewHolder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item_article_list, null);
				viewHolder.mainPic = (ImageView) convertView.findViewById(R.id.iv_article_main_pic);
				viewHolder.title = (TextView)convertView.findViewById(R.id.tv_article_title);
				viewHolder.desc = (TextView)convertView.findViewById(R.id.tv_article_desc);
				viewHolder.viewCount = (TextView)convertView.findViewById(R.id.tv_article_view_count);
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			Article article = list.get(position);
			String imageUrl = article.getA_main_pic();
			viewHolder.title.setText(null==article.getA_title()?"":article.getA_title());
			viewHolder.desc.setText(null==article.getA_desc()?"":article.getA_desc());
			System.out.println(imageUrl);
			imageLoader.displayImage(imageUrl, viewHolder.mainPic, options);
			viewHolder.viewCount.setText("浏览数:1000");
			return convertView;
			
		}
		class ViewHolder{
			ImageView mainPic;
			TextView title;
			TextView desc;
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
  private OnItemClickListener onItemClickListener = new OnItemClickListener() {

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Article article = articlesList.get(position);
		Intent intent = new Intent(ArticleActivity.this, ArticleDetailActivity.class);
		intent.putExtra("article", article);
		startActivity(intent);
	}
  };

}
