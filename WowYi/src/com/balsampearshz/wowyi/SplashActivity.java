package com.balsampearshz.wowyi;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.balsampearshz.wowyi.appUtil.AppUtil;
import com.balsampearshz.wowyi.appUtil.NetReceiveDelegate;
import com.balsampearshz.wowyi.appUtil.NetUtil;
import com.balsampearshz.wowyi.appUtil.UserUtil;
import com.balsampearshz.wowyi.util.AsyncImageSaveLoader;
import com.balsampearshz.wowyi.util.ImageSaveQueue;
import com.balsampearshz.wowyi.util.StaticValue;


public class SplashActivity extends Activity implements NetReceiveDelegate{

	private static final String TAG = "SplashActivity---->";
	Handler mhandler;
	Runnable mRunnable;
	
	private Button retryButton;
	
	private boolean canRemoveFromSuperView = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		mhandler = new Handler();
		mRunnable = new Runnable() {
			@Override
			public void run() {
				if(canRemoveFromSuperView){
					finishSplashView();
				}
			}
		};
		mhandler.postDelayed(mRunnable, 1000);
		
		retryButton = (Button)findViewById(R.id.btn_retry);
		retryButton.setOnClickListener(clickListener);

		AppUtil.initApp(this);
		regDevice();
		initLocalImage();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.bottom_view_stay, R.anim.bottom_view_stay);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

	    return true;
	}
	
	/**
	 * 初始化本地图片
	 */
	private void initLocalImage() {
		long start = System.currentTimeMillis();
		String path = StaticValue.getPicCachePath();
		ImageSaveQueue<String> imageSaveQueue = ImageSaveQueue.instance();
		imageSaveQueue.clear();
		File file = new File(path);
		// 如果是文件夹的话
		if (file.isDirectory()) {
			// 返回文件夹中有的数据
			File[] files = file.listFiles();
			// 先判断下有没有权限，如果没有权限的话，就不执行了
			if (null == files)
				return;
			int length = files.length;
			for (int i = 0; i < length; i++) {
				// 进行文件的处理
				String filePath = files[i].getAbsolutePath();
				// 文件名
				String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
				// 添加
				imageSaveQueue.offer(fileName, getPath(fileName));
				
			}
		}
		long end = System.currentTimeMillis();
		Log.d("time", (end - start) + "");
	}

	private String getPath(String fileName) {
		String path = StaticValue.getPicCachePath() + "/" + fileName;
		return path;
	}
	
	@SuppressWarnings("deprecation")
	private void regDevice() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String phoneInfo = "Product: " + android.os.Build.PRODUCT; 
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI; 
        phoneInfo += ", TAGS: " + android.os.Build.TAGS; 
        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE; 
        phoneInfo += ", MODEL: " + android.os.Build.MODEL; 
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK; 
        phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE; 
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE; 
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY; 
        phoneInfo += ", BRAND: " + android.os.Build.BRAND; 
        phoneInfo += ", BOARD: " + android.os.Build.BOARD; 
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT; 
        phoneInfo += ", ID: " + android.os.Build.ID; 
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER; 
        phoneInfo += ", USER: " + android.os.Build.USER;
        phoneInfo += ", HOST: " + android.os.Build.HOST;
        phoneInfo += ", DeviceId: " + tm.getDeviceId();
        phoneInfo += ", OperatorName:" + tm.getNetworkOperatorName();
        phoneInfo += ", User:" +android.os.Build.USER;
        Log.e("phone info", phoneInfo);
        
		if("".equals(UserUtil.getMySession(this)))
	    {
			HashMap<String, String> params = new HashMap<String, String>();
			if(AppUtil.isTabletDevice(this)){
				params.put("device_type", URLEncoder.encode("android pad"));
			}
			else{
				params.put("device_type", URLEncoder.encode("android phone"));
			}
			params.put("device_model", URLEncoder.encode(android.os.Build.MODEL));
			params.put("os_type", "android");
			params.put("os_version", URLEncoder.encode(android.os.Build.VERSION.RELEASE));
			params.put("device_unique_id", URLEncoder.encode(tm.getDeviceId()));
//			parmas.put("device_name",)
	        
			NetUtil util = new NetUtil();
			util.setDelegate(this);
			util.regUtil(params);
	    }
		else
		{
			canRemoveFromSuperView = true;
		}
	}
	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		
		try {
			JSONObject jsonObject = new JSONObject(result);
			String session = jsonObject.getJSONObject("data").getString("sessionkey");
			UserUtil.setMySession(this, session);
			
			String userProfile = jsonObject.getJSONObject("data").getString("user");
			UserUtil.setMyUserProfile(this, userProfile);
			
			String userDevice = jsonObject.getJSONObject("data").getString("device");
			UserUtil.setMyUserDevice(this, userDevice);
			
			finishSplashView();
		} catch (JSONException e) {
			e.printStackTrace();
			retryButton.setVisibility(View.VISIBLE);
		}
	}
	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		retryButton.setVisibility(View.VISIBLE);
	}
	
	private OnClickListener clickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			regDevice();
			retryButton.setVisibility(View.INVISIBLE);
		}
	};
	
	
	private void finishSplashView(){
		
		Intent intent = new Intent(SplashActivity.this,
				MainActivity.class);
		startActivity(intent);
		SplashActivity.this.finish(); // 结束启动动画界面
	}
}
