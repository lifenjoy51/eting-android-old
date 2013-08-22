package com.gif.eting.svc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.svc.StoryService;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.Config;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;

public class SendStoryTask extends AsyncTask<Object, String, String> {
	private AsyncTaskCompleteListener<String> callback;
	private Context context;
	
	public SendStoryTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}

	@Override
	protected String doInBackground(Object... params) {

			String content = (String) params[0]; 	//파라미터 첫번째값 content.
			this.context = (Context) params[1];	//파라미터 두번째값 context
			String phoneId = Installation.id(context);	//기기 고유값
			String param = "phone_id=" + phoneId +"&content=" +content;
			String urlStr = Config.serverContext+"/saveStory";
			
			
			return HttpUtil.doPost(urlStr, param);
	}
	

	@Override
	protected void onPostExecute(String result) {

		Log.i("json response", result);	//응답 확인

		// 폰DB에 저장
		StoryService storyService = new StoryService(context);
		storyService.saveToPhoneDB(result);

		
		// 호출한 클래스 콜백
		if (callback != null)
			callback.onTaskComplete("");
	}

}
