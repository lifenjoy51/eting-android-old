package com.gif.eting.svc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StampDTO;

public class StampService {
	
	private Context context;
	@SuppressWarnings("unused")
	private InboxDAO inboxDao;
	@SuppressWarnings("unused")
	private StoryDAO storyDao;
	
	public StampService(Context context){
		this.context = context;
		this.storyDao = new StoryDAO(this.context);
		this.inboxDao = new InboxDAO(this.context);
	}

	// 폰에 저장된 스탬프 목록 가져오기
	public List<StampDTO> getStampList() {
		List<StampDTO> stampList = new ArrayList<StampDTO>();

		StampDTO stamp = new StampDTO();
		stamp.setStamp_id("5");
		stamp.setStamp_name("좋아요");			
		stampList.add(stamp);
		
		stamp = new StampDTO();
		stamp.setStamp_id("6");
		stamp.setStamp_name("힘내요");
		stampList.add(stamp);
		
		return stampList;
	}

}
