package com.tanck.mysafephone.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * �绰�����ѯ
 * 
 * @author Administrator
 * 
 */
public class NumberQueryUtils {
	private static String path = "data/data/com.tanck.mysafephone/files/address.db";

	/**
	 * ���ݺ����ѯ������
	 * 
	 * @param number
	 * @return
	 */
	public static String numberQuery(String number) {
		// �����жϺ����Ƿ�Ϸ�
		if (number.matches("^1[34568]\\d{9}$")) {
			// �����ݿ��ļ�
			SQLiteDatabase database = SQLiteDatabase
					.openDatabase(path, null, 0);
			// ִ�в�ѯ���
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });

			// ����һ����¼
			while (cursor.moveToNext()) {
				// ��ȡ����ַ��
				String laction = cursor.getString(0);
				number = laction;
				System.out.println("��ѯ����-------------->" + number);
			}
			return number;
		}
		return null;
	}
}
