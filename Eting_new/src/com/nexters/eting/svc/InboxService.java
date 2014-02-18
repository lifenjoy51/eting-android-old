package com.nexters.eting.svc;

import java.util.List;

import android.content.Context;

import com.nexters.eting.dao.InboxDAO;
import com.nexters.eting.obj.Story;

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
	public Story getInboxStory() {
		List<Story> inboxStoryList = inboxDao.getStoryList();

		Story returnedStory;
		if (inboxStoryList.size() > 0) {
			int r = (int) (Math.random() * (inboxStoryList.size()));
			returnedStory = inboxStoryList.get(r);
		} else {
			// TODO 받은이야기가 없을때 어떤 로직을?
			returnedStory = new Story();
		}
		return returnedStory;
	}

	/**
	 * 현재 저장된 받은이야기 개수 가져오기
	 */
	public Integer getInboxCnt() {
		Integer count = inboxDao.getStoryCnt();
		return count;
	}
}
