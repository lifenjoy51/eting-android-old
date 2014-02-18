package com.nexters.eting.svc;

import android.content.Context;
import android.content.SharedPreferences;

import com.nexters.eting.etc.SecureUtil;
import com.nexters.eting.etc.Util;

/**
 * 암호관련 로직 처리
 * 
 * @author lifenjoy51
 * 
 */
public class PasswordService {

	private String key = "password";
	private SharedPreferences pref;

	/**
	 * 초기화
	 * @param context
	 */
	public PasswordService(Context context) {
		pref = context.getSharedPreferences("eting", Context.MODE_PRIVATE);
	}

	/**
	 * 비밀번호 저장
	 * 
	 * @param pw
	 */
	public void savePassword(String pw) {
		pw = SecureUtil.toSHA256(pw);

		String chk = pref.getString(key, "");
		if (Util.isEmpty(chk)) {
			pref.edit().putString(key, pw).commit();
		} else {
			pref.edit().remove(key);
			pref.edit().putString(key, pw).commit();
		}
	}

	/**
	 * 비밀번호 체크
	 * 
	 * @param pw
	 * @return
	 */
	public boolean checkPassword(String pw) {
		boolean isValid = false;
		pw = SecureUtil.toSHA256(pw); // 사용자가 입력한 비밀번호

		String chk = pref.getString(key, "");
		if (Util.isEmpty(chk)) {
			isValid = true; // 설정된 비밀번호가 없으면 유효처리
		} else if (pw.equals(chk)) {
			isValid = true;
		}

		return isValid;
	}

	/**
	 * 패스워드 유무 확인
	 * 
	 * @return
	 */
	public boolean isPassword() {
		String chk = pref.getString(key, "");
		if (Util.isEmpty(chk)) {
			return false;
		} else {
			return true;
		}
	}

}
