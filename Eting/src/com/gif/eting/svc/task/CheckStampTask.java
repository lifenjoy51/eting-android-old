package com.gif.eting.svc.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.dto.StampDTO;
import com.gif.eting.svc.StampService;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Util;

/**
 * 폰에 저장된 스탬프ID 최대값을 서버에 전송하고
 * 최신 스탬프정보를 받아온다.
 * 
 * @author lifenjoy51
 *
 */
public class CheckStampTask extends AsyncTask<Object, String, String> {
	/**
	 * 작업 수행 후 이 클래스를 호출한 객체에서 후속작업을 실행시키기 위한 콜백. 
	 */
	private AsyncTaskCompleteListener<String> callback;
	private Context context;
	
	/**
	 * 생성자로 콜백을 받아온다.
	 * 
	 * @param callback
	 */
	public CheckStampTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			String urlStr = Util.serverContext+"/checkStamp";
			
			this.context = (Context) params[0];	//파라미터 context
			

			StampService svc = new StampService(context);
			String stampId = svc.getMaxStampId();
			String param = "stamp_id=" + stampId;	
			
			
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

			if (!json.isNull("stampList")) {
				JSONArray stampList = json.getJSONArray("stampList");
				List<StampDTO> list = new ArrayList<StampDTO>();
				for(int i=0; i<stampList.length(); i++){
					JSONObject stamp = stampList.getJSONObject(i);
					StampDTO stampDto = new StampDTO();
					
					//저장
					stampDto.setStamp_id(stamp.getString("stamp_id"));
					stampDto.setStamp_name(stamp.getString("stamp_name"));
					stampDto.setStamp_type(stamp.getString("stamp_type"));
					stampDto.setStamp_order(stamp.getString("stamp_order"));
					stampDto.setStamp_url(stamp.getString("stamp_url"));
					
					
					list.add(stampDto);
					
					Log.i("recieved new stamp", stamp.getString("stamp_id") + stamp.getString("stamp_name"));
				}
				
				StampService svc = new StampService(context);
				svc.saveStamps(list);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// 호출한 클래스 콜백
		if (callback != null)
			callback.onTaskComplete(stamps.toString());	
	}

}
