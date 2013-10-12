package com.gif.eting.svc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Util;

/**
 * 이야기 신고기능
 * AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 * 
 * @author lifenjoy51
 *
 */
public class ReportStroyTask extends AsyncTask<Object, String, String> {
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
	public ReportStroyTask(AsyncTaskCompleteListener<String> callback){
		this.callback = callback;
	}
	
	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

			String urlStr = Util.serverContext+"/report";
			
			this.storyId = (String) params[0];
			String param = "story_id=" + storyId;	//파라미터 첫번째값 storyId
			this.context = (Context) params[1];	//파라미터 두번째값 context
			
			String response = HttpUtil.doPost(urlStr, param);	//Http전송 
			System.out.println(this.getClass().getName() + " = " + response);
			return response;
	}

	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		Log.i("reportStroyTask json response", result); // 응답확인
		String returnMessage = "";

		if ("UnknownHostException".equals(result)) {
			returnMessage = "UnknownHostException";

		} else if ("HttpUtil_Error".equals(result)) {
			returnMessage = "HttpUtil_Error";

		} else {
			// 성공했을때
			returnMessage = "Success";

			StoryDTO inboxStory = new StoryDTO();
			inboxStory.setIdx(Long.parseLong(storyId));
	
			InboxDAO inboxDao = new InboxDAO(context);
			
			// 신고한 이야기 삭제
			inboxDao.open();
			inboxDao.delStory(inboxStory);
			inboxDao.close();
		}

		// 호출한 클래스 콜백
		if (callback != null)
			callback.onTaskComplete(returnMessage);
	}

}