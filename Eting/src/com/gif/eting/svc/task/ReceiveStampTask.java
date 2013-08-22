package com.gif.eting.svc.task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.Config;
import com.gif.eting.util.HttpUtil;

/**
 * 조회할 이야기에 해당하는 스탬프를 서버에서 받아오는 작업
 * AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 * 
 * @author lifenjoy51
 *
 */
public class ReceiveStampTask extends AsyncTask<String, String, String> {
	/**
	 * 작업 수행 후 이 클래스를 호출한 객체에서 후속작업을 실행시키기 위한 콜백. 
	 */
	private AsyncTaskCompleteListener<String> callback;
	
	/**
	 * 생성자로 콜백을 받아온다.
	 * 
	 * @param callback
	 */
	public ReceiveStampTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(String... params) {

			String urlStr = Config.serverContext+"/getStamp";
			String param = "storyId=" + params[0];	//파라미터 첫번째값 storyId
			
			return HttpUtil.doPost(urlStr, param);	//Http전송
	}
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		Log.i("json response", result);	//응답확인
		
		StringBuffer stamps = new StringBuffer();
		
		try {
			JSONObject json = new JSONObject(result);

			//TODO 현재는 스탬프를 텍스트형태로 붙여서 넘겨주는데 추후 어떻게 할것인지 논의필요함.
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
