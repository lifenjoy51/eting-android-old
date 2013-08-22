package com.gif.eting.svc.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.Config;
import com.gif.eting.util.HttpUtil;

public class SendStampTask extends AsyncTask<Object, String, String> {
	private AsyncTaskCompleteListener<String> callback;
	private String storyId;
	private Context context;
	
	public SendStampTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}

	@Override
	protected String doInBackground(Object... params) {

			String urlStr = Config.serverContext+"/saveStamp";
			
			this.storyId = (String) params[0];
			String param = "storyId=" + storyId;	//파라미터 첫번째값 storyId
			
			@SuppressWarnings("unchecked")
			List<String> stampIds = (List<String>)params[1];	//파라미터 두번째값 stamp들 #형식 유의
			
			this.context = (Context) params[2];	//파라미터 세번째값 context
			
			StringBuffer sbf = new StringBuffer();
			for(String stampId : stampIds){
				sbf.append(stampId);
				sbf.append(",");
			}
			
			if(sbf.length()>0){		//스탬프가 있으면
				String stampIdParams = sbf.substring(0, sbf.length()-1);	
				param += "&stamp_id=" + stampIdParams;
			}
			
			return HttpUtil.doPost(urlStr, param);
	}
	

	@Override
	protected void onPostExecute(String result) {

		Log.i("json response", result);	//응답확인

		StoryDTO inboxStory = new StoryDTO();
		inboxStory.setIdx(Long.parseLong(storyId));

		InboxDAO inboxDao = new InboxDAO(context);
		
		// 스탬프찍은 이야기 삭제
		inboxDao.open();
		inboxDao.delStory(inboxStory);
		inboxDao.close();
		
		// 호출한 클래스 콜백
		if (callback != null)
			callback.onTaskComplete("");	//화면에 무엇을 넘길것인가?
	}

}