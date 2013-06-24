package com.balsampearshz.wowyi.util;

import java.lang.reflect.Type;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 *  GSON 工作类，它是用占位字符，反向来做的，如果用泛型从外面传类型过来，则会报错，因为JAVA的泛型是运行时擦除的<br />
 *  例如 public static <T> T str2json (String str) 这种写法，运行时就会出错<br />
 * @author crazy_cabbage
 *
 */
public class JsonUtil {
	public static  Map<String, Map<String, String[]>>   json2Map(String json) {
		if (StringUtil.isTrimBlank(json)) {
			return null;
		}
		Type type =new TypeToken<Map<String, Map<String, String[]>>>(){}.getType();
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}
	/**
	 * 到任何对象
	 * @param json
	 * @return
	 */
	public static <T> T json2Any(String json,Type type) {
		if (StringUtil.isTrimBlank(json)) {
			return null;
		}
		Gson gson = new Gson();
		return gson.fromJson(json, type);
	}
	/**
	 * 把JSON转化为对象
	 * @param json json对象
	 * @return 返回的对象
	 */
	public static <T> T json2Obj(String json ,Class<T> clazz){
		if (StringUtil.isTrimBlank(json)) {
			return null;
		}
		Gson gson = new Gson();
		return gson.fromJson(json, clazz);
		
	}
	/**
	 * 把对象转成JSON对象
	 * @param object 对象
	 * @return json字符串
	 */
	public static String  toJson(Object object){
		Gson gson = new Gson();
		return gson.toJson(object);
	}
}
