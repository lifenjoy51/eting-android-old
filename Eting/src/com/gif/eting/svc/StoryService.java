package com.gif.eting.svc;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.gif.eting.ReadMyStoryActivity;
import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;

public class StoryService {
	
	private Context context;
	private InboxDAO inboxDao;
	private StoryDAO storyDao;
	
	public StoryService(Context context){
		this.context = context;
		this.storyDao = new StoryDAO(context);
		this.inboxDao = new InboxDAO(context);
	}

	//서버에 저장 후 자료 받아옴
	public void saveStoryToServer(String content) {
		String phoneId = Installation.id(context);	//기기 고유값

		HttpUtil http = new HttpUtil();
		String params = "phone_id=" + phoneId+"&content=" + content;	//파라미터 설정
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/saveStory", params);	//서버주소
		String response = http.doPost("http://112.144.52.47:8080/eting/saveStory", params);	//개발서버주소
		
		Log.i("json response", response);
		
		//폰DB에 저장
		saveToPhoneDB(response);
		dbTest();
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
	public void saveStampToServer(String storyId, String stampId) {
		
		HttpUtil http = new HttpUtil();
		String params = "story_id=" + storyId+"&stamp_id=" + stampId;	//파라미터 설정
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/saveStamp", params);	//서버주소
		String response = http.doPost("http://112.144.52.47:8080/eting/saveStamp", params);	//개발서버주소
		
		Log.i("json response", response);
		
		StoryDTO inboxStory = new StoryDTO();
		inboxStory.setIdx(Long.parseLong(storyId));
		
		//스탬프찍은 이야기 삭제
		inboxDao.open();
		inboxDao.delStory(inboxStory);
		inboxDao.close();
	}
	
	//서버에서 스탬프 가져오기
	public String getStampFromServer(String storyId){
		StringBuffer stamps = new StringBuffer();
		
		HttpUtil http = new HttpUtil();
		String params = "storyId=" + storyId;	//파라미터 설정
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/getStamp", params);	//서버주소
		String response = http.doPost("http://112.144.52.47:8080/eting/getStamp", params);	//개발서버주소
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
		return stamps.toString();
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
