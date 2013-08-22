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

public class ReadMyStory extends Activity {

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
		String idx = intent.getStringExtra("idx");		

		//Service초기화
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO myStory = storyService.getMyStory(idx);
		String content = myStory.getContent();
		String storyDate = myStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);
		
		//스탬프 받아오기
		new ReceiveStampTask(new AfterGetStampFromServer()).execute(idx);
		
	}
	
	//스탬프찍기 Http 요청 후 로직
	private class AfterGetStampFromServer implements AsyncTaskCompleteListener<String>{

		@Override
		public void onTaskComplete(String result) {
			TextView stampInfoView = (TextView) findViewById(R.id.stamp_info);
			stampInfoView.setText(result);	
		}
	}

}
