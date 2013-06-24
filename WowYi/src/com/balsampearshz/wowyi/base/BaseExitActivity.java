package com.balsampearshz.wowyi.base;

import java.util.Timer;
import java.util.TimerTask;

import android.view.KeyEvent;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

public class BaseExitActivity extends BaseActivity {

	private static Boolean isExit = false;
	private static Boolean hasTask = false;
	Timer tExit;
	TimerTask task;

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tExit = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				isExit = false;
				hasTask = true;
			}
		};
	};

	/**
	 * 按键返回
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!isExit) {
				isExit = true;
				Toast.makeText(this, "再按一次后退键退出程序!", Toast.LENGTH_SHORT).show();
				if(!hasTask){
					tExit.schedule(task, 2200, 2200);
				} 
			} else {
				MobclickAgent.onKillProcess( mContext );
				finish();
				System.exit(0);
			}
		}
		return true;
	}
}