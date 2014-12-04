package com.tanck.mysafephone.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 电话号码查询
 * 
 * @author Administrator
 * 
 */
public class NumberQueryUtils {
	private static String path = "data/data/com.tanck.mysafephone/files/address.db";

	/**
	 * 根据号码查询归属地
	 * 
	 * @param number
	 * @return
	 */
	public static String numberQuery(String number) {
		// 正则判断号码是否合法
		if (number.matches("^1[34568]\\d{9}$")) {
			// 打开数据库文件
			SQLiteDatabase database = SQLiteDatabase
					.openDatabase(path, null, 0);
			// 执行查询语句
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });

			// 有下一条记录
			while (cursor.moveToNext()) {
				// 获取到地址后
				String laction = cursor.getString(0);
				number = laction;
				System.out.println("查询到了-------------->" + number);
			}
			return number;
		}
		return null;
	}
}
