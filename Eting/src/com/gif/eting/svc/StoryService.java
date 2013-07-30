package com.gif.eting.svc;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StoryDTO;

public class StoryService {
	
	Context context;
	
	public StoryService(Context context){
		this.context = context;		
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
				saveRecievedStoryToPhone(recievedStoryDto); // 폰DB에 받아온 이야기 저장
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	//내 이야기를 폰에 저장
	private void saveMyStoryToPhone(StoryDTO story){
		
		StoryDAO storyDao = new StoryDAO(context);
		storyDao.open();	//열고		
		storyDao.insStory(story);	//입력하고
		storyDao.close();	//닫고
	}
	
	//받아온 이야기를 폰에 저장
	private void saveRecievedStoryToPhone(StoryDTO story){
		
		InboxDAO inboxDao = new InboxDAO(context);
		inboxDao.open();	//열고		
		inboxDao.insStory(story);	//입력하고
		inboxDao.close();	//닫고
	}

	// 테스트용
	public void dbTest() {
		StoryDAO storyDao = new StoryDAO(context);
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		for (StoryDTO story : myStoryList) {
			Log.i("my story list",
					story.getIdx() + story.getContent() + story.getStory_date());
		}
		storyDao.close();

		InboxDAO inboxDao = new InboxDAO(context);
		inboxDao.open();
		List<StoryDTO> recievedStoryList = inboxDao.getStoryList();
		for (StoryDTO story : recievedStoryList) {
			Log.i("inbox story list", story.getIdx() + story.getContent()
					+ story.getStory_date());
		}
		inboxDao.close();
	}

}
