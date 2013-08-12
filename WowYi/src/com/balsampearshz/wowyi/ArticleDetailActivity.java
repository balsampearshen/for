package com.balsampearshz.wowyi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.NetUtil.Net_Tag;
import com.balsampearshz.wowyi.base.BaseActivity;
import com.balsampearshz.wowyi.bean.Article;
import com.balsampearshz.wowyi.bean.ArticleComment;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.balsampearshz.wowyi.pulltorefresh.PullToRefreshListView;
import com.balsampearshz.wowyi.util.JsonUtil;
import com.google.gson.reflect.TypeToken;

public class ArticleDetailActivity extends BaseActivity implements NetReceiveDelegate {
	private PullToRefreshListView pullToRefreshListView;
	private ListView listView;
	private List<ArticleComment> articleComment;
	private Article article;
	private TextView from;
	private TextView author;
	private TextView original;
	private Button btnBack;
	private WebView articleContentWebView;
	private ArticleCommentAdapter articleCommentAdapter;
	
 	private int page = 1;
	private int totalCount = 0;
	private int pageSize = 20;
	private boolean isloading = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_article_detail);
		
		initView();
		getData();
	}
	
	private void initView(){
		article = (Article) getIntent().getExtras().getSerializable("article");
		btnBack = (Button)findViewById(R.id.btn_back);
		btnBack.setOnClickListener(onClickListener);
		LayoutInflater layoutInflater = getLayoutInflater();
		View view = layoutInflater.inflate(R.layout.inflate_articledetail_header, null);
		from = (TextView) view.findViewById(R.id.tv_from);
		author = (TextView)view.findViewById(R.id.tv_author);
		original = (TextView)view.findViewById(R.id.tv_original_url);
		articleContentWebView = (WebView)view.findViewById(R.id.wv_article_content);
		
		pullToRefreshListView = (PullToRefreshListView)findViewById(R.id.lv_all_comments);
		listView = pullToRefreshListView.getRefreshableView();
		listView.addHeaderView(view);
		setHeaderView();
		
		pullToRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page =1;
				getData();
			}
		});
		articleComment = new ArrayList<ArticleComment>();
		articleCommentAdapter = new ArticleCommentAdapter(this, articleComment, listView);
		listView.setAdapter(articleCommentAdapter);
		listView.setOnScrollListener(scrollListener);
		
	}
	private void setHeaderView(){
		from.setText(article.getAr_name()==null?"":"来源:"+article.getAr_name());
		author.setText(article.getA_author()==null?"":",原文作者:"+article.getA_author());
		original.setText(article.getA_original_url()==null?"":"原文地址:"+article.getA_original_url());
		String content = article.getA_content()==null?"":article.getA_content();
		if(!"".equals(content)){
			content = "<html>\n"+
		            "<head>\n"+
		            "<style type=\"text/css\">\n"+
		            "a {color:%@; text-decoration: none;}\n"+
		            "body {font-family: \"%@\"; font-size:15pt; color:#745f3f; font-style: normal;text-indent:2em;line-height:15pt;}\n"+
		            "</style>\n"+
		            "</head>\n"+
		            "<body>"+content+"</body>\n"+
		            "</html>";
			articleContentWebView.setVisibility(View.VISIBLE);
			articleContentWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
		}
	}
	
	private void getData(){
		if(!isloading){
			isloading = true;
			NetUtil netUtil = new NetUtil();
			netUtil.setTag(Net_Tag.Tag_Article_Comment_List);
			netUtil.setDelegate(this);
			Map<String, String> params = new HashMap<String, String>();
	 		params.put("a_id", article.getA_id());
	 		netUtil.articleCommentList(params);
		}
	}
	
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		NetUtil netUtil = (NetUtil)delegate;
		if(netUtil.getTag()==Net_Tag.Tag_Article_Comment_List){
			try {
				JSONObject jsonObject = new JSONObject(result);
				totalCount = Integer.valueOf(jsonObject.getJSONObject("data").getString("totalCount"));
				String articleComments = jsonObject.getJSONObject("data").getString("articleComments");
				Type type = new TypeToken<List<ArticleComment>>(){}.getType();
				List<ArticleComment> list = JsonUtil.json2Any(articleComments, type);
				if(page==1){
					articleComment.clear();
				}
				articleComment.addAll(list);
				articleCommentAdapter.notifyDataSetChanged();
				pullToRefreshListView.onRefreshComplete();
				isloading = false;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
		}
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		
	}
	
	private class ArticleCommentAdapter extends BaseAdapter{
		private LayoutInflater layoutInflater;
		private List<ArticleComment> list;
		private ListView listView;
		public ArticleCommentAdapter(Context context,List<ArticleComment> list,ListView listView){
			layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
			this.list = list;
			this.listView = listView;
		}

		@Override
		public int getCount() {
			if(list.isEmpty()){
				return 1;
			}
			else if(totalCount<list.size()){
				return list.size()+1;
			}
			else{
				return list.size();
			}
		}

		@Override
		public Object getItem(int position) {
			try {
				return list.get(position);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ArticleComment articleComment = (ArticleComment) getItem(position);
				if(articleComment==null){
					if(list.isEmpty()){
						convertView = layoutInflater.inflate(R.layout.item_comment_empty, null);
						return convertView;
				
					}
				}
				ViewHolder viewHolder=null;
				if(convertView!=null){
					viewHolder = (ViewHolder) convertView.getTag();
				}
				if(viewHolder==null){
					viewHolder = new ViewHolder();
					convertView = layoutInflater.inflate(R.layout.item_article_comment, null);
					viewHolder.nickName = (TextView)convertView.findViewById(R.id.tv_comment_nickname);
					viewHolder.floor = (TextView)convertView.findViewById(R.id.tv_floor);
					viewHolder.content = (TextView)convertView.findViewById(R.id.tv_comment_content);
					convertView.setTag(viewHolder);
				}
				viewHolder.nickName.setText(articleComment.getU_nickname()==null?"匿名":articleComment.getU_nickname());
				if(position==0){
					viewHolder.floor.setText("沙发");
				}
				else if(position==1){
					viewHolder.floor.setText("板凳");
				}
				else if(position==2){
					viewHolder.floor.setText("地板");
				}
				else{
					viewHolder.floor.setText(""+position+1);
				}
				viewHolder.content.setText(articleComment.getAco_content()==null?"":articleComment.getAco_content());
				
				return convertView;
				
			
		}
		
		class ViewHolder{
			
			TextView nickName;
			TextView floor;
			TextView content;
		}
		
	}
	
	/**
	 * 上拉更多
	 */
	private OnScrollListener scrollListener = new OnScrollListener() {
		private int scrollState = 0;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

			this.scrollState = scrollState;
		}
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			if ((scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || scrollState == OnScrollListener.SCROLL_STATE_FLING) && !isloading) {
				if (firstVisibleItem + visibleItemCount > totalItemCount - 2 && totalCount > page * pageSize) {
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
