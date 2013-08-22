package com.gif.eting.act;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.R;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;

public class MyStoryList  extends SherlockFragment{

    /**
     * Factory method for this fragment class. Constructs a new fragment for the given page number.
     */
    public static MyStoryList create(int pageNumber) {
        MyStoryList fragment = new MyStoryList();
        return fragment;
    }

    public MyStoryList() {
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
                .inflate(R.layout.read_story, container, false);
        drawMyStoryList(rootView);

        return rootView;
	}

	// 내 이야기 목록 그리기
	public void drawMyStoryList(View view) {
		Log.i("debug", "drawMyStoryList");
		// StoryService초기화
		StoryService storyService = new StoryService(getActivity());
		List<StoryDTO> myStoryList = storyService.getMyStoryList();

		// 리스트뷰를 위한 변수들
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (StoryDTO myStory : myStoryList) {
			Log.i("debug", "myStory");
			Map<String, String> item = new HashMap<String, String>();
			item.put("title", myStory.getStory_date());
			item.put("desc", myStory.getContent());
			item.put("idx", String.valueOf(myStory.getIdx()));
			list.add(item);
		}

		// 어댑터에 데이터 포함
		final String[] fromMapKey = new String[] { "title", "desc" };
		final int[] toLayoutId = new int[] { android.R.id.text1,
				android.R.id.text2 };
		ListAdapter adapter = new SimpleAdapter(getActivity(), list,
				android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);

		// 리스트뷰에 어댑터 연결
		ListView listView = (ListView) view.findViewById(R.id.myStoryListView);
		listView.setAdapter(adapter);
		// 클릭이벤트 연결
		 listView.setOnItemClickListener(mOnItemClickListener);
	}
	
	 //아이템 클릭이벤트
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

    	@SuppressWarnings("unchecked")
    	public void onItemClick(AdapterView<?> parentView, View clickedView,
    			int position, long id) {
    		
    		ListAdapter listAdapter = (ListAdapter) parentView.getAdapter();	//ListView에서 Adapter 받아옴
    		Map<String, String> selectedItem = (Map<String, String>) listAdapter.getItem(position);	//선택한 Row에 있는 Object를 받아옴
    		String idx = selectedItem.get("idx");	//Object에서 idx값을 받아옴
    		
    		String toastMessage = idx;

    		Intent intent =new Intent(getActivity(), ReadMyStory.class);
    		intent.putExtra("idx", idx);
    		startActivity(intent);
    		
    		/*
    		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
    				+ " is selected. position is " + position + ", and id is " + id;*/

    		Toast.makeText(getActivity(), toastMessage,
    				Toast.LENGTH_SHORT).show();
    	}
    	
    };
}