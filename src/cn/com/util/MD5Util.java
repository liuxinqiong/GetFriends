package cn.com.util;

import java.security.*;

public class MD5Util {
	private static MessageDigest digest = null;

	public MD5Util() {
	}

	// ------------------------------------------------------------------------------
	/**
	 * 对字符串进行HASH(MD5)加密，注意，加密结果不可逆
	 * 
	 * @param data
	 *            待加密数据
	 * @return 加密结果
	 */
	public synchronized static final String hash(String data) {
		if (digest == null) {
			try {
				digest = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException nsae) {
				System.err.println("Failed to load the MD5 MessageDigest. ");
				nsae.printStackTrace();
			}
		}
		if (digest != null) {
			digest.update(data.getBytes());
			return encodeHex(digest.digest());
		}
			return null;
	}

	// ------------------------------------------------------------------------------
	/**
	 * 将数组转换成16进制字符串
	 * 
	 * @param bytes
	 * @return
	 */
	public static final String encodeHex(byte[] bytes) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if (((int) bytes[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString((int) bytes[i] & 0xff, 16));
		}
		return buf.toString();
	}
	// ------------------------------------------------------------------------------
}