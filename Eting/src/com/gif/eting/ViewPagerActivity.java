package com.gif.eting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting.util.ServiceCompleteListener;

public class ViewPagerActivity extends Activity implements OnClickListener{
	private final int COUNT = 4; // 뷰 4개 등록 (main, write, read, setting)
	private ViewPager mPager; // 뷰 페이저
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);
		this.context = getApplicationContext();
		
		//버튼이벤트 삽입
		((ImageButton) findViewById(R.id.write_et_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.read_et_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.setting_btn)).setOnClickListener(this);
		
		
		/* 현진추가 */
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.write_et_btn:
			//startActivity(new Intent(this, WriteStoryActivity.class));
			mPager.setCurrentItem(1);
			break;
		case R.id.read_et_btn:
			//startActivity(new Intent(this, ReadMyStoryActivity.class));
			mPager.setCurrentItem(2);
			break;
		case R.id.setting_btn:
			//startActivity(new Intent(this, SettingActivity.class));
			mPager.setCurrentItem(3);
			break;
		}

	}

	//페이져 어댑터
	public class BkPagerAdapter extends PagerAdapter {
		private LayoutInflater mInflater;
		private ProgressDialog progressDialog;

		public BkPagerAdapter(Context context) {
			super();
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return COUNT;
		}
		
		//뷰 클릭이벤트
		private View.OnClickListener mPagerListener = new View.OnClickListener() {
	        @Override
	        public void onClick(View v) {
	    		switch (v.getId()) {
	    		case R.id.inbox_btn:
	    			startActivity(new Intent(context, InboxStoryPopupActivity.class));
	    			break;

	    		case R.id.send_story_btn:
	    			sendAndSaveStory();
	    			break;
	    		}
	        }
	    };
	    
	    //아이템 클릭이벤트
	    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

	    	@SuppressWarnings("unchecked")
	    	public void onItemClick(AdapterView<?> parentView, View clickedView,
	    			int position, long id) {
	    		
	    		ListAdapter listAdapter = (ListAdapter) parentView.getAdapter();	//ListView에서 Adapter 받아옴
	    		Map<String, String> selectedItem = (Map<String, String>) listAdapter.getItem(position);	//선택한 Row에 있는 Object를 받아옴
	    		String idx = selectedItem.get("idx");	//Object에서 idx값을 받아옴
	    		
	    		String toastMessage = idx;

	    		Intent intent =new Intent(context, MyStoryPopupActivity.class);
	    		intent.putExtra("idx", idx);
	    		startActivity(intent);
	    		
	    		/*
	    		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
	    				+ " is selected. position is " + position + ", and id is " + id;*/

	    		Toast.makeText(getApplicationContext(), toastMessage,
	    				Toast.LENGTH_SHORT).show();
	    	}
	    	
	    };

		// 뷰페이저에서 사용할 뷰객체 생성/등록
		@Override
		public Object instantiateItem(View pager, int position) {
			View v = null;
			if (position == 0) {
				v = mInflater.inflate(R.layout.main, null);
				//버튼이벤트 삽입
				((ImageButton) v.findViewById(R.id.inbox_btn)).setOnClickListener(mPagerListener);
			} else if (position == 1) {
				v = mInflater.inflate(R.layout.write_story, null);
				// 버튼이벤트 삽입
				((ImageButton) v.findViewById(R.id.send_story_btn))
						.setOnClickListener(mPagerListener);
			} else if (position == 2) {
				v = mInflater.inflate(R.layout.read_story, null);
				
				drawMyStoryList(v);	//자기 이야기 읽어오기
		        
			} else {
				v = mInflater.inflate(R.layout.setting, null);
			}

			((ViewPager) pager).addView(v, 0);
			Log.i("instantiateItem", String.valueOf(v.getId()));
			return v;
		}

		// 뷰 객체 삭제
		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		// instantiateItem메소드에서 생성한 객체를 이용할 것인지
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			Log.i("finishUpdate", String.valueOf(container.getId()));
			if(container.getId()== R.layout.read_story){
			}
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
		

		
		// 내 이야기 목록 그리기
		public void drawMyStoryList(View view) {
			// StoryService초기화
			StoryService storyService = new StoryService(
					context);
			List<StoryDTO> myStoryList = storyService.getMyStoryList();

			// 리스트뷰를 위한 변수들
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			for (StoryDTO myStory : myStoryList) {
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
			ListAdapter adapter = new SimpleAdapter(context, list,
					android.R.layout.simple_list_item_2, fromMapKey, toLayoutId);

			// 리스트뷰에 어댑터 연결
			ListView listView = (ListView) view.findViewById(R.id.myStoryListView);
			listView.setAdapter(adapter);
			// 클릭이벤트 연결
			listView.setOnItemClickListener(mOnItemClickListener);
		}
		
		private void sendAndSaveStory(){
			View view = mPager.findFocus();
			
			EditText et = (EditText) view.findViewById(R.id.story_content);
			String content = et.getText().toString();	//이야기 내용
			
			//전송상태 나타냄
			progressDialog = ProgressDialog.show(ViewPagerActivity.this, "", getResources().getString(R.string.app_name), true, true);

			//StoryService초기화
			StoryService storyService = new StoryService(context);
			storyService.saveStoryToServer(content, new AfterSendAndSaveStory()); // 서버에 이야기저장, 파라미터로 콜백함수 넘김
		}
		
		private class AfterSendAndSaveStory implements ServiceCompleteListener<String>{

			@Override
			public void onServiceComplete(String result) {
				Log.i("onTaskComplete", result);

				if (progressDialog != null)
					progressDialog.dismiss();

				Toast toast = Toast.makeText(context, "이야기가 전송되었습니다", Toast.LENGTH_SHORT);
				toast.show();

				// 내 이야기 읽기 화면으로 이동
				//startActivity(new Intent(context, ReadMyStoryActivity.class));
				mPager.setCurrentItem(2);
				
			}
		}
		
	}

}
