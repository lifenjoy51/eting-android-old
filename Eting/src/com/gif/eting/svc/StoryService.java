package com.gif.eting.svc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StoryDTO;

/**
 * 이야기 관련 로직을 처리한다.
 * 
 * @author lifenjoy51
 *
 */
public class StoryService {
	
	/**
	 * Context
	 */
	private Context context;
	
	/**
	 * 받은이야기에 대한 정보를 읽고/쓰는 객체. DataAccessObject.
	 */
	private InboxDAO inboxDao;
	
	/**
	 * 이야기에 대한 정보를 읽고/쓰는 객체. DataAccessObject. 
	 */
	private StoryDAO storyDao;
	
	/**
	 * 생성자. DAO를 초기화시킨다.
	 * 
	 * @param context
	 */
	public StoryService(Context context){
		this.context = context;
		this.storyDao = new StoryDAO(this.context);
		this.inboxDao = new InboxDAO(this.context);
	}
	
	/**
	 * 서버에서 받아온 정보를 폰 DB에 저장한다.
	 * @param response
	 */
	public void saveToPhoneDB(String response){

		try {
			/**
			 * 서버로부터 받은 JSON문자열을 JSON객체로 변환한다.
			 */
			JSONObject json = new JSONObject(response);	

			/**
			 * 내가 작성한 이야기를 처리하는 부분
			 */
			if (!json.isNull("myStory")) {
				JSONObject myStory = json.getJSONObject("myStory");

				// 내 이야기
				StoryDTO myStoryDto = new StoryDTO();
				String myStoryId = myStory.getString("story_id");
				String myContent = myStory.getString("content");
				String myStoryDate = myStory.getString("story_date");
				String myStoryTime = myStory.getString("story_time");
				myStoryDto.setIdx(Long.parseLong(myStoryId));
				myStoryDto.setContent(myContent);
				myStoryDto.setStory_date(myStoryDate);
				myStoryDto.setStory_time(myStoryTime);

				Log.i("returned my story",
						myStoryDto.getIdx() + myStoryDto.getContent() + myStoryDto.getStory_date());
				
				this.saveMyStoryToPhone(myStoryDto); // 폰DB에 내 이야기 저장
			}

			/**
			 * 서버에서 받아온 다른사람의 이야기를 처리하는 부분
			 */
			if (!json.isNull("recievedStory")) {
				JSONObject recievedStory = json.getJSONObject("recievedStory");

				// 받아온 이야기
				StoryDTO recievedStoryDto = new StoryDTO();
				String recievedStoryId = recievedStory.getString("story_id");
				String recievedContent = recievedStory.getString("content");
				String recievedStoryDate = recievedStory.getString("story_date");
				String recievedStoryTime = recievedStory.getString("story_time");
				recievedStoryDto.setIdx(Long.parseLong(recievedStoryId));
				recievedStoryDto.setContent(recievedContent);
				recievedStoryDto.setStory_date(recievedStoryDate);
				recievedStoryDto.setStory_time(recievedStoryTime);
				
				Log.i("returned recieved story",
						recievedStoryDto.getIdx() + recievedStoryDto.getContent() + recievedStoryDto.getStory_date());
				
				this.saveRecievedStoryToPhone(recievedStoryDto); // 폰DB에 받아온 이야기 저장
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 내 이야기를 폰에 저장한다
	 * 
	 * @param story
	 */
	private void saveMyStoryToPhone(StoryDTO story){
		storyDao.open();	//열고		
		storyDao.insStory(story);	//입력하고
		storyDao.close();	//닫고
	}
	
	/**
	 * 받아온 이야기를 폰에 저장한다.
	 * 
	 * @param story
	 */
	private void saveRecievedStoryToPhone(StoryDTO story){
		inboxDao.open();	//열고		
		inboxDao.insStory(story);	//입력하고
		inboxDao.close();	//닫고
	}
	
	/**
	 * 지금까지 작성한 내 이야기를 전부 가져온다.
	 *  
	 * @return
	 */
	public List<StoryDTO> getMyStoryList(){
		storyDao.open();
		List<StoryDTO> myStoryList = storyDao.getStoryList();
		storyDao.close();
		return myStoryList;
	}
	
	/**
	 *  key값을 갖고 특정한 이야기를 불러온다.
	 * @param idx
	 * @return
	 */
	public StoryDTO getMyStory(String idx) {
		storyDao.open();
		StoryDTO myStory = storyDao.getStoryInfo(idx);
		storyDao.close();
		return myStory;
	}
	
	/**
	 * 이야기 삭제
	 * 
	 * @param storyIdx
	 * @return
	 */
	public Integer delStory(String storyIdx) {
		storyDao.open();
		StoryDTO story = new StoryDTO();
		story.setIdx(Long.parseLong(storyIdx));		
		Integer result = storyDao.delStory(story);
		storyDao.close();
		return result;
	}
	
	/**
	 * 스탬프찍힌 이야기 업데이트
	 * @param stampedStoryList
	 */
	public void updStoryStampYn(List<String> stampedStoryList){
		storyDao.open();
		Set<String> set = new HashSet<String>(stampedStoryList);
		for(String storyId : set){
			storyDao.updStoryStampYn(storyId);
		}
		storyDao.close();
		
	}
	
	/**
	 * 내 이야기 개수
	 * 
	 * @return
	 */
	public Integer getStoryCnt(){
		storyDao.open();
		Integer count = storyDao.getStoryCnt();
		storyDao.close();
		return count;
	}

}
