package com.gif.eting.act;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.act.view.StampView;
import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.InboxService;
import com.gif.eting.svc.StampService;
import com.gif.eting.svc.task.ReportStroyTask;
import com.gif.eting.svc.task.SendStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Util;

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
	private String comment;
	
	/**
	 * 코멘트
	 */
	private EditText et;
	
	private Context context;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);		
		context = getApplicationContext();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);

		setContentView(R.layout.read_inbox_popup);
		
		/**
		 * 짧은이야기 적는 부분
		 */
		et = (EditText) findViewById(R.id.stamp_comment);
		//et.setTypeface(Util.getNanum(getApplicationContext()));
		
		//Service초기화
		inboxService = new InboxService(this.getApplicationContext());
		stampService = new StampService(this.getApplicationContext());
		
		StoryDTO inboxStory = inboxService.getInboxStory();	//받은이야기를 가져온다.
		Long idx = inboxStory.getIdx();
		inboxStoryIdx = idx;
		String content = inboxStory.getContent();
		String storyDate = inboxStory.getStory_date();
		if(storyDate != null && storyDate != ""){
			storyDate = storyDate.replaceAll("-", ".");
		}
		String storyTime = inboxStory.getStory_time();
		if(storyTime != null && storyTime != ""){
			storyTime = storyTime.substring(0, 5);
		}
		
		String storyDateTime = storyDate + "\t\t" + storyTime;
		
		//스크린크기
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		//contentView.setTypeface(Util.getNanum(getApplicationContext()));
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		//storyDateView.setTypeface(Util.getNanum(getApplicationContext()), Typeface.BOLD);
		storyDateView.setText(storyDateTime);

		//버튼이벤트 삽입
		findViewById(R.id.inbox_confirm_btn).setOnClickListener(this);		
		findViewById(R.id.report_btn).setOnClickListener(this);		
		findViewById(R.id.pass_btn).setOnClickListener(this);		
		
		/**
		 * 스탬프입력창
		 */
		List<StampDTO> list = stampService.getStampList();
		LinearLayout stampArea = (LinearLayout) findViewById(R.id.inbox_stamp_area); // 스탬프영역
			
		//여백		
		int margin = 10;
		
		//한줄에 3개씩 넣었을대 필요한 너비
		int cntPerRow = 3;
		
		//처음 시작위치 설정
		int chk = 1;

		LinearLayout stampInnerLayout = initLayout();
		for(StampDTO stamp : list){
			
			StampView stampView = new StampView(this);
			stampView.setText(stamp.getStamp_name());
			stampView.setStamp(stamp);
			stampView.setGravity(Gravity.CENTER);
			stampView.setBackgroundResource(R.drawable.feedback);
			stampView.setTextSize(15);
			stampView.setTextColor(Color.parseColor("#999999"));
			//stampView.setTypeface(Util.getNanum(getApplicationContext()), Typeface.BOLD);
			stampView.setOnClickListener(this);
			
			LinearLayout.LayoutParams stampParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
			stampParams.leftMargin =  margin;
			stampParams.topMargin =  margin;
			stampParams.rightMargin =  margin;
			stampParams.bottomMargin =  margin;
			stampView.setLayoutParams(stampParams);
			
			if(chk != 0 && chk % cntPerRow == 0){
				stampInnerLayout.addView(stampView);
				stampArea.addView(stampInnerLayout);
				stampInnerLayout = initLayout();
				//System.out.println("new stampInnerLayout");
			}else{
				stampInnerLayout.addView(stampView);
			}
			chk++;
		}
		
		//출력해줌
		if (chk != 0 && chk % cntPerRow != 0) {
			stampArea.addView(stampInnerLayout);				
		}
				
	}
	
	private LinearLayout initLayout(){
		LinearLayout stampInnerLayout = new LinearLayout(this);
		stampInnerLayout.setGravity(Gravity.CENTER);
		stampInnerLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return stampInnerLayout;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.inbox_confirm_btn){
			Log.d("et length", String.valueOf(et.getText().toString().length()));
			if(stamps.size()==0){
				
				//스탬프 없으면 자동스탬프
				stamps.add("1");
				
				/*
				Toast toast = Toast.makeText(context, R.string.select_stamp_plz,
						Toast.LENGTH_SHORT);
				toast.show();
				return;
				*/
				
				
			}
			
			if(et.getText().toString().length() < 2){
				Toast toast = Toast.makeText(context, R.string.enter_comment_plz,
						Toast.LENGTH_SHORT);
				toast.show();
				return;
			}
			
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
    		
    		//Log.i("getItem", stampId+stampName);
    		
    		//스탬프 추가
    		if(stamps.contains(stampId)){
    			//이미 찍은거면 초기화
    			//((StampView) v).setTextColor(color.black);
    			v.setBackgroundResource(R.drawable.feedback);
    			((StampView) v).setTextColor(Color.parseColor("#999999"));
    			stamps.remove(stampId);
    		}else{
    			//스탬프찍기
    			//((StampView) v).setTextColor(color.white);
    			v.setBackgroundResource(R.drawable.feedback_chk);
    			((StampView) v).setTextColor(Color.WHITE);
    			stamps.add(stampId);
    		}
    		
		} else if (v.getId() == R.id.report_btn) {

			// Ask the user if they want to quit
			new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.report_title)
					.setMessage(R.string.report_message)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// 전송상태 나타냄
									progressDialog = ProgressDialog.show(
											ReadInboxActivity.this,
											"",
											getResources().getString(
													R.string.app_name), true,
											true);
									// 신고기능
									reportStory();
									// 돌아가기
									finish();
								}

							}).setNegativeButton(R.string.no, null).show();

		}else if (v.getId() == R.id.pass_btn) {
			// Ask the user if they want to quit
						new AlertDialog.Builder(this)
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setTitle(R.string.pass_title)
								.setMessage(R.string.pass_message)
								.setPositiveButton(R.string.yes,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog,
													int which) {

												// 전송상태 나타냄
												progressDialog = ProgressDialog.show(
														ReadInboxActivity.this,
														"",
														getResources().getString(
																R.string.app_name), true,
														true);
												// 신고기능
												passStory();
												// 돌아가기
												finish();
											}

										}).setNegativeButton(R.string.no, null).show();

		}
		
	}

	/**
	 * 서버로 스탬프 전송
	 */
	private void saveStampsToServer(){
		/**
		 * 보낸이 설정
		 */
		et = (EditText) findViewById(R.id.stamp_comment);
		comment = et.getText().toString();
		
		/**
		 * 서버로 스탬프 전송
		 * 
		 * SendStampTask를 생성할때 파라미터는 SendStampTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = inboxStoryIdx. 받은이야기 고유번호
		 * parameter[1] = stamps. 전송할 스탬프들
		 * parameter[2] = getApplicationContext(). Context.
		 * parameter[3] = comment. 보낸사람 이름(별명).
		 */
		new SendStampTask(new AfterSendStampTask()).execute(String.valueOf(inboxStoryIdx), stamps, getApplicationContext(), comment);
	}

	/**
	 * SendStampTask 수행 후 실행되는 콜백
	 */
	private class AfterSendStampTask implements
			AsyncTaskCompleteListener<String> {
		@Override
		public void onTaskComplete(String result) {

			if (progressDialog != null)
				progressDialog.dismiss();

			/**
			 * 에러처리
			 */
			if ("HttpUtil_Error".equals(result)) {
				Toast toast = Toast.makeText(context, R.string.error_on_transfer ,
						Toast.LENGTH_LONG);
				toast.show();

			} else if ("UnknownHostException".equals(result)) {
				Toast toast = Toast.makeText(context, R.string.cannot_connect_to_internet,
						Toast.LENGTH_LONG);
				toast.show();

			} else if ("Success".equals(result)) {
				// 정상으로 전송되었을 때.
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Service.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				 
				finish();

			} else {
				// ???
			}
		}
	}
	
	
	/**
	 * 이야기 신고기능
	 * 
	 * ReportStroyTask를 생성할때 파라미터는 ReportStroyTask가 수행되고 나서 실행될 콜백이다.
	 * execute의 파라미터가 실제 넘겨줄 자료들.
	 * parameter[0] = inboxStoryIdx. 받은이야기 고유번호
	 */
	private void reportStory(){
		new ReportStroyTask(new AfterReportStoryTask()).execute(String.valueOf(inboxStoryIdx), getApplicationContext());
	}
	
	/**
	 * ReportStroyTask 수행 후 실행되는 콜백
	 */
	private class AfterReportStoryTask implements
			AsyncTaskCompleteListener<String> {
		@Override
		public void onTaskComplete(String result) {

			if (progressDialog != null)
				progressDialog.dismiss();

			/**
			 * 에러처리
			 */
			if ("HttpUtil_Error".equals(result)) {
				Toast toast = Toast.makeText(context, R.string.error_on_transfer,
						Toast.LENGTH_LONG);
				toast.show();

			} else if ("UnknownHostException".equals(result)) {
				Toast toast = Toast.makeText(context, R.string.cannot_connect_to_internet,
						Toast.LENGTH_LONG);
				toast.show();

			} else if ("Success".equals(result)) {
				Toast toast = Toast.makeText(context, R.string.report_completed,
						Toast.LENGTH_SHORT);
				toast.show();
				
				// 정상으로 전송되었을 때.
				finish();

			} else {
				// ???
			}
		}
	}
	
	/**
	 * 이야기 패스기능
	 */
	private void passStory(){
		new PassStroyTask().execute();
	}
	
	class PassStroyTask extends AsyncTask<Object, String, String> {
		
		private String storyId;
		
		/**
		 * 생성자로 콜백을 받아온다.
		 * 
		 * @param callback
		 */
		public PassStroyTask(){
		}
		
		/**
		 * 실제 실행되는 부분
		 */
		@Override
		protected String doInBackground(Object... params) {

				String urlStr = Util.serverContext+"/passStory";
				
				this.storyId = String.valueOf(inboxStoryIdx);
				String param = "story_id=" + storyId;	//파라미터 첫번째값 storyId
				
				String response = HttpUtil.doPost(urlStr, param);	//Http전송 
				//System.out.println(this.getClass().getName() + " = " + response);
				return response;
		}

		/**
		 * 작업이 끝나면 자동으로 실행된다.
		 */
		@Override
		protected void onPostExecute(String result) {
			//이야기 삭제
			StoryDTO inboxStory = new StoryDTO();
			inboxStory.setIdx(Long.parseLong(storyId));
	
			InboxDAO inboxDao = new InboxDAO(context);
			
			// 스탬프찍은 이야기 삭제
			inboxDao.open();
			inboxDao.delStory(inboxStory);
			inboxDao.close();
			
		}
	}


}
