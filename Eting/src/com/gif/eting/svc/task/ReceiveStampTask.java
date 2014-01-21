package com.gif.eting.svc.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.dto.StampDTO;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Util;

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
	private AsyncTaskCompleteListener<List<StampDTO>> callback;
	
	/**
	 * 생성자로 콜백을 받아온다.
	 * 
	 * @param callback
	 */
	public ReceiveStampTask(AsyncTaskCompleteListener<List<StampDTO>> callback){
		this.callback = callback;
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(String... params) {

			String urlStr = Util.serverContext+"/getStamp";
			String param = "story_id=" + params[0];	//파라미터 첫번째값 storyId
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			//System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {
		//System.out.println("ReceiveStampTask onPostExecute = "+result);
		//Log.i("ReceiveStampTask json response", result);	//응답확인
		
		if("UnknownHostException".equals(result)){
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete(null);	
			
		}else if("HttpUtil_Error".equals(result)){
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete(null);	
			
		}else{
			
			List<StampDTO> list = new ArrayList<StampDTO>();
			
			try {
				JSONObject json = new JSONObject(result);
	
				if (!json.isNull("stampList")) {
					JSONArray stampList = json.getJSONArray("stampList");
					for(int i=0; i<stampList.length(); i++){
						JSONObject stamp = stampList.getJSONObject(i);
						StampDTO stampDto = new StampDTO();
						stampDto.setStamp_id(stamp.getString("stamp_id"));
						stampDto.setStamp_name(stamp.getString("stamp_name"));
						stampDto.setComment(stamp.getString("comment"));
						list.add(stampDto);
						
						//Log.i("returned stamp", stamp.getString("stamp_id") + stamp.getString("stamp_name"));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete(list);	
		}
	}

}
