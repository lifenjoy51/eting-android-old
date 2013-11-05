package com.gif.eting.svc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;
import com.gif.eting.util.Util;

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
	 * @param callback
	 */
	public RegistrationTask(){
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			this.regId = (String) params[0]; 	//파라미터 첫번째값 regId.
			this.context = (Context) params[1];	//파라미터 두번째값 context
			String phoneId = Installation.id(context);	//기기 고유값
			String param = "phone_id=" + phoneId +"&reg_id=" +regId;	//서버에 전송할 파라미터 조립
			String urlStr = Util.serverContext+"/registration";
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			//System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}	
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		Log.i("RegistrationTask response", result);	//응답 확인

		if("UnknownHostException".equals(result)){
			//등록실패
			Log.i("RegistrationTask Fail", result);
			new RegistrationTask().execute(regId, context);
		}else if("HttpUtil_Error".equals(result)){
			//등록실패
			Log.i("RegistrationTask Fail", result);
			new RegistrationTask().execute(regId, context);
		}else{
			//등록성공
		}
	}

}
