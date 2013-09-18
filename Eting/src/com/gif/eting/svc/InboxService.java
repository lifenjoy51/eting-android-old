package com.gif.eting.svc;

import java.util.List;

import android.content.Context;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dto.StoryDTO;

/**
 * 받은 이야기 관련 로직 처리
 * 
 * @author lifenjoy51
 * 
 */
public class InboxService {

	private Context context;
	private InboxDAO inboxDao;

	public InboxService(Context context) {
		this.context = context;
		this.inboxDao = new InboxDAO(this.context);
	}

	/**
	 * 받아온 이야기 하나 가져오기
	 * 
	 * @return
	 */
	public StoryDTO getInboxStory() {
		inboxDao.open();
		List<StoryDTO> inboxStoryList = inboxDao.getStoryList();
		inboxDao.close();

		StoryDTO returnedStory;
		if (inboxStoryList.size() > 0) {
			returnedStory = inboxStoryList.get(0);
		} else {
			// TODO 받은이야기가 없을때 어떤 로직을?
			returnedStory = new StoryDTO();
		}
		return returnedStory;
	}
	

	/**
	 * 현재 저장된 받은이야기 개수 가져오기
	 */
	public Integer getInboxCnt() {
		inboxDao.open();
		Integer count = inboxDao.getStoryCnt();
		inboxDao.close();
		return count;
	}

}
