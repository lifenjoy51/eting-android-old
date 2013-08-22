package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gif.eting.R;
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
public class ReadMyStoryActivity extends Activity {

	private StoryService storyService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount = 0.7f;
		getWindow().setAttributes(layoutParams);
		setContentView(R.layout.popup);
		
		Intent intent = getIntent();
		String idx = intent.getStringExtra("idx");	//파라미터값으로 넘긴 이야기 고유번호

		//Service초기화
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO myStory = storyService.getMyStory(idx);	//해당하는 이야기 받아오기
		String content = myStory.getContent();
		String storyDate = myStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);
		
		/**
		 * 조회하는 이야기에 찍힌 스탬프 받아오기
		 * 
		 * ReceiveStampTask를 생성할때 파라미터는 ReceiveStampTask가 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = idx. 이야기 고유번호.
		 */
		new ReceiveStampTask(new AfterReceiveStampTask()).execute(idx);
		
	}
	
	/**
	 * ReceiveStampTask 수행 후 실행되는 콜백
	 */
	private class AfterReceiveStampTask implements AsyncTaskCompleteListener<String>{

		@Override
		public void onTaskComplete(String result) {
			//TODO 스탬프를 어떻게 보여줄것인가 생각해봐야함. 현재는 그냥 텍스트를 집어넣는다.
			TextView stampInfoView = (TextView) findViewById(R.id.stamp_info);
			stampInfoView.setText(result);	
		}
	}

}
