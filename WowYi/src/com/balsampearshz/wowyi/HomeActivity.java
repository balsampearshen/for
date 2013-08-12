package com.balsampearshz.wowyi;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.base.BaseActivity;

public class HomeActivity extends BaseActivity implements NetReceiveDelegate {
	private Button btnArticle;
	private Button btnMusic;
	private Button btnImages;
	private Button btnMovie;
	private Button btnNovel;
	private Button btnSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		initView();
	}

	private void initView() {
		btnArticle = (Button) findViewById(R.id.btn_article);
		btnMusic = (Button) findViewById(R.id.btn_music);
		btnImages = (Button) findViewById(R.id.btn_images);
		btnMovie = (Button) findViewById(R.id.btn_movie);
		btnNovel = (Button) findViewById(R.id.btn_note);
		btnSetting = (Button) findViewById(R.id.btn_set);

		btnArticle.setOnClickListener(onClickListener);
		btnMusic.setOnClickListener(onClickListener);
		btnImages.setOnClickListener(onClickListener);
		btnMovie.setOnClickListener(onClickListener);
		btnNovel.setOnClickListener(onClickListener);
		btnSetting.setOnClickListener(onClickListener);
	}

	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		// TODO Auto-generated method stub

	}

	private OnClickListener onClickListener = new OnClickListener() {
		Intent intent;

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.btn_article:
				intent = new Intent(HomeActivity.this, ArticleActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_music:
				intent = new Intent(HomeActivity.this, MusicActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_images:
				intent = new Intent(HomeActivity.this, ImagesActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_movie:
				intent = new Intent(HomeActivity.this, MovieActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_note:
				intent = new Intent(HomeActivity.this, NovelActivity.class);
				startActivity(intent);
				break;
			case R.id.btn_set:
				intent = new Intent(HomeActivity.this, SettingActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}

		}
	};

}
