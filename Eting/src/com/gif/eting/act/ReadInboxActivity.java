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

/**
 * 받은편지함 읽기화면
 * 
 * @author lifenjoy51
 *
 */
public class ReadInboxActivity extends Activity implements OnClickListener{

	/**
	 * 받은편지함 관련 로직을 수행하는 서비스
	 */
	private InboxService inboxService;
	
	/**
	 * 스탬프관련 로직을 수행하는 서비스
	 */
	private StampService stampService;
	
	/**
	 * 받은이야기 고유번호
	 */
	private Long inboxStoryIdx;
	
	/**
	 * 프로그래스 동글이
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * 받은이야기에 달린 스탬프들
	 */
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
		
		StoryDTO inboxStory = inboxService.getInboxStory();	//받은이야기를 가져온다.
		Long idx = inboxStory.getIdx();
		inboxStoryIdx = idx;
		String content = inboxStory.getContent();
		String storyDate = inboxStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);

		//버튼이벤트 삽입
		findViewById(R.id.inbox_confirm_btn).setOnClickListener(this);
		
		//스탬프 자동완성
		//TODO 스탬프 자동완성기능은 빠진다. 
		//TODO 스탬프 입력화면을 새로 개발해야한다.
		List<StampDTO> list = stampService.getStampList();
		
		AutoCompleteTextView stampAC = (AutoCompleteTextView) findViewById(R.id.stamp_auto_complete);
		ArrayAdapter<StampDTO> adapter = new ArrayAdapter<StampDTO>(this, android.R.layout.simple_dropdown_item_1line, list);
		stampAC.setAdapter(adapter);
		// 클릭이벤트 연결
		stampAC.setOnItemClickListener(mOnItemClickListener);
	}
	
	/**
	 * 스티커 클릭시 이벤트
	 */
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
    		
    		//스탬프 추가
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
			
			/**
			 * 다른사람 이야기에 찍은 스탬프 정보를 서버로 전송
			 */
			this.saveStampsToServer();
			break;
		}
	}
	
	/**
	 * 스탬프 입력
	 * 
	 * @param stampId
	 * @param stampName
	 */
	private void addStamp(String stampId, String stampName) {
		stamps.add(stampId);
		LinearLayout stampArea = (LinearLayout) findViewById(R.id.inbox_stamp_area); // 스탬프영역

		TextView stampView = new TextView(this);
		stampView.setText(stampName);
		stampView.setGravity(Gravity.LEFT);
		
		stampArea.addView(stampView);

	}

	/**
	 * 서버로 스탬프 전송
	 */
	private void saveStampsToServer(){
		/**
		 * 서버로 스탬프 전송
		 * 
		 * SendStampTask를 생성할때 파라미터는 SendStampTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = inboxStoryIdx. 받은이야기 고유번호
		 * parameter[1] = stamps. 전송할 스탬프들
		 * parameter[2] = getApplicationContext(). Context.
		 */
		new SendStampTask(new AfterSendStampTask()).execute(String.valueOf(inboxStoryIdx), stamps, getApplicationContext());
	}
	
	/**
	 * SendStampTask 수행 후 실행되는 콜백
	 */
	private class AfterSendStampTask implements AsyncTaskCompleteListener<String>{
		@Override
		public void onTaskComplete(String result) {

			if (progressDialog != null)
				progressDialog.dismiss();
			
			finish();
			
		}
	}


}
