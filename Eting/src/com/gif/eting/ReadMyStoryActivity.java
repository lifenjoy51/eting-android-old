package com.gif.eting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.TwoLineListItem;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;

public class ReadMyStoryActivity extends Activity implements OnItemClickListener{
	
	private StoryService storyService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_story);
		
		drawMyStoryList();	//내 이야기 목록 그리기
	}
	
	//내 이야기 목록 그리기
	public void drawMyStoryList(){
		//StoryService초기화
		storyService = new StoryService(this.getApplicationContext());
		List<StoryDTO> myStoryList = storyService.getMyStoryList();
		
		//리스트뷰를 위한 변수들
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		for(StoryDTO myStory:myStoryList){
	        Map<String,String> item = new HashMap<String,String>();
			item.put("title", myStory.getStory_date());
	        item.put("desc", myStory.getContent());
	        item.put("idx", String.valueOf(myStory.getIdx()));
	        list.add(item);
		}
		
		
		// 어댑터에 데이터 포함
		final String[] fromMapKey = new String[] { "title", "desc" };
		final int[] toLayoutId = new int[] { android.R.id.text1,
				android.R.id.text2 };
		ListAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);

        // 리스트뷰에 어댑터 연결
        ListView listView = (ListView)findViewById(R.id.myStoryListView);
        listView.setAdapter(adapter);
        //클릭이벤트 연결
        listView.setOnItemClickListener(this);

	}

	@SuppressWarnings("unchecked")
	public void onItemClick(AdapterView<?> parentView, View clickedView,
			int position, long id) {
		
		ListAdapter listAdapter = (ListAdapter) parentView.getAdapter();	//ListView에서 Adapter 받아옴
		Map<String, String> selectedItem = (Map<String, String>) listAdapter.getItem(position);	//선택한 Row에 있는 Object를 받아옴
		String idx = selectedItem.get("idx");	//Object에서 idx값을 받아옴
		
		String toastMessage = idx;

		Intent intent =new Intent(this, MyStoryPopupActivity.class);
		intent.putExtra("idx", idx);
		startActivity(intent);
		
		/*
		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
				+ " is selected. position is " + position + ", and id is " + id;*/

		Toast.makeText(getApplicationContext(), toastMessage,
				Toast.LENGTH_SHORT).show();
	}
	
}
