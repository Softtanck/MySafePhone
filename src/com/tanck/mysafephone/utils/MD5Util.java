package com.tanck.mysafephone.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public static String md5Password(String password) {
		try {
			// ��ϢժҪ��
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] result = messageDigest.digest(password.getBytes());
			StringBuffer buffer = new StringBuffer();
			for (byte s : result) {
				int tmp = s & 0xff;// ��1111111����������
				String t = Integer.toHexString(tmp);// ת��Ϊ16���Ƶ��ַ���
				if (t.length() == 1) {// �������8λ�Ͳ�0
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
