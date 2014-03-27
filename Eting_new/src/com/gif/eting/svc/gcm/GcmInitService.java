package com.gif.eting.svc.gcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.gif.eting.etc.Const;
import com.gif.eting.etc.HttpUtil;
import com.gif.eting.etc.Installation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmInitService {

	private final Context context;
	private GoogleCloudMessaging gcm;

	// 앱등록아이디
	private final String SENDER_ID = "112355150629";
	// 기기등록아이디
	public String regId;
	// 저장소
	private final SharedPreferences prefs;

	/**
	 * 생성자
	 *
	 * @param context
	 */
	public GcmInitService(Context context) {
		this.context = context;
		// 설정정보를 담기 위한 저장소 초기화
		prefs = context.getSharedPreferences("gcm_preferences",
				Context.MODE_PRIVATE);
	}

	/**
	 * GCM 초기화
	 * @throws GcmInitException
	 */
	public void initGcm() throws GcmInitException {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(context);
			regId = prefs.getString("registration_id", "0");
			if ("0".equals(regId)) {
				registerInBackground();
			}
		}
	}

	/**
	 * Google play servce 작동 확인
	 *
	 * @return
	 * @throws Exception
	 */
	public boolean checkPlayServices() throws GcmInitException {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			// 에러메세지
			String errorMsg = GooglePlayServicesUtil.getErrorString(resultCode);
			// 에러메세지를 서버로 전송
			new SendErrorTask().execute(errorMsg);
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				throw new GcmInitException(errorMsg);
				//Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show();
			}
			return false;
		}
		return true;
	}

	/**
	 * GCM을 등록한다. (기기)
	 */
	public void registerInBackground() {
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(context);
			}
			// GCM등록 후 기기 등록아이디를 가져온다.
			regId = gcm.register(SENDER_ID);
			// 등록 아이디를 저장한다.
			prefs.edit().putString("registration_id", regId).commit();
		} catch (IOException ex) {
			ex.printStackTrace();
			// TODO GCM등록 실패시 어떻게 해야하나??
			// TODO RegID가 등록되지 않으면 분명히 여길 드른다...
			new SendErrorTask().execute(ex.getMessage());
		}
	}

	/**
	 * 등록번호
	 *
	 * @return
	 */
	public String getRegId() {
		return prefs.getString("registration_id", "");
	}

	/**
	 * 에러메세지 전송
	 */
	public class SendErrorTask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {

			// 에러내용을 서버에 전송
			String url = Const.serverContext + "/sendNotifyComment";
			Map<String, String> param = new HashMap<String, String>();

			String msg = (String) params[0];

			//기기 고유 UUID
			String deviceUUID = Installation.id(context);

			param.put("msgId", "0");
			param.put("comment", deviceUUID.concat("_").concat(msg));

			return HttpUtil.doPost(url, param);
		}

	}
}
