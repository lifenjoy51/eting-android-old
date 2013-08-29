package com.gif.eting.act;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.act.view.StampView;
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
		//TODO 스탬프 입력화면을 새로 개발해야한다.
		
		List<StampDTO> list = stampService.getStampList();
		RelativeLayout stampArea = (RelativeLayout) findViewById(R.id.inbox_stamp_area); // 스탬프영역
			
		//화면 해상도
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		Log.i("display", width + " , " + height);
		
		int preX = 0;
		int preY = 0;
		for(StampDTO stamp : list){
			StampView stampView = new StampView(this);
			stampView.setText(stamp.getStamp_name());
			stampView.setStamp(stamp);
			stampView.setGravity(Gravity.CENTER);
			stampView.setBackgroundResource(R.drawable.stamp_bg);
			stampView.setTextSize(15);
			stampView.setOnClickListener(this);
			
			//위치조정
			int objWidth = getResources().getDrawable(R.drawable.stamp_bg).getIntrinsicWidth();
			int objHeight = getResources().getDrawable(R.drawable.stamp_bg).getIntrinsicHeight();
			//Log.i("obj size", objWidth + " , " + objHeight);
			
			RelativeLayout.LayoutParams stampParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
			//stampParams.gravity = Gravity.LEFT | Gravity.TOP;
			
			int margin = 5;
			
			if(preX+objWidth > width){
				stampParams.leftMargin = 0 + margin; //Your X coordinate
				stampParams.topMargin = preY+objHeight + margin; //Your Y coordinate

				preX = 0;
				preX += objWidth;
				preY += objHeight;
			}else{
				
				stampParams.leftMargin = preX + margin; //Your X coordinate
				stampParams.topMargin = preY + margin; //Your Y coordinate
				preX += objWidth;
			}
			Log.i("stampParams", stampParams.leftMargin + " , " + stampParams.topMargin);
			/*stampParams.setMargins(10, 10, 10, 10);*/
			stampView.setLayoutParams(stampParams);
			
			stampArea.addView(stampView);
		}
		
		
/*
		//TODO 스탬프 자동완성기능은 빠진다.
		AutoCompleteTextView stampAC = (AutoCompleteTextView) findViewById(R.id.stamp_auto_complete);
		ArrayAdapter<StampDTO> adapter = new ArrayAdapter<StampDTO>(this, android.R.layout.simple_dropdown_item_1line, list);
		stampAC.setAdapter(adapter);
			
		// 클릭이벤트 연결
		stampAC.setOnItemClickListener(mOnItemClickListener);*/
	}
/*	
	*//**
	 * 스티커 클릭시 이벤트
	 *//*
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
    		
    		
    		Toast.makeText(getApplicationContext(), stampId,
    				Toast.LENGTH_SHORT).show();
    	}
    	
    };
	*/

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.inbox_confirm_btn){
			
			
			//전송상태 나타냄
			progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.app_name), true, true);
			
			/**
			 * 다른사람 이야기에 찍은 스탬프 정보를 서버로 전송
			 */
			this.saveStampsToServer();
		}else if(v  instanceof StampView){	//스탬프 클릭했을때
			
			
			StampDTO stamp = ((StampView) v).getStamp();	//선택한 Row에 있는 StampDTO를 받아옴
    		String stampId = stamp.getStamp_id();
    		String stampName = stamp.getStamp_name();
    		
    		Log.i("getItem", stampId+stampName);
    		
    		//스탬프 추가
    		if(stamps.contains(stampId)){
    			//이미 찍은거면 초기화
    			v.setBackgroundResource(R.drawable.stamp_bg);
    			stamps.remove(stampId);
    		}else{
    			//스탬프찍기
    			v.setBackgroundResource(R.drawable.stamp_bg_chk);
    			stamps.add(stampId);
    		}
    		
    		Toast.makeText(getApplicationContext(), stampId,
    				Toast.LENGTH_SHORT).show();
		}
		
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
