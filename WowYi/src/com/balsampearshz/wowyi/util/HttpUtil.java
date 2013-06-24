package com.balsampearshz.wowyi.util;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/**
 * http传输方法
 * 
 * @author ylx
 * 
 */
public class HttpUtil {
	/**
	 * post方法
	 * 
	 * @param validateUrl
	 *            请求地址
	 * @param params
	 *            请求参数，可以为空
	 * @return 返回请求的结果
	 */
	public static String doPost(String validateUrl, Map<String, String> params) {
		String strResult = "";
		HttpParams httpParameters;
		HttpEntity httpentity;
		int stateCode = 0;
		if (params == null) {
			params = new HashMap<String, String>();
		}
		StringBuilder sb = new StringBuilder(validateUrl);

		try {
			List<NameValuePair> postParams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				postParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			sb.deleteCharAt(sb.length() - 1);
			httpParameters = new BasicHttpParams();
			// 设置httphost的time out时间
			HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
			HttpConnectionParams.setSoTimeout(httpParameters, 5000);
			HttpPost httpPost = new HttpPost(validateUrl);

			// 传入List列表
			httpentity = new UrlEncodedFormEntity(postParams, "utf-8");

			httpPost.setEntity(httpentity);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);

			HttpResponse httpResponse = httpClient.execute(httpPost);

			stateCode = httpResponse.getStatusLine().getStatusCode();
			if (stateCode == HttpStatus.SC_OK) {
				strResult = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strResult;
	}

	/**
	 * get方法
	 * 
	 * @param validateUrl
	 *            请求地址
	 * @param params
	 *            请求参数，可以为空
	 * @return 返回请求的结果
	 */
	public static String doGet(String validateUrl, Map<String, String> params) throws Exception {
		String strResult = "";
		StringBuilder sb = new StringBuilder(validateUrl);
		if(sb.indexOf("?")==-1){
			sb.append("?");
		}
		else{
			sb.append("&");
		}
		int stateCode = 0;
		if (params == null) {
			params = new HashMap<String, String>();
		}
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey()).append('=').append(entry.getValue()).append('&');
		}
		sb.deleteCharAt(sb.length() - 1);

		HttpGet httpRequest = new HttpGet(sb.toString());
		HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
		stateCode = httpResponse.getStatusLine().getStatusCode();

		if (stateCode == HttpURLConnection.HTTP_OK) {// 返回成功200判断
			strResult = EntityUtils.toString(httpResponse.getEntity());
		}

		return strResult;
	}

}
