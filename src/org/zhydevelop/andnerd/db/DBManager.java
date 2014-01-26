package org.zhydevelop.andnerd.db;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.zhydevelop.andnerd.bean.Category;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Given a PersistableResource, this class will take support loading/storing
 * it's data or requesting fresh data, as appropriate.
 */
public class DBManager {
	private SQLiteOpenHelper helper;
	private SQLiteDatabase db;

	public static enum DataType{Cache, Internal};

	public DBManager(Context context, DataType dataType) {
		//数据库类型
		if(dataType == DataType.Cache) {
			helper = new CacheDBHelper(context);
			db = helper.getWritableDatabase();
		} else {
			helper = new InternalDBHelper(context);
			db = helper.getReadableDatabase();
		}		
	}
	
	/*
	 * 插入搜索记录
	 */
	public void addRecentKeyword(String keyword) {
		String sql = "insert into [history](keyword, timestamp) values(?, datetime());";
		db.rawQuery(sql, new String[]{keyword});		
	}
	
	public List<String> getRecentKeywords() {
		String sql = "select distinct keyword from [history] order by timestamp desc limit 10;";
		Cursor cur = db.rawQuery(sql, null);

		//最多10个
		ArrayList<String> keywords = new ArrayList<String>(10);
		if(cur == null || !cur.moveToFirst()) return keywords;	
		
		do {
			keywords.add(cur.getString(0));
		} while (cur.moveToNext());
		
		keywords.trimToSize();
		cur.close();
		return keywords;
	}

	/*
	 * 查找符合条件的分类
	 */
	public List<Category> searchCategory(String prefix) {
		//过滤参数
		if(!Pattern.matches("^[A-Za-z]{1,2}[0-9.]*$", prefix))
			throw new InvalidParameterException("Invalid prefix");

		String sql = String.format(
				"select code, title from category where code like '%s_' or code like '%s._';",
				prefix, prefix);
		
		//开始查询
		Cursor cur = db.rawQuery(sql, null);
		ArrayList<Category> list = new ArrayList<Category>();
		if(cur == null || !cur.moveToFirst()) return list;
		
		//取搜索结果
		String code, title; int children;
		//性能优化
		int indexCode = cur.getColumnIndex("code"),
			indexTitle = cur.getColumnIndex("title"),
			indexChildren = cur.getColumnIndex("children");
		do {
			code = cur.getString(indexCode);
			title = cur.getString(indexTitle);
			children = cur.getInt(indexChildren);
			list.add(new Category(code, title, children));
		} while (cur.moveToNext());
		cur.close();
		list.trimToSize();        
		return list;
	}

	/**
	 * close database
	 */
	public synchronized void close() {
		helper.close();
		db.close();
	}
}