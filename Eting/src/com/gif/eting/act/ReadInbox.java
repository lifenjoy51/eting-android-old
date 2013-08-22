package com.gif.eting.act;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.InboxService;
import com.gif.eting.svc.StampService;
import com.gif.eting.svc.task.SendStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

public class ReadInbox extends Activity implements OnClickListener{

	private InboxService inboxService;
	private StampService stampService;
	
	private Long inboxStoryIdx;
	private ProgressDialog progressDialog;
	private List<String> stamps = new ArrayList<String>();;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		setContentView(R.layout.inbox_popup);
		
		//Service초기화
		inboxService = new InboxService(this.getApplicationContext());
		stampService = new StampService(this.getApplicationContext());
		
		StoryDTO inboxStory = inboxService.getInboxStory();
		Long idx = inboxStory.getIdx();
		inboxStoryIdx = idx;
		String content = inboxStory.getContent();
		String storyDate = inboxStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);

		//버튼이벤트 삽입
		((Button) findViewById(R.id.inbox_confirm_btn)).setOnClickListener(this);
		
		//스탬프 자동완성
		List<StampDTO> list = stampService.getStampList();
		
		AutoCompleteTextView stampAC = (AutoCompleteTextView) findViewById(R.id.stamp_auto_complete);
		ArrayAdapter<StampDTO> adapter = new ArrayAdapter<StampDTO>(this, android.R.layout.simple_dropdown_item_1line, list);
		stampAC.setAdapter(adapter);
		// 클릭이벤트 연결
		stampAC.setOnItemClickListener(mOnItemClickListener);
	}
	
	 //아이템 클릭이벤트
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

    	@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parentView, View clickedView,
    			int position, long id) {

    		Log.i("position",String.valueOf(position));
    		Log.i("id",String.valueOf(id));
			
    		ArrayAdapter<StampDTO> adapter = (ArrayAdapter<StampDTO>) parentView.getAdapter();	// Adapter 받아옴
    		StampDTO stamp = adapter.getItem(position);	//선택한 Row에 있는 StampDTO를 받아옴
    		String stampId = stamp.getStamp_id();
    		String stampName = stamp.getStamp_name();
    		
    		Log.i("getItem", stampId+stampName);
    		
    		/*
    		String toastMessage = ((TwoLineListItem) clickedView).getText1().getText()
    				+ " is selected. position is " + position + ", and id is " + id;*/
    		
    		addStamp(stampId, stampName);
    		
    		Toast.makeText(getApplicationContext(), stampId,
    				Toast.LENGTH_SHORT).show();
    	}
    	
    };
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.inbox_confirm_btn:
			
			//전송상태 나타냄
			progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.app_name), true, true);
			
			//스탬프1
			//storyService.saveStampToServer(String.valueOf(inboxStoryIdx), "5", new AfterSaveStampToServer());	//스탬프ID 5는 하드코딩 추후 변경필요
			saveStampsToServer();
			break;
		}
	}
	// 스탬프 입력
	private void addStamp(String stampId, String stampName) {
		stamps.add(stampId);
		LinearLayout stampArea = (LinearLayout) findViewById(R.id.inbox_stamp_area); // 스탬프영역

		TextView stampView = new TextView(this);
		stampView.setText(stampName);
		stampView.setGravity(Gravity.LEFT);
		
		stampArea.addView(stampView);

	}

	private void saveStampsToServer(){
		new SendStampTask(new AfterSaveStampToServer()).execute(String.valueOf(inboxStoryIdx), stamps, getApplicationContext());
		//stampService.saveStampToServer(String.valueOf(inboxStoryIdx), stamps, new AfterSaveStampToServer());
	}
	
	//스탬프찍기 Http 요청 후 로직
	private class AfterSaveStampToServer implements AsyncTaskCompleteListener<String>{
		@Override
		public void onTaskComplete(String result) {

			if (progressDialog != null)
				progressDialog.dismiss();
			
			finish();
			
		}
	}


}
