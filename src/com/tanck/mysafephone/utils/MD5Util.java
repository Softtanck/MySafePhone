package com.tanck.mysafephone.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static String md5Password(String password) {
		try {
			// 消息摘要器
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] result = messageDigest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte s : result) {
				int tmp = s & 0xff;// 与1111111进行与运算
				String t = Integer.toHexString(tmp);// 转换为16进制的字符串
				if (t.length() == 1) {// 如果不足8位就补0
					buffer = buffer.append("0");
				}
				buffer.append(t);
			}
			return buffer.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
	}
}
