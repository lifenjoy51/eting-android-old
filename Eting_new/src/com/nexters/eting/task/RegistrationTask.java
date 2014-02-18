package com.nexters.eting.task;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.nexters.eting.etc.Const;
import com.nexters.eting.etc.HttpUtil;
import com.nexters.eting.etc.Installation;

/**
 * 작성한 이야기를 서버에 전송하는 작업
 * AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 * 
 * @author lifenjoy51
 *
 */
public class RegistrationTask extends AsyncTask<Object, String, String> {
	/**
	 * Context
	 */
	private Context context;
	private String regId;

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			this.regId = (String) params[0]; 	//파라미터 첫번째값 regId.
			this.context = (Context) params[1];	//파라미터 두번째값 context
			String phoneId = Installation.id(context);	//기기 고유값
			String param = "phone_id=" + phoneId +"&reg_id=" +regId;	//서버에 전송할 파라미터 조립
			String urlStr = Const.serverContext+"/registration";
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			////System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}	
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {
		//System.out.println(result);
		
		try {
			JSONObject json = new JSONObject(result);
			String deviceId = json.getString("device_id");
			
			//TODO 기기를 등록하면 등록된 기기값을 저장한다.
			SharedPreferences prefs = context.getSharedPreferences("eting",
					Context.MODE_PRIVATE);
			prefs.edit().putString("device_id", deviceId).commit();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
