package com.gif.eting.svc;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;
import com.gif.eting.util.ServiceCompleteListener;

public class StoryService {
	
	//private String serverContext = "http://lifenjoys.cafe24.com/eting";	//서버
	private String serverContext = "http://112.144.52.47:8080/eting";	//개발서버
	private Context context;
	private InboxDAO inboxDao;
	private StoryDAO storyDao;
	
	public StoryService(Context context){
		this.context = context;
		this.storyDao = new StoryDAO(context);
		this.inboxDao = new InboxDAO(context);
	}

	//서버에 저장 후 자료 받아옴
	public void saveStoryToServer(String content, ServiceCompleteListener<String> callback) {
		
		String url = this.serverContext + "/saveStory";	//개발서버주소 URL
		String phoneId = Installation.id(context);	//기기 고유값
		String params = "phone_id=" + phoneId+"&content=" + content;	//파라미터 설정
		
		new HttpUtil(url, params,new AfterSaveStoryToServer(callback)).execute(params);	//Http요청. 콜백함수는 아래에
		
	}
	
	// Http통신 후 처리로직
	private class AfterSaveStoryToServer implements
			AsyncTaskCompleteListener<String> {
		
		private ServiceCompleteListener<String> callback;

		public AfterSaveStoryToServer(ServiceCompleteListener<String> callback) {
			super();
			this.callback = callback;
		}

		@Override
		public void onTaskComplete(String response) {

			Log.i("json response", response);	//응답 확인

			// 폰DB에 저장
			saveToPhoneDB(response);
			
			dbTest();	//자료입력되었나 확인하는 테스트함수

			// 호출한 클래스 콜백
			if (callback != null)
				callback.onServiceComplete("");	//화면에 무엇을 넘길것인가?
		}

	}
	

	//서버에서 받아온 자료 정리
	public void saveToPhoneDB(String response){

		try {
			JSONObject json = new JSONObject(response);

			if (!json.isNull("myStory")) {
				JSONObject myStory = json.getJSONObject("myStory");

				// 내 이야기
				StoryDTO myStoryDto = new StoryDTO();
				String myStoryId = myStory.getString("story_id");
				String myContent = myStory.getString("content");
				String myStoryDate = myStory.getString("story_date");
				myStoryDto.setIdx(Long.parseLong(myStoryId));
				myStoryDto.setContent(myContent);
				myStoryDto.setStory_date(myStoryDate);

				Log.i("returned my story",
						myStoryDto.getIdx() + myStoryDto.getContent() + myStoryDto.getStory_date());
				
				
				saveMyStoryToPhone(myStoryDto); // 폰DB에 내 이야기 저장
			}

			if (!json.isNull("recievedStory")) {
				JSONObject recievedStory = json.getJSONObject("recievedStory");

				// 받아온 이야기
				StoryDTO recievedStoryDto = new StoryDTO();
				String recievedStoryId = recievedStory.getString("story_id");
				String recievedContent = recievedStory.getString("content");
				String recievedStoryDate = recievedStory.getString("story_date");
				recievedStoryDto.setIdx(Long.parseLong(recievedStoryId));
				recievedStoryDto.setContent(recievedContent);
				recievedStoryDto.setStory_date(recievedStoryDate);
				
				Log.i("returned recieved story",
						recievedStoryDto.getIdx() + recievedStoryDto.getContent() + recievedStoryDto.getStory_date());
				
				saveRecievedStoryToPhone(recievedStoryDto); // 폰DB에 받아온 이야기 저장
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	//내 이야기를 폰에 저장
	private void saveMyStoryToPhone(StoryDTO story){
		storyDao.open();	//열고		
		storyDao.insStory(story);	//입력하고
		storyDao.close();	//닫고
	}
	
	//받아온 이야기를 폰에 저장
	private void saveRecievedStoryToPhone(StoryDTO story){
		inboxDao.open();	//열고		
		inboxDao.insStory(story);	//입력하고
		inboxDao.close();	//닫고
	}
	
	//내 이야기 가져오기
	public List<StoryDTO> getMyStoryList(){
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		storyDao.close();
		return myStoryList;
	}
	
	// 내 이야기 가져오기
	public StoryDTO getMyStory(String idx) {
		StoryDTO story = new StoryDTO();
		story.setIdx(Long.parseLong(idx));
		
		storyDao.open();
		StoryDTO myStory = storyDao.getStoryInfo(story);
		storyDao.close();
		return myStory;
	}

	// 받아온 이야기 하나 가져오기
	public StoryDTO getInboxStory() {
		inboxDao.open();
		List<StoryDTO> inboxStoryList = inboxDao.getStoryList();
		inboxDao.close();
		
		StoryDTO returnedStory;
		if(inboxStoryList.size()>0){
			returnedStory = inboxStoryList.get(0);
		}else{
			returnedStory = new StoryDTO();
		}
		return returnedStory;
	}
	

	//서버로 스탬프 찍은 정보 전송
	public void saveStampToServer(String storyId, String stampId, ServiceCompleteListener<String> callback) {
		
		String url = this.serverContext+"/saveStamp";
		String params = "story_id=" + storyId+"&stamp_id=" + stampId;	//파라미터 설정
		
		new HttpUtil(url, params, new AfterSaveStampToServer(callback, storyId)).execute("");	//Http 요청
	}

	// Http통신 후 처리로직
	private class AfterSaveStampToServer implements
			AsyncTaskCompleteListener<String> {
		
		private ServiceCompleteListener<String> callback;
		private String storyId;

		public AfterSaveStampToServer(
				ServiceCompleteListener<String> callback, String storyId) {
			super();
			this.callback = callback;
			this.storyId = storyId;
		}

		@Override
		public void onTaskComplete(String response) {

			Log.i("json response", response);	//응답확인

			StoryDTO inboxStory = new StoryDTO();
			inboxStory.setIdx(Long.parseLong(storyId));

			// 스탬프찍은 이야기 삭제
			inboxDao.open();
			inboxDao.delStory(inboxStory);
			inboxDao.close();
			
			// 호출한 클래스 콜백
			if (callback != null)
				callback.onServiceComplete("");	//화면에 무엇을 넘길것인가?
		}
	}
	
	//서버에서 스탬프 가져오기
	public void getStampFromServer(String storyId, ServiceCompleteListener<String> callback){
		String url = this.serverContext+"/getStamp";
		String params = "storyId=" + storyId;	//파라미터 설정
		new HttpUtil(url, params, new AfterGetStampFromServer(callback));	//Http 요청
	}
	
	// Http통신 후 처리로직
		private class AfterGetStampFromServer implements
				AsyncTaskCompleteListener<String> {
			
			private ServiceCompleteListener<String> callback;

			public AfterGetStampFromServer(
					ServiceCompleteListener<String> callback) {
				super();
				this.callback = callback;
			}

			@Override
			public void onTaskComplete(String response) {

				Log.i("json response", response);	//응답확인
				
				StringBuffer stamps = new StringBuffer();
				
				try {
					JSONObject json = new JSONObject(response);

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
					callback.onServiceComplete(stamps.toString());
			}

		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 테스트용
	public void dbTest() {
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		for (StoryDTO story : myStoryList) {
			Log.i("my story list",
					story.getIdx() + story.getContent() + story.getStory_date());
		}
		storyDao.close();

		inboxDao.open();
		List<StoryDTO> recievedStoryList = inboxDao.getStoryList();
		for (StoryDTO story : recievedStoryList) {
			Log.i("inbox story list", story.getIdx() + story.getContent()
					+ story.getStory_date());
		}
		inboxDao.close();
	}

}
