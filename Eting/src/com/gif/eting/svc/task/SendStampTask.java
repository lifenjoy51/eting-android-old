package com.gif.eting.svc.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;
import com.gif.eting.util.Util;

/**
 * 스탬프를 서버에 전송하는 작업
 * AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 * 
 * @author lifenjoy51
 *
 */
public class SendStampTask extends AsyncTask<Object, String, String> {
	/**
	 * 작업 수행 후 이 클래스를 호출한 객체에서 후속작업을 실행시키기 위한 콜백. 
	 */
	private AsyncTaskCompleteListener<String> callback;
	
	private String storyId;
	private Context context;
	
	/**
	 * 생성자로 콜백을 받아온다.
	 * 
	 * @param callback
	 */
	public SendStampTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}
	
	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			this.context = (Context) params[2];	//파라미터 세번째값 context
		
			String urlStr = Util.serverContext+"/saveStamp";
			
			this.storyId = (String) params[0];
			String param = "story_id=" + storyId;	//파라미터 첫번째값 storyId
			
			String phoneId = Installation.id(context);	//기기 고유값
			param += "&phone_id=" + phoneId;
			
			@SuppressWarnings("unchecked")
			List<String> stampIds = (List<String>)params[1];	//파라미터 두번째값 stamp들 #형식 유의
			
			
			String sender =(String) params[3];	//파라미터 네번째값 sender
			
			StringBuffer sbf = new StringBuffer();
			for(String stampId : stampIds){
				sbf.append(stampId);
				sbf.append(",");
			}
			
			if(sbf.length()>0){		//스탬프가 있으면
				String stampIdParams = sbf.substring(0, sbf.length()-1);	
				param += "&stamp_id=" + stampIdParams;				
				param += "&sender=" + sender;	//보내는이는 서버에서 이야기-스탬프 매핑테이블에 저장될것이기 때문에 스탬프가 없으면 보내는이도 기록하지 못한다. 
			}
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			//System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}
	
	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		//Log.i("json response", result);	//응답확인
		
		if("UnknownHostException".equals(result)){
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete("UnknownHostException");	
			
		}else if("HttpUtil_Error".equals(result)){
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete("HttpUtil_Error");	
			
		}else{

			StoryDTO inboxStory = new StoryDTO();
			inboxStory.setIdx(Long.parseLong(storyId));
	
			InboxDAO inboxDao = new InboxDAO(context);
			
			// 스탬프찍은 이야기 삭제
			inboxDao.open();
			inboxDao.delStory(inboxStory);
			inboxDao.close();
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onTaskComplete("Success");
		}
	}

}