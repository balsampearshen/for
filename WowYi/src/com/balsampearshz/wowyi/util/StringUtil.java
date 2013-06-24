package com.balsampearshz.wowyi.util;
/**
 * 字符串操作类
 * 
 * @author ylx
 * 
 */
public class StringUtil {
	/**
	 * 不为空 返回真
	 * 
	 * @param str
	 *            字符串
	 * @return 如果字符串不为空且长度大于1 返回真 ，其他返回假
	 */
	public static boolean isNotBlank(String str) {
		return str != null && !(null==str||str.equals(""));
		//str.isEmpty()
	}

	/**
	 * 如果为空 返回真
	 * 
	 * @param str
	 *            字符串
	 * @return 如果为空或长度等于零，返回真，其他返回假
	 */
	public static boolean isBlank(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * 去掉空格不为空 返回真
	 * 
	 * @param str
	 *            字符串
	 * @return 如果字符串不为空且去掉空格长度大于1 返回真 ，其他返回假
	 */
	public static boolean isNotTrimBlank(String str) {
		return str != null && !(null==str.trim() || str.trim().equals(""));
	}

	/**
	 * 去掉空格为空返回真
	 * 
	 * @param str
	 *            字符串
	 * @return 如果字符串为空或去掉空格长度为0,返回真，其他返回假
	 */
	public static boolean isTrimBlank(String str) {
		return str == null || (null==str.trim() || str.trim().equals(""));
	}

	/**
	 * 空串
	 */
	public final static String EMPTY = "";

	/**
	 * 首字母大写
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 首字母大写的字符串
	 */
	public static String capFirstUpperCase(String str) {
		if (isBlank(str)) {
			return str;
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);

	}

	/**
	 * 首字母小写
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 首字母小写的字符串
	 */
	public static String capFirstLowerCase(String str) {
		if (isBlank(str)) {
			return str;
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}


	
}
