package com.gif.eting.svc.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.svc.StoryService;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;
import com.gif.eting.util.Util;

/**
 * 폰에 저장된 스탬프ID 최대값을 서버에 전송하고
 * 최신 스탬프정보를 받아온다.
 * 
 * @author lifenjoy51
 *
 */
public class CheckStampedStoryTask extends AsyncTask<Object, String, String> {
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
	public CheckStampedStoryTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			String urlStr = Util.serverContext+"/checkMyStoryStamped";
			
			this.context = (Context) params[0];	//파라미터 context
			String phoneId = Installation.id(context);	//기기 고유값
			String param = "phone_id=" + phoneId;	
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			//System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		//Log.i("CheckStampedStoryTask json response", result);	//응답확인
		
		if("UnknownHostException".equals(result)){
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete("UnknownHostException");	
			
		}else if("HttpUtil_Error".equals(result)){
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete("HttpUtil_Error");	
			
		}else{
			StringBuffer stamps = new StringBuffer();
			
			try {
				JSONObject json = new JSONObject(result);

				if (!json.isNull("stampedStoryList")) {				
					StoryService svc = new StoryService(context);
					JSONArray stampedStoryList = json.getJSONArray("stampedStoryList");
					List<String> list = new ArrayList<String>();
					for(int i=0; i<stampedStoryList.length(); i++){
						list.add(stampedStoryList.getString(i));
					}
					svc.updStoryStampYn(list);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}finally{
				
				// 호출한 클래스 콜백
				if (callback != null)
					callback.onTaskComplete(stamps.toString());	
				
			}
			
		}
		
	}

}
