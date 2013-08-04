package com.gif.eting.svc;

import android.content.Context;

import com.gif.eting.dao.SettingDAO;
import com.gif.eting.dto.SettingDTO;
import com.gif.eting.util.SecureUtil;

public class SettingService {

	private SettingDAO settingDao;

	public SettingService(Context context) {
		this.settingDao = new SettingDAO(context);
	}

	// 비밀번호 저장
	public void savePassword(String pw) {
		SettingDTO setting = new SettingDTO();
		pw = SecureUtil.toSHA256(pw);

		setting.setKey("password");
		setting.setValue(pw);

		settingDao.open(); // 열고
		settingDao.inssetting(setting);
		settingDao.close(); // 닫고
	}

	// 비밀번호 체크
	public boolean checkPassword(String pw) {
		boolean isValid = false;
		pw = SecureUtil.toSHA256(pw); // 사용자가 입력한 비밀번호

		SettingDTO setting = new SettingDTO();

		settingDao.open(); // 열고
		setting = settingDao.getsettingInfo("password");
		settingDao.close(); // 닫고
		
		if(setting == null){
			isValid = true;	//설정된 비밀번호가 없으면 유효처리
		}else if (pw.equals(setting.getValue())) {
			isValid = true;
		}

		return isValid;
	}

}