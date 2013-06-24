package com.balsampearshz.wowyi.appUtil;

public interface NetReceiveDelegate {
	
	public void receiveSuccess(NetReceiveDelegate delegate, String result);
	public void receiveFail(NetReceiveDelegate delegate, String message);
}
