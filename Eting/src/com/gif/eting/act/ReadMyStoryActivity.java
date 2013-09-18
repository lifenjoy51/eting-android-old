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
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.act.view.StampView;
import com.gif.eting.dto.StampDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting.svc.task.ReceiveStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.R;

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
	private Typeface nanum;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.read_mystory_popup);
		
		nanum = Typeface.createFromAsset(getAssets(), "fonts/NanumGothic.ttf");
		

		/**
		 * 시간에따라 배경을 바꾼다 
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
		String thisHourStr = sdf.format(new Date());
		int thisHour = Integer.parseInt(thisHourStr);
		Log.i("currunt hour", thisHourStr);
		
		

		if(thisHour<6 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_4);	//파랑
		}else if(thisHour<12 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_5);	//보라
		}else if(thisHour<24 ){
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_1);	//초록
		}else{
			findViewById(R.id.popup_layout).setBackgroundResource(R.drawable.bg_4);	//파랑
		}
		
		this.context = getApplicationContext();
		
		Intent intent = getIntent();
		storyIdx = intent.getStringExtra("idx");	//파라미터값으로 넘긴 이야기 고유번호

		//Service초기화
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO myStory = storyService.getMyStory(storyIdx);	//해당하는 이야기 받아오기
		String content = myStory.getContent();
		
		String storyDate = myStory.getStory_date();
		if(storyDate != null && storyDate != ""){
			storyDate = storyDate.replaceAll("-", ".");
		}
		
		String storyTime = myStory.getStory_time();
		if(storyTime == null || storyTime == ""){
			storyTime = "0";
		}else{
			storyTime = storyTime.substring(0,2);
		}
		
		
		
		TextView storyDateView = (TextView) findViewById(R.id.mystory_content_top);
		storyDateView.setText(storyDate);
		storyDateView.setTypeface(nanum, Typeface.BOLD);
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setTypeface(nanum);
		contentView.setText(content);
		
		/**
		 * 작성시간에 맞게 배경화면 변화
		 */
		int storyHour = Integer.parseInt(storyTime);
		Log.i("currunt hour", thisHourStr);
							
		if(storyHour<6 ){
			findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.myeting_blank_b_top);
			findViewById(R.id.popup_content_scroll_area).setBackgroundResource(R.drawable.myeting_blank_b_middle);
			findViewById(R.id.mystory_content_bottom).setBackgroundResource(R.drawable.myeting_blank_b_bottom);
						
		}else if(storyHour<12 ){
			findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.myeting_blank_p_top);
			findViewById(R.id.popup_content_scroll_area).setBackgroundResource(R.drawable.myeting_blank_p_middle);
			findViewById(R.id.mystory_content_bottom).setBackgroundResource(R.drawable.myeting_blank_p_bottom);
			
		}else if(storyHour<24 ){
			findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.myeting_blank_g_top);
			findViewById(R.id.popup_content_scroll_area).setBackgroundResource(R.drawable.myeting_blank_g_middle);
			findViewById(R.id.mystory_content_bottom).setBackgroundResource(R.drawable.myeting_blank_g_bottom);
			
		}else {
			findViewById(R.id.mystory_content_top).setBackgroundResource(R.drawable.myeting_blank_b_top);
			findViewById(R.id.popup_content_scroll_area).setBackgroundResource(R.drawable.myeting_blank_b_middle);
			findViewById(R.id.mystory_content_bottom).setBackgroundResource(R.drawable.myeting_blank_b_bottom);
			
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
		findViewById(R.id.check_btn).setOnClickListener(this);
	}

	/**
	 * ReceiveStampTask 수행 후 실행되는 콜백
	 */
	private class AfterReceiveStampTask implements
			AsyncTaskCompleteListener<List<StampDTO>> {

		@Override
		public void onTaskComplete(List<StampDTO> list) {
			System.out.println("@_@!#!#");
			if (list != null) {
				if (list.size() > 0) {
					System.out.println("onTaskComplete = "+list);
					String sender = list.get(0).getSender();
					TextView contentView = (TextView) findViewById(R.id.popup_stamp_sender);
					contentView.setTypeface(nanum, Typeface.BOLD);
					
					contentView.setText("PS. "+sender);
				}else{
					ScrollView stampAreaView = (ScrollView) findViewById(R.id.mystory_stamp_scroll_area); // 스탬프영역
					stampAreaView.setVisibility(View.GONE);					
					ImageView line = (ImageView) findViewById(R.id.stamp_div_line);
					line.setVisibility(View.GONE);
					
				}
			}else{
				ScrollView stampAreaView = (ScrollView) findViewById(R.id.mystory_stamp_scroll_area); // 스탬프영역
				stampAreaView.setVisibility(View.GONE);
				ImageView line = (ImageView) findViewById(R.id.stamp_div_line);
				line.setVisibility(View.GONE);
				
			}

			/**
			 * 스탬프입력창
			 */
			LinearLayout stampArea = (LinearLayout) findViewById(R.id.mystory_stamp_area); // 스탬프영역

			// 여백
			int margin = 10;

			// 한줄에 3개씩 넣었을대 필요한 너비
			int cntPerRow = 3;

			// 처음 시작위치 설정
			int chk = 1;

			LinearLayout stampInnerLayout = initLayout();
			for (StampDTO stamp : list) {
				System.out.println("@@@@@"+stamp);
				StampView stampView = new StampView(context);
				stampView.setText(stamp.getStamp_name());
				stampView.setStamp(stamp);
				stampView.setGravity(Gravity.CENTER);
				stampView.setBackgroundResource(R.drawable.feedback);
				stampView.setTextSize(15);
				stampView.setTextColor(Color.parseColor("#474747"));
				stampView.setTypeface(nanum, Typeface.BOLD);

				LinearLayout.LayoutParams stampParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
				
				stampParams.leftMargin = margin;
				stampParams.topMargin = margin;
				stampParams.rightMargin = margin;
				stampParams.bottomMargin = margin;
				stampView.setLayoutParams(stampParams);

				System.out.println("chk = ? "+chk);
				System.out.println("cntPerRow = ? "+cntPerRow);
				if (chk != 0 && chk % cntPerRow == 0) {
					stampInnerLayout.addView(stampView);
					stampArea.addView(stampInnerLayout);
					stampInnerLayout = initLayout();
					System.out.println("new stampInnerLayout");
				} else {
					stampInnerLayout.addView(stampView);
				}
				chk++;

			}
			
			//출력해줌
			if (chk != 0 && --chk % cntPerRow != 0) {
				stampArea.addView(stampInnerLayout);				
			}
		}

		private LinearLayout initLayout() {
			LinearLayout stampInnerLayout = new LinearLayout(context);
			stampInnerLayout.setGravity(Gravity.CENTER);
			stampInnerLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			return stampInnerLayout;
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
		}else if(v.getId()==R.id.check_btn){
			finish();
		}
	}

}
