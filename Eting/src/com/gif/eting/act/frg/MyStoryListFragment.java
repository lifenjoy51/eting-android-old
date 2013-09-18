package com.gif.eting.act.frg;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.act.ReadMyStoryActivity;
import com.gif.eting.act.view.MylistAdapter;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting_dev.R;

/**
 * 내 이야기 목록
 * 
 * @author lifenjoy51
 *
 */
public class MyStoryListFragment  extends SherlockFragment{
	private ViewPager mPager;
	
	private Typeface nanum;
	

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static MyStoryListFragment create(int pageNumber) {
        MyStoryListFragment fragment = new MyStoryListFragment();
        return fragment;
    }

    /**
     * 생성자
     */
    public MyStoryListFragment() {
    }

    public void setViewPager(ViewPager mPager) {
    	this.mPager = mPager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.mystory_list, container, false);
        nanum = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumGothic.ttf");

        return rootView;
	}
    
    

	@Override
	public void onResume() {
		super.onResume();
        
        /**
         * 내 이야기 목록을 가져와 그린다.
         */
        this.drawMyStoryList(getView());
	}

	/**
	 *  내 이야기 목록 그리기
	 * @param view
	 */
	public void drawMyStoryList(View view) {
		// StoryService초기화
		StoryService storyService = new StoryService(getActivity());
		List<StoryDTO> myStoryList = storyService.getMyStoryList();
		
		/**
		 * 날짜구분선을 위한 작업
		 */
		List<StoryDTO> list = new ArrayList<StoryDTO>();
		String chkDate="";
		for(StoryDTO story : myStoryList){
			String tempMonth = "";
			if(story.getStory_date() != null){
				tempMonth = story.getStory_date().substring(0,7);
				Log.i("temp month = ", tempMonth);
			}
			if(!chkDate.equals(tempMonth)){
				chkDate = tempMonth;
				StoryDTO temp = new StoryDTO();
				temp.setStory_date(chkDate);
				temp.setContent("#dateInfo");	//이야기 내용이 아니라 날짜를 구분하는 특수문자를 입력한다.
				list.add(temp);
			}
			
			list.add(story);
		}
		MylistAdapter m_adapter = new MylistAdapter(getActivity(), R.layout.mylist_item, list); // 어댑터 생성
		
		// 리스트뷰에 어댑터 연결
		ListView listView = (ListView) view.findViewById(R.id.myStoryListView);
		listView.setAdapter(m_adapter);
		
		// 클릭이벤트 연결
		listView.setOnItemClickListener(this.mOnItemClickListener);
	}
	
    
    /** 
     * 아이템 클릭이벤트
     */
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
    	
    	public void onItemClick(AdapterView<?> parentView, View clickedView,
    			int position, long id) {

			try {
				MylistAdapter listAdapter = (MylistAdapter) parentView.getAdapter(); // ListView에서 Adapter 받아옴
				StoryDTO selectedItem = (StoryDTO) listAdapter.getItem(position); // 선택한 Row에 있는 Object를 받아옴
				String idx = String.valueOf(selectedItem.getIdx()); // Object에서 idx값을 받아옴
				Log.i("idx = ", idx);
				
				if(!"".equals(idx) && !"0".equals(idx)){
					readMyStoryPopup(getActivity(), idx);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    };
    
    /**
     * 내이야기 보기 화면 호출
     * @param storyId
     */
    public void readMyStoryPopup(Context context, String storyId){		
		Intent intent =new Intent(context, ReadMyStoryActivity.class);
		intent.putExtra("idx", storyId);
		context.startActivity(intent);
    }

	/**
	 * 뒤로가기 눌렀을때 이벤트 처리를 위한 로직
	 * 
	 * @param keyCode
	 * @return
	 */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	Log.i("onKeyDown SUB", String.valueOf(keyCode));
    	// 메인 화면으로 이동
    	if(mPager!=null){
    		mPager.setCurrentItem(1);
    	}
        return true;
    }
}