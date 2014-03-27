package com.gif.eting.etc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

/**
 * 암호화처리
 *
 * @author lifenjoy51
 *
 */
public class SecureUtil {

	/**
	 * SHA256 해쉬값을 리턴한다.
	 *
	 * @param str
	 * @return
	 */
	public static String toSHA256(String str) {
		String SHA = "";
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(str.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			SHA = sb.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			SHA = null;
		}
		return SHA;
	}

	public static String encode(String in) {
		byte[] b = Base64.encode(in.getBytes(), Base64.URL_SAFE);
		String out = new String(b);
		return out;
	}

	public static String decode(String in) {
		byte[] b = Base64.decode(in.getBytes(), Base64.URL_SAFE);
		String out = new String(b);
		return out;
	}

}
