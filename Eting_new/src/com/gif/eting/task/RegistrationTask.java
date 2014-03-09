package com.gif.eting.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.gif.eting.etc.Const;
import com.gif.eting.etc.HttpUtil;
import com.gif.eting.etc.Installation;
import com.gif.eting.svc.gcm.GcmInitService;

/**
 * GCM을 등록하고, 서버에 기기정보를 등록한다.
 *
 * @author lifenjoy51
 *
 */
public class RegistrationTask extends AsyncTask<Object, String, String> {
	// Context
	private Context context;
	private String regId;

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {


		this.context = (Context) params[0]; // 파라미터 두번째값 context

		//GCM초기화!!!
		//TODO GCM을 등록한 이후 서버에 기기를 등록하려면 이곳에서 GCM을 초기화 해야한다.
		GcmInitService gcmInitService = new GcmInitService(context);
		gcmInitService.initGcm();

		regId = gcmInitService.getRegId();

		//기기 고유 UUID
		String deviceUUID = Installation.id(context);

		// 서버에 전송할 파라미터 조립
		String param = "device_uuid=" + deviceUUID + "&reg_id=" + regId;
		String urlStr = Const.serverContext + "/registration";

		// Http전송
		String response = HttpUtil.doPost(urlStr, param);

		return response;
	}

	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		try {
			JSONObject json = new JSONObject(result);
			String deviceId = json.getString("device_id");

			// 기기를 등록하면 등록된 기기값을 저장한다.
			SharedPreferences prefs = context.getSharedPreferences("eting",
					Context.MODE_PRIVATE);
			prefs.edit().putString("device_id", deviceId).commit();

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
