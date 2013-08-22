package com.gif.eting.svc;

import java.util.List;

import android.content.Context;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dto.StoryDTO;

public class InboxService {
	
	private Context context;
	private InboxDAO inboxDao;
	
	public InboxService(Context context){
		this.context = context;
		this.inboxDao = new InboxDAO(this.context);
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
	

}
