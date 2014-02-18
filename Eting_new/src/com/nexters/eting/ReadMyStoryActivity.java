package com.nexters.eting;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nexters.eting.etc.Const;
import com.nexters.eting.etc.HttpUtil;
import com.nexters.eting.obj.Story;
import com.nexters.eting.svc.StoryService;
import com.nexters.eting.view.EmoticonView;

/**
 * 내 이야기 목록에서 선택한 이야기를 읽는 화면
 * 
 * @author lifenjoy51
 *
 */
public class ReadMyStoryActivity extends Activity implements OnClickListener{

	private StoryService storyService;
	private String storyIdx;
	private Context context;
	private String replyId;	
	private String reply;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_readmystory);
		

		/**
		 * 시간에따라 배경을 바꾼다 
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
		String thisHourStr = sdf.format(new Date());
		int thisHour = Integer.parseInt(thisHourStr);
		// Log.i("currunt hour", thisHourStr);
		
		

		if(thisHour<12 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_purple);	//보라
		}else if(thisHour<24 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_green);	//초록
		}else{
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_blue);	//파랑
		}
		
		this.context = getApplicationContext();
		
		Intent intent = getIntent();
		storyIdx = intent.getStringExtra("idx");	//파라미터값으로 넘긴 이야기 고유번호
		Log.i("gcm storyId", "ReadMyStory Activity = "+storyIdx);
		
		//Service초기화
		storyService = new StoryService(this.getApplicationContext());
		
		try{
			//읽음 표시
			storyService.updStoryReplyRead(storyIdx);
			Story myStory = storyService.getMyStory(storyIdx);	//해당하는 이야기 받아오기
			
			//코멘트아이디
			replyId = myStory.getReply_id();
			
			String content = myStory.getStory_content();
			
			String storyDate = myStory.getStory_date();
			if(storyDate != null && storyDate != ""){
				storyDate = storyDate.replaceAll("-", ".");
			}
			
			String storyTime = myStory.getStory_time();
			String storyHH = "";
			String storyHHMM = "";
			if(storyTime == null || storyTime == ""){
				storyHH = "0";
			}else{
				storyHHMM = storyTime.substring(0,5);
				storyHH = storyTime.substring(0,2);				
			}
			
			
			
			String storyDateTime = storyDate + "\t\t" + storyHHMM;
			
			TextView storyDateView = (TextView) findViewById(R.id.mystory_content_top);
			storyDateView.setText(storyDateTime);
			TextView contentView = (TextView) findViewById(R.id.popup_content);
			contentView.setText(content);
			
			/**
			 * 작성시간에 맞게 배경화면 변화
			 */
			int storyHour = Integer.parseInt(storyHH);
			//Log.i("currunt hour", thisHourStr);
								
			if(storyHour<12 ){
				findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.contents_bg01_p);
				
			}else if(storyHour<24 ){
				findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.contents_bg01_g);
				
			}else {
				findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.contents_bg01_b);
				
			}
			
			/**
			 * 조회하는 이야기에 찍힌 스탬프 받아오기
			 * 
			 * ReceiveReplyTask를 생성할때 파라미터는 ReceiveReplyTask가 수행되고 나서 실행될 콜백이다.
			 * execute의 파라미터가 실제 넘겨줄 자료들.
			 * parameter[0] = idx. 이야기 고유번호.
			 */
			//new ReceiveReplyTask(new AfterReceiveReplyTask()).execute(storyIdx);
			drawReplyArea();
			
			//클릭이벤트 설정
			findViewById(R.id.del_btn).setOnClickListener(this);
			findViewById(R.id.check_btn).setOnClickListener(this);
			findViewById(R.id.reply_report_btn).setOnClickListener(this);
			findViewById(R.id.reply_del_btn).setOnClickListener(this);
			
			findViewById(R.id.del_btn).bringToFront();
		}catch(Exception e){
			//Log.i("read my story activity error", e.toString());
			e.printStackTrace();
			Toast.makeText(context, R.string.have_problem, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}		

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.del_btn){
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.delete_story)
					.setMessage(R.string.really_delete_story)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									//삭제버튼을 클릭했을 때
									storyService.delStory(storyIdx);
									
									//삭제 후 로직
									Toast.makeText(context, R.string.delete_completed, Toast.LENGTH_SHORT).show();
									
									//돌아가기
									finish();								}

							}).setNegativeButton(R.string.no, null).show();
			/**
			 * 코멘트 신고 기능
			 */
		}else if(v.getId()==R.id.reply_report_btn){
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.reply_report)
					.setMessage(R.string.really_reply_report)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									//삭제버튼을 클릭했을 때
									onReportOrDeleteComment("R");
									
									//신고 후 로직
									Toast.makeText(context, R.string.report_completed, Toast.LENGTH_SHORT).show();
									
									//돌아가기
									finish();								}

							}).setNegativeButton(R.string.no, null).show();
			/**
			 * 코멘트 삭제기능
			 */
		}else if(v.getId()==R.id.reply_del_btn){
			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.reply_del)
					.setMessage(R.string.really_reply_del)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									//삭제버튼을 클릭했을 때
									onReportOrDeleteComment("D");
									
									//삭제 후 로직
									Toast.makeText(context, R.string.delete_completed, Toast.LENGTH_SHORT).show();
									
									//돌아가기
									finish();								}

							}).setNegativeButton(R.string.no, null).show();
		}else if(v.getId()==R.id.check_btn){
			finish();
		}
	}
	
	/**
	 * 스탬프정보 출력
	 * 
	 */
	public void drawReplyArea(){
		List<Integer> list=  storyService.getEmoticons(storyIdx);
		
		if (list != null) {
			if (list.size() > 0) {
				//System.out.println("onTaskComplete = "+list);
				reply = storyService.getReplyContent(storyIdx);
				TextView contentView = (TextView) findViewById(R.id.popup_stamp_reply);
				
				contentView.setText("P.S. "+reply);
			}else{
				LinearLayout reply_area = (LinearLayout) findViewById(R.id.reply_area); // 스탬프영역
				reply_area.setVisibility(View.GONE);
				
			}
		}

		/**
		 * 스탬프입력창
		 */
		LinearLayout emoticonArea = (LinearLayout) findViewById(R.id.mystory_stamp_area); // 스탬프영역

		for(Integer emoticonId : list){
			EmoticonView ev = new EmoticonView(context);
			ev.setBackgroundResource(emoticonId);
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
			lParams.gravity = Gravity.CENTER_HORIZONTAL;
			lParams.weight = 0;
			ev.setLayoutParams(lParams);
			emoticonArea.addView(ev);
			emoticonArea.addView(getDummyView());
		}
	}
	
	private View getDummyView(){
		View v = new View(context);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
		lParams.weight = 1;
		v.setLayoutParams(lParams);
		return v;
	}
	
	/**
	 * 답글 삭제 or 신고 시 로직
	 * @param flag
	 */
	private void onReportOrDeleteComment(String flag){
		storyService.deleteComment(storyIdx);
	
		//삭제한 이야기 서버로 전송~~ 신고인지 삭제인지 값에 맞게!!
		new ResendStroyTask().execute(flag);
	}
	
	 class ResendStroyTask extends AsyncTask<Object, String, String> {
			
			/**
			 * 생성자로 콜백을 받아온다.
			 * 
			 * @param callback
			 */
			public ResendStroyTask(){
			}
			
			/**
			 * 실제 실행되는 부분
			 */
			@Override
			protected String doInBackground(Object... params) {

					String urlStr = Const.serverContext+"/reportComment";
					
					String param = "reply_id=" + replyId;	//코멘트 번호
					param += "&reply_content=" + reply;	//코멘트 내용
					param += "&story_id=" + storyIdx;	//코멘트 내용
					param += "&flag=" + String.valueOf(params[0]);	//신고인지 삭제인지
					
					String response = HttpUtil.doPost(urlStr, param);	//Http전송 
					//System.out.println(this.getClass().getName() + " = " + response);
					return response;
			}

			/**
			 * 작업이 끝나면 자동으로 실행된다.
			 */
			@Override
			protected void onPostExecute(String result) {
				
			}

		}
}
