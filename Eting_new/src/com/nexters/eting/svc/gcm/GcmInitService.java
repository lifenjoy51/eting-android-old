package com.nexters.eting.svc.gcm;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nexters.eting.task.RegistrationTask;

public class GcmInitService {

	private Context context;
	private GoogleCloudMessaging gcm;

	// 앱등록아이디
	private String SENDER_ID = "112355150629";
	// 기기등록아이디
	public String regId;
	// 저장소
	private SharedPreferences prefs;
	// 등록여부
	public boolean isReg = false;

	/**
	 * 생성자
	 * 
	 * @param context
	 */
	public GcmInitService(Context context) {
		this.context = context;
		//설정정보를 담기 위한 저장소 초기화
		prefs = context.getSharedPreferences("gcm_preferences",
				Context.MODE_PRIVATE);
	}

	/**
	 * GCM 초기화
	 */
	public void initGcm() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(context);
			regId = prefs.getString("registration_id", "");
			if ("".equals(regId)) {
				registerInBackground();
			}
		}
	}

	/**
	 * Google play servce 작동 확인
	 * 
	 * @return
	 */
	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			// TODO GCM등록 에러 시 어떻게 처리하나? 서버에 정보 전송!!?!
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				// TODO 사용자가 복구 가능한 에러??
			}else{				
				// TODO 복구 불가능 에러??
			}
			return false;
		}
		return true;
	}

	/**
	 * GCM을 등록한다. (기기)
	 */
	public void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					// GCM등록 후 기기 등록아이디를 가져온다.
					regId = gcm.register(SENDER_ID);
					// 등록 아이디를 저장한다.
					prefs.edit().putString("registration_id", regId).commit();
					// 등록 성공
					isReg = true;
					// 서버에 등록
					new RegistrationTask().execute(regId, context);
				} catch (IOException ex) {
					ex.printStackTrace();
					// TODO GCM등록 실패시 어떻게 해야하나??
					// TODO RegID가 등록되지 않으면 분명히 여길 드른다...
				}
				return msg;
			}
		}.execute(null, null, null);
	}
}
