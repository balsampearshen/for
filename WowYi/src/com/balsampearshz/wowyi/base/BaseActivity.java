package com.balsampearshz.wowyi.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.balsampearshz.wowyi.R;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {
	private ProgressDialog dialog = null;
	protected Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		MobclickAgent.updateOnlineConfig(this);
	}
	
	public void showToast(String content) {
		Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
	}
	
	public void showPrgressDialog(Context context, String text) {
		dialog = new ProgressDialog(context);
		dialog.setMessage(text);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.show();
	}

	public void dismissDialog() {
		try {
			dialog.dismiss();
		} catch (Exception e) {

		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void back(){
		finish();
	}
}
