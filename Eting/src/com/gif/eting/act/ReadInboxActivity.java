package com.gif.eting.act;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gif.eting.act.view.StampView;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.InboxService;
import com.gif.eting.svc.StampService;
import com.gif.eting.svc.task.SendStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.R;

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
	/**
	 * 보낸사람
	 */
	private String sender;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);

		/*LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View pop = inflater.inflate(R.layout.inbox_popup, null, false);*/
		setContentView(R.layout.read_inbox_popup);
		
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
		
		/**
		 * 스탬프입력창
		 */
		List<StampDTO> list = stampService.getStampList();
		RelativeLayout stampArea = (RelativeLayout) findViewById(R.id.inbox_stamp_area); // 스탬프영역
			
		//화면 해상도
		DisplayMetrics metrics = this.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;	//팝업창이므로 좀 줄인다
		int height = metrics.heightPixels;

		//pop.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		//int width = pop.getMeasuredWidth();
		//int height = pop.getMeasuredHeight();
		
		Log.i("display", width + " , " + height);
		
		int preX = 0;
		int preY = 0;
		
		//스탬프크기
		int objWidth = getResources().getDrawable(R.drawable.feedback).getIntrinsicWidth();
		int objHeight = getResources().getDrawable(R.drawable.feedback).getIntrinsicHeight();
		
		//여백		
		int margin = 10;
		
		//한줄에 3개씩 넣었을대 필요한 너비
		int cntPerRow = 3;
		int widthPerRow = (objWidth+margin)*cntPerRow;		
		int leftMargin = (width - widthPerRow) / 2 /2;
		
		//처음 시작위치 설정
		preX = leftMargin - margin;
		int chk = 0;
		
		for(StampDTO stamp : list){
			
			StampView stampView = new StampView(this);
			stampView.setText(stamp.getStamp_name());
			stampView.setStamp(stamp);
			stampView.setGravity(Gravity.CENTER);
			stampView.setBackgroundResource(R.drawable.feedback);
			stampView.setTextSize(15);
			stampView.setOnClickListener(this);
			
			RelativeLayout.LayoutParams stampParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
						
			if(chk++ % cntPerRow == 0){
				stampParams.leftMargin = leftMargin + margin; //Your X coordinate
				stampParams.topMargin = preY+objHeight + margin + margin; //Your Y coordinate

				preX = leftMargin;
				preX += objWidth + margin;
				preY += objHeight + margin;
			}else{
				
				stampParams.leftMargin = preX + margin; //Your X coordinate
				stampParams.topMargin = preY + margin; //Your Y coordinate
				preX += objWidth + margin;
			}
			
			Log.i("stampParams", stampParams.leftMargin + " , " + stampParams.topMargin);
			/*stampParams.setMargins(10, 10, 10, 10);*/
			stampView.setLayoutParams(stampParams);
			
			stampArea.addView(stampView);
		}
		
		
	}

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
    			//((StampView) v).setTextColor(color.black);
    			v.setBackgroundResource(R.drawable.feedback);
    			stamps.remove(stampId);
    		}else{
    			//스탬프찍기
    			//((StampView) v).setTextColor(color.white);
    			v.setBackgroundResource(R.drawable.feedback_chk);
    			stamps.add(stampId);
    		}
    		
		}
		
	}

	/**
	 * 서버로 스탬프 전송
	 */
	private void saveStampsToServer(){
		/**
		 * 보낸이 설정
		 */
		EditText et = (EditText) findViewById(R.id.stamp_sender);
		sender = et.getText().toString();
		
		/**
		 * 서버로 스탬프 전송
		 * 
		 * SendStampTask를 생성할때 파라미터는 SendStampTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = inboxStoryIdx. 받은이야기 고유번호
		 * parameter[1] = stamps. 전송할 스탬프들
		 * parameter[2] = getApplicationContext(). Context.
		 * parameter[3] = sender. 보낸사람 이름(별명).
		 */
		new SendStampTask(new AfterSendStampTask()).execute(String.valueOf(inboxStoryIdx), stamps, getApplicationContext(), sender);
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
