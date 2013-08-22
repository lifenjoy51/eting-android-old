package com.gif.eting.svc.task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.Config;
import com.gif.eting.util.HttpUtil;

public class ReceiveStampTask extends AsyncTask<String, String, String> {
	private AsyncTaskCompleteListener<String> callback;
	
	public ReceiveStampTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... params) {

			String urlStr = Config.serverContext+"/getStamp";
			String param = "storyId=" + params[0];	//파라미터 첫번째값 storyId
			
			return HttpUtil.doPost(urlStr, param);
	}
	

	@Override
	protected void onPostExecute(String result) {

		Log.i("json response", result);	//응답확인
		
		StringBuffer stamps = new StringBuffer();
		
		try {
			JSONObject json = new JSONObject(result);

			if (!json.isNull("stampList")) {
				JSONArray stampList = json.getJSONArray("stampList");
				for(int i=0; i<stampList.length(); i++){
					JSONObject stamp = stampList.getJSONObject(i);
					stamps.append(stamp.getString("stamp_name"));
					stamps.append(" , ");
					
					Log.i("returned stamp", stamp.getString("stamp_id") + stamp.getString("stamp_name"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// 호출한 클래스 콜백
		if (callback != null)
			callback.onTaskComplete(stamps.toString());
	}

}
