package com.balsampearshz.wowyi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "wowyi.db";      
	private static final int DATABASE_VERSION = 1; 
	
	public DBHelper(Context context) {      
		super(context, DATABASE_NAME, null, DATABASE_VERSION);      
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String tableSQL = "";
		
		//用户登录表
		tableSQL = "CREATE TABLE IF NOT EXISTS wowyi_image_key_value" +
					"([wiwv_id] INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
					"[wiwv_filename] String(25)," +//文件名
					"[wiwv_path] String(50),"+//文件路径
					"[wiwv_type] String(16)"; //清空表时用
		db.execSQL(tableSQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}
	/**查询表
	 * @param sSql 查询语句
	 * @param arrSelectionArgs 查询条件里值
	 * @return 查询记录集
	 */
	public List<Map<String,Object>> queryTable(String sSql,String[] arrSelectionArgs) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		List<Map<String,Object>> lReturn = new ArrayList<Map<String, Object>>();
		try {
			db = getReadableDatabase();  
	    	cur = db.rawQuery(sSql, arrSelectionArgs);
	    	if (cur.getCount()>0) {
	        	cur.moveToFirst();
	        	for (int i = 0; i < cur.getCount(); i++) {
	        		Map<String,Object> map = new TreeMap<String, Object>();
	        		for(int j = 0; j < cur.getColumnCount(); j++) {
	        			if(cur.getString(j) != null && cur.getString(j).length() > 0) {
	        				map.put(cur.getColumnName(j),cur.getString(j));
	        			}
	        			else {
	        				map.put(cur.getColumnName(j),"");
	        			}
	        		}
	        		lReturn.add(map);
	        		cur.moveToNext();
	        	}
	    		cur.close();
	    		db.close();
	    	}
	    	else {
	    		cur.close();
	    		db.close();
	    		return null;
	    	}
		}
		catch(Exception e){
			cur.close();
			db.close();
			return null;
		}
    	return lReturn;
	}
	
	/**插入到数据表
	 * @param sTableName 数据表名
	 * @param arrColName 字段名
	 * @param arrField 字段内容
	 * @return 布尔类型是否操作成功
	 */
	public boolean insertTable(String sTableName,String[] arrColName,String[] arrField) {
		String sSql = "",sColName = "",sField = "";
		//参数不能为空
		if(sTableName.length()==0 && arrColName.length==0 && arrField.length==0) {
			return false;
		}
		//字段名和字段内容必须相同
		if(arrColName.length != arrField.length) {
			return false;
		}
		//字段名
		for(int i=0;i<arrColName.length;i++) {
			if(i==0) {
				sColName += arrColName[i];
			}
			else {
				sColName += "," + arrColName[i];
			}
		}
		//字段内容
		for(int i=0;i<arrField.length;i++) {
			if(i==0) {
				sField += "'" + arrField[i] + "'";
			}
			else {
				sField += ",'" + arrField[i] + "'";
			}
		}
		//组成SQL语句
		try {
			sSql = "insert into " + sTableName + "(" + sColName + ") values(" + sField + ")";
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sSql);
			db.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**修改数据表
	 * @param sTableName 数据表名
	 * @param arrColName 字段名
	 * @param arrField 字段内容
	 * @param sWhere 修改条件
	 * @return 布尔类型是否操作成功
	 */
	public boolean updateTable(String sTableName,String[] arrColName,String[] arrField,String sWhere) {
		String sSql = "",sField = "";
		//参数不能为空
		if(sTableName.length()==0 && arrColName.length==0 && arrField.length==0) {
			return false;
		}
		//字段名和字段内容必须相同
		if(arrColName.length != arrField.length) {
			return false;
		}
		//修改字段
		for(int i=0;i<arrColName.length;i++) {
			if(i==0) {
				sField += arrColName[i] + "='" + arrField[i] + "'";
			}
			else {
				sField += "," + arrColName[i] + "='" + arrField[i] + "'";
			}
		}
		//组成SQL语句
		try {
			sSql = "update " + sTableName + " set " + sField;
			if(sWhere != null) {
				if(sWhere.length()>0) {
					sSql += " where " + sWhere;
				}
			}
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sSql);
			db.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	/**删除数据
	 * @param sTableName 数据表名
	 * @param sWhere 删除条件
	 * @return 布尔类型是否操作成功
	 */
	public boolean deleteTable(String sTableName,String sWhere) {
		String sSql = "";
		if(sTableName.length()==0) {
			return false;
		}
		//组成SQL语句
		try {
			sSql = "delete from " + sTableName;
			if(sWhere != null) {
				if(sWhere.length()>0) {
					sSql += " where " + sWhere;
				}
			}
			SQLiteDatabase db = getWritableDatabase();
			db.execSQL(sSql);
			db.close();
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
	/**查询数据总数
	 * @param sTableName 数据表名
	 * @param sWhere 查询条件
	 * @return Integer类型总数
	 */
	public Integer countTable(String sSql,String[] arrSelectionArgs) {
		Cursor cur = null;
		SQLiteDatabase db = null;
		int count=0;
		try {
			db = getReadableDatabase();  
		    cur = db.rawQuery(sSql, arrSelectionArgs);
		    if (cur.getCount()>0) {
		    	count=cur.getCount();
		    }
		}
		catch(Exception e){
			cur.close();
			db.close();
			return count;
		}
		cur.close();
		db.close();
		return count;
	}

}
