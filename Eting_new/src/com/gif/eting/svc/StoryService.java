package com.gif.eting.svc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.etc.Util;
import com.gif.eting.obj.Emoticon;
import com.gif.eting.obj.Story;

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
	public StoryService(Context context) {
		this.context = context;
		this.storyDao = new StoryDAO(this.context);
		this.inboxDao = new InboxDAO(this.context);
	}

	/**
	 * 서버에서 받아온 정보를 폰 DB에 저장한다.
	 * 
	 * @param response
	 */
	public void saveToPhoneDB(String response) {

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
				Story myStoryDto = new Story();
				String myStoryId = myStory.getString("story_id");
				String myContent = myStory.getString("story_content");
				String myStoryDate = myStory.getString("story_date");
				String myStoryTime = myStory.getString("story_time");
				myStoryDto.setStory_id(myStoryId);
				myStoryDto.setStory_content(myContent);
				myStoryDto.setStory_date(myStoryDate);
				myStoryDto.setStory_time(myStoryTime);

				storyDao.insStory(myStoryDto); // 폰DB에 내 이야기 저장
			}

			/**
			 * 서버에서 받아온 다른사람의 이야기를 처리하는 부분
			 */
			if (!json.isNull("recievedStory")) {
				JSONObject recievedStory = json.getJSONObject("recievedStory");

				// 받아온 이야기
				Story recievedStoryDto = new Story();

				String recievedStoryId = recievedStory.getString("story_id");
				String recievedContent = recievedStory.getString("story_content");
				String recievedStoryDate = recievedStory
						.getString("story_date");
				String recievedStoryTime = recievedStory
						.getString("story_time");

				recievedStoryDto.setStory_id(recievedStoryId);
				recievedStoryDto.setStory_content(recievedContent);
				recievedStoryDto.setStory_date(recievedStoryDate);
				recievedStoryDto.setStory_time(recievedStoryTime);

				inboxDao.insStory(recievedStoryDto); // 폰DB에 받아온 이야기 저장
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 이야기에 찍힌 스탬프를 불러온다.
	 * 
	 * @param idx
	 * @return
	 */
	public List<Integer> getEmoticons(String storyId) {
		Story myStory = storyDao.getStoryInfo(storyId);
		String emoticonList = myStory.getEmoticon_list();

		List<Integer> list = new ArrayList<Integer>();
		if (!Util.isEmpty(emoticonList)) {
			String[] emoticons = emoticonList.split(",");
			for (String emoticon : emoticons) {
				if ("".equals(emoticon))
					continue;
				Integer emoticonSrc = Emoticon.getEmoticon_press_map().get(emoticon);
				list.add(emoticonSrc);
			}
		}
		return list;
	}
	
	/**
	 * 답글을 가져온다.
	 * 
	 * @param storyId
	 * @return
	 */
	public String getReplyContent(String storyId){
		Story myStory = storyDao.getStoryInfo(storyId);
		String replyContent = myStory.getReply_content();
		return replyContent;
	}

	/**
	 * 지금까지 작성한 내 이야기를 전부 가져온다.
	 * 
	 * @return
	 */
	public List<Story> getMyStoryList() {
		return storyDao.getStoryList();
	}

	/**
	 * key값을 갖고 특정한 이야기를 불러온다.
	 * 
	 * @param idx
	 * @return
	 */
	public Story getMyStory(String idx) {
		return storyDao.getStoryInfo(idx);
	}

	/**
	 * 이야기 삭제
	 * 
	 * @param storyIdx
	 * @return
	 */
	public Integer delStory(String storyIdx) {
		Integer result = storyDao.delStory(storyIdx);
		return result;
	}

	/**
	 * 스탬프찍힌 이야기 업데이트 (목록)
	 * 
	 * @param repliedStoryList
	 */
	public void updStoryReplyYn(List<String> repliedStoryList) {

		Set<String> set = new HashSet<String>(repliedStoryList);
		for (String storyId : set) {
			storyDao.updStoryReplyYn(storyId);
		}

	}

	/**
	 * 스탬프찍힌 이야기 업데이트 (단건)
	 * 
	 * @param repliedStoryList
	 */
	public int updStoryReply(String storyId, String emoticonList,
			String replyContent, String replyId) {
		
		// 코멘트ID가있으면 엄데이트 금지!
		if (storyDao.getStoryInfo(storyId) != null) {
			String pCommentId = storyDao.getStoryInfo(storyId).getReply_id();
			if (!Util.isEmpty(pCommentId)) {
				return 0;
			}
		}
		
		return storyDao.updStoryReply(storyId, emoticonList, replyContent,
				replyId);
	}

	/**
	 * 스탬프 읽은상태로 변환
	 * 
	 * @param storyId
	 */
	public void updStoryReplyRead(String storyId) {
		storyDao.updStoryReplyRead(storyId);
	}

	/**
	 * 코멘트 삭제
	 * 
	 * @param storyId
	 */
	public int deleteComment(String storyId) {
		return storyDao.deleteComment(storyId);
	}

	/**
	 * 내 이야기 개수
	 * 
	 * @return
	 */
	public Integer getStoryCnt() {
		return storyDao.getStoryCnt();
	}

}
