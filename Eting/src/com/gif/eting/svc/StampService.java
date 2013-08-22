package com.gif.eting.svc;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.StoryDAO;
import com.gif.eting.dto.StampDTO;

/**
 * 스탬프 관련 로직을 처리한다.
 * 
 * @author lifenjoy51
 *
 */
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

	/**
	 *  폰에 저장된 스탬프 목록 가져오기
	 * @return
	 */
	public List<StampDTO> getStampList() {
		List<StampDTO> stampList = new ArrayList<StampDTO>();

		//TODO 현재는 임시로 하드코딩했다. 스티커목록을 정리해서 DB에 저장하고, 불러와서 사용해야한다. 그렇다면 스티커테이블이 필요?
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
