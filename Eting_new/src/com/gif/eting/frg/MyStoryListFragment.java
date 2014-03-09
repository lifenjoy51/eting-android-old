package com.gif.eting.frg;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gif.eting.MainViewPagerActivity;
import com.gif.eting.R;
import com.gif.eting.ReadMyStoryActivity;
import com.gif.eting.etc.Util;
import com.gif.eting.obj.Story;
import com.gif.eting.svc.StoryService;

/**
 * 내 이야기 목록
 * 
 * @author lifenjoy51
 *
 */
public class MyStoryListFragment  extends BaseFragment{
	private ViewPager mPager = MainViewPagerActivity.mPager;
	
	private ViewGroup rootView;
	
	private int lastPos = 0;
	

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static MyStoryListFragment create(int pageNumber) {
        MyStoryListFragment fragment = new MyStoryListFragment();
        return fragment;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mystory, container, false);
        return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
        
        // 내 이야기 목록을 가져와 그린다.
        this.drawMyStoryList(getView());
	}

	/**
	 *  내 이야기 목록 그리기
	 * @param view
	 */
	public void drawMyStoryList(View view) {
		// StoryService초기화
		StoryService storyService = new StoryService(getActivity());
		List<Story> myStoryList = storyService.getMyStoryList();
		
		/**
		 * 날짜구분선을 위한 작업
		 */	
		MylistAdapter m_adapter = new MylistAdapter(getActivity(), R.layout.mylist_item); // 어댑터 생성
		String chkDate="";
		for(Story story : myStoryList){
			String tempMonth = "";
			if(story.getStory_date() != null){
				if(story.getStory_date().length() > 0){
					tempMonth = story.getStory_date().substring(0,7);
				}
				//Log.i("temp month = ", tempMonth);
			}
			if(!chkDate.equals(tempMonth)){
				chkDate = tempMonth;
				Story temp = new Story();
				temp.setStory_date(chkDate);
				temp.setStory_content("#dateInfo");	//이야기 내용이 아니라 날짜를 구분하는 특수문자를 입력한다.
				m_adapter.addSeparatorItem(temp);
			}
			
			m_adapter.addItem(story);
		}
		
		/**
		 * 아무것도 없을때 처리
		 */
		System.out.println("myStoryList.size() " +myStoryList.size());
		if(myStoryList.size() == 0){
			
			FrameLayout area = (FrameLayout) rootView.findViewById(R.id.story_list_alarm_area);
			area.setVisibility(View.VISIBLE);
			area.bringToFront();
			
			// 별모양2
			ImageView storyListAlarmIcon = (ImageView) rootView
					.findViewById(R.id.story_list_alarm_icon);
			Util.setPosition(storyListAlarmIcon, R.drawable.main_acc_2, 50, 35);
			
			//텍스트
			TextView storListAramText = (TextView) rootView.findViewById(R.id.story_list_alarm_text);
			storListAramText.setTextColor(Color.parseColor("#555555"));
			int storListAramTextY = getResources().getDisplayMetrics().heightPixels *43/100;
			FrameLayout.LayoutParams storListAramTextParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.WRAP_CONTENT);
			storListAramTextParams.topMargin = storListAramTextY; //Your Y coordinate
			storListAramTextParams.gravity = Gravity.CENTER | Gravity.TOP;
			storListAramText.setLayoutParams(storListAramTextParams);
			
		}else{
			FrameLayout area = (FrameLayout) rootView.findViewById(R.id.story_list_alarm_area);
			area.setVisibility(View.GONE);
			area.bringToFront();
		}
		
		// 리스트뷰에 어댑터 연결
		ListView listView = (ListView) view.findViewById(R.id.myStoryListView);
		listView.setAdapter(m_adapter);
		listView.setSelection(lastPos);
		
		// 클릭이벤트 연결
		listView.setOnItemClickListener(this.mOnItemClickListener);
		listView.setOnScrollListener(new OnScrollListener(){

		    public void onScroll(AbsListView view, int firstVisibleItem,
		            int visibleItemCount, int totalItemCount) { 
		    }

		    public void onScrollStateChanged(AbsListView view, int scrollState) {   
		    	lastPos = view.getFirstVisiblePosition();
		    }

		});

	}
	
    
    /** 
     * 아이템 클릭이벤트
     */
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
    	
		public void onItemClick(AdapterView<?> parentView, View clickedView,
    			int position, long id) {

			try {
				MylistAdapter listAdapter = (MylistAdapter) parentView.getAdapter(); // ListView에서 Adapter 받아옴
				Story selectedItem = (Story) listAdapter.getItem(position); // 선택한 Row에 있는 Object를 받아옴
				String idx = String.valueOf(selectedItem.getStory_id()); // Object에서 idx값을 받아옴
				//Log.i("idx = ", idx);
				
				if(!"".equals(idx) && !"0".equals(idx)){
					readMyStoryPopup(getActivity(), idx);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    };
    
    /**
     * Gcm으로 받은 메세지 열기
     */
    @Override
    public void onGcm(Context context, String... storyId) {
    	readMyStoryPopup(context, storyId[0]);
    }
    
    /**
     * 내이야기 보기 화면 호출
     * @param storyId
     */
    public void readMyStoryPopup(Context context, String storyId){		
		Intent intent =new Intent(context, ReadMyStoryActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
    	//Log.i("onKeyDown SUB", String.valueOf(keyCode));
    	// 메인 화면으로 이동
		mPager = (ViewPager) getView().getParent();
		mPager.setCurrentItem(1);
		
        return true;
    }
    
}