package com.gif.eting.act;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.gif.eting.svc.StoryService;
import com.gif.eting.svc.task.ReceiveStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

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
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		setContentView(R.layout.popup);
		

		/**
		 * 시간에따라 배경을 바꾼다 
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
		String thisHourStr = sdf.format(new Date());
		int thisHour = Integer.parseInt(thisHourStr);
		Log.i("currunt hour", thisHourStr);
		
		if(thisHour<1 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_1);
		}else if(thisHour<5 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_2);
		}else if(thisHour<9 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_3);
		}else if(thisHour<13 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_4);
		}else if(thisHour<17 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_5);
		}else if(thisHour<21 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_6);
		}else{
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_1);
		}
		
		this.context = getApplicationContext();
		
		Intent intent = getIntent();
		storyIdx = intent.getStringExtra("idx");	//파라미터값으로 넘긴 이야기 고유번호

		//Service초기화
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO myStory = storyService.getMyStory(storyIdx);	//해당하는 이야기 받아오기
		String content = myStory.getContent();
		String storyDate = myStory.getStory_date();
		String storyTime = myStory.getStory_time();
		if(storyTime == null || storyTime == ""){
			storyTime = "0";
		}else{
			storyTime = storyTime.substring(0,2);
		}
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(storyDate+"\n"+content);
		
		/**
		 * 작성시간에 맞게 배경화면 변화
		 */
		int storyHour = Integer.parseInt(storyTime);
		Log.i("currunt hour", thisHourStr);
		
		if(storyHour<1 ){
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_1);
		}else if(storyHour<5 ){
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_2);
		}else if(storyHour<9 ){
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_3);
		}else if(storyHour<13 ){
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_4);
		}else if(storyHour<17 ){
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_5);
		}else if(storyHour<21 ){
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_6);
		}else{
			findViewById(R.id.popup_content).setBackgroundResource(R.drawable.textbox_1);
		}
		
		/**
		 * 조회하는 이야기에 찍힌 스탬프 받아오기
		 * 
		 * ReceiveStampTask를 생성할때 파라미터는 ReceiveStampTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = idx. 이야기 고유번호.
		 */
		new ReceiveStampTask(new AfterReceiveStampTask()).execute(storyIdx);
		
		//클릭이벤트 설정
		findViewById(R.id.del_btn).setOnClickListener(this);
	}
	
	/**
	 * ReceiveStampTask 수행 후 실행되는 콜백
	 */
	private class AfterReceiveStampTask implements AsyncTaskCompleteListener<List<StampDTO>>{

		@Override
		public void onTaskComplete(List<StampDTO> list) {
			//TODO 스탬프DTO들을 받아온다. 화면에 뿌려주는 로직 만들어야함.
			
			if(list!=null){
				if(list.size()>0){
					String sender = list.get(0).getSender();
					TextView contentView = (TextView) findViewById(R.id.popup_stamp_sender);
					contentView.setText("From. "+sender);
				}
			}
			
			/**
			 * 스탬프입력창
			 */
			RelativeLayout stampArea = (RelativeLayout) findViewById(R.id.mystory_stamp_area); // 스탬프영역
				
			//화면 해상도
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels * 90 / 100 ;	//팝업창이므로 좀 줄인다
			int height = metrics.heightPixels;
			Log.i("display", width + " , " + height);
			
			int preX = 0;
			int preY = 0;
			for(StampDTO stamp : list){
				StampView stampView = new StampView(context);
				stampView.setText(stamp.getStamp_name());
				stampView.setStamp(stamp);
				stampView.setGravity(Gravity.CENTER);
				stampView.setBackgroundResource(R.drawable.feedback);
				stampView.setTextSize(15);
				
				//위치조정
				int objWidth = getResources().getDrawable(R.drawable.feedback).getIntrinsicWidth();
				int objHeight = getResources().getDrawable(R.drawable.feedback).getIntrinsicHeight();
				//Log.i("obj size", objWidth + " , " + objHeight);
				
				RelativeLayout.LayoutParams stampParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
				//stampParams.gravity = Gravity.LEFT | Gravity.TOP;
				
				int margin = 10;
				
				if(preX+objWidth > width){
					stampParams.leftMargin = 0 + margin; //Your X coordinate
					stampParams.topMargin = preY+objHeight + margin + margin; //Your Y coordinate

					preX = 0;
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
			
			//stampInfoView.setText(result);	
		}
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
									Toast.makeText(context, "삭제되었습니다", Toast.LENGTH_SHORT).show();
									
									//돌아가기
									finish();								}

							}).setNegativeButton(R.string.no, null).show();
		}
	}

}
