package com.airyisea.bos.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	
public static String getPwd(String password) {
		
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			byte[] digest2 = digest.digest(password.getBytes());
			String md5pwd = "";
			for (byte b : digest2) {
				int i = b & 255;
				if(i >= 0 && i < 16) {
					md5pwd = md5pwd + "0" + Integer.toHexString(i);
				}else {
					md5pwd = md5pwd + Integer.toHexString(i);
				}
			}
			return md5pwd;
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new RuntimeException("没有该种加密算法");
		}
	}
	
}
