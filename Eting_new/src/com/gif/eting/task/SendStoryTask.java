package com.gif.eting.task;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.gif.eting.etc.AsyncTaskCompleteListener;
import com.gif.eting.etc.Const;
import com.gif.eting.etc.HttpUtil;
import com.gif.eting.etc.Util;
import com.gif.eting.svc.StoryService;

/**
 * 작성한 이야기를 서버에 전송하는 작업 AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 *
 * @author lifenjoy51
 *
 */
public class SendStoryTask extends AsyncTask<Object, String, String> {
	// 작업 수행 후 이 클래스를 호출한 객체에서 후속작업을 실행시키기 위한 콜백.
	private final AsyncTaskCompleteListener<String> callback;

	// Context
	private Context context;

	/**
	 * 생성자로 콜백을 받아온다.
	 *
	 * @param callback
	 */
	public SendStoryTask(AsyncTaskCompleteListener<String> callback) {
		this.callback = callback;
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... params) {

		String content = (String) params[0]; // 파라미터 첫번째값 content.
		this.context = (Context) params[1]; // 파라미터 두번째값 context

		String deviceId = Util.DeviceId(context); // 기기 고유값

		// 서버에 전송할 파라미터 조립
		Map<String,String> param = new HashMap<String,String>();
		param.put("device_id", deviceId);
		param.put("content", content);
		String urlStr = Const.serverContext + "/saveStory";

		// Http전송
		String response = HttpUtil.doPost(urlStr, param);
		return response;
	}

	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {
		String returnString = "Success";
		if ("HttpUtil_Error".equals(result)) {
			returnString = "HttpUtil_Error";
		} else if ("UnknownHostException".equals(result)) {
			returnString = "UnknownHostException";
		}  else if ("".equals(result)) {
			returnString = "Error";
		} else {
			// 폰DB에 저장
			StoryService storyService = new StoryService(context);
			storyService.saveToPhoneDB(result);
		}
		// 콜백 메소드 실행
		if (callback != null)
			callback.onTaskComplete(returnString);
	}

}
