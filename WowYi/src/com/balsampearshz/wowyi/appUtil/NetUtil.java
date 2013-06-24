package com.balsampearshz.wowyi.appUtil;

import java.util.Map;

import android.os.AsyncTask;

import com.balsampearshz.wowyi.util.HttpUtil;
import com.balsampearshz.wowyi.util.StaticValue;



public class NetUtil implements NetReceiveDelegate{
	
	public enum Net_Tag
	{	

	}
	public NetReceiveDelegate delegate;
	public Net_Tag tag;
	//注册手机
	public void regUtil(Map<String, String> params){
		String url = StaticValue.URL+"/UserDevice/regUserDevice";
		connect(url, params);
	}
	
	//获取用户信息
	public void getUserProfile(Map<String, String> params){
		String userProfileUrl = StaticValue.URL+"/User/getMyProfile";
		connect(userProfileUrl, params);
	}
	//退出登录
	public void exitLogin(Map<String, String> params){
		String exitUrl = StaticValue.URL+"/User/unbind";
		connect(exitUrl, params);
	}

	
	private void connect(final String url, final Map<String, String> params) {
		
		new AsyncTask<String, Void, String>(){

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				
			}
			
			@Override
			protected String doInBackground(String... param) {
				
				try {
					String result = HttpUtil.doGet(url, params);
					if (!"".equals(result)) {
						
						return result;
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if(null != result){
					try {
						//DES解密
						if(StaticValue.NOCRYPT == 0){
							DES des = new DES();
							result = des.decrypt(result);
						}
						delegate.receiveSuccess(NetUtil.this,result);
					} catch (Exception e) {
						e.printStackTrace();
						delegate.receiveFail(NetUtil.this,"des crypt error!");
					}
					
				}
				else{
					delegate.receiveFail(NetUtil.this,"error");
				}
			}
			
		}.execute();
		
		
	}
	
	public NetReceiveDelegate getDelegate() {
		return delegate;
	}

	public void setDelegate(NetReceiveDelegate delegate) {
		this.delegate = delegate;
	}

	public Net_Tag getTag() {
		return tag;
	}

	public void setTag(Net_Tag tag) {
		this.tag = tag;
	}

	@Override
	public void receiveSuccess(NetReceiveDelegate delegate, String result) {
		
		
	}

	@Override
	public void receiveFail(NetReceiveDelegate delegate, String message) {
		
		
	}
	
	
}
