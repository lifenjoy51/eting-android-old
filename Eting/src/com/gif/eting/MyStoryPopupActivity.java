package com.gif.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
import com.gif.eting.util.ServiceCompleteListener;

public class MyStoryPopupActivity extends Activity {

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

		//StoryService√ ±‚»≠
		storyService = new StoryService(this.getApplicationContext());
		
		StoryDTO myStory = storyService.getMyStory(idx);
		String content = myStory.getContent();
		String storyDate = myStory.getStory_date();
		
		TextView contentView = (TextView) findViewById(R.id.popup_content);
		contentView.setText(content);
		
		TextView storyDateView = (TextView) findViewById(R.id.popup_date);
		storyDateView.setText(storyDate);
		
		//Ω∫≈∆«¡ ¬Ô±‚
		storyService.getStampFromServer(idx, new AfterGetStampFromServer());
	}
	
	//Ω∫≈∆«¡¬Ô±‚ Http ø‰√ª »ƒ ∑Œ¡˜
	private class AfterGetStampFromServer implements ServiceCompleteListener<String>{
		@Override
		public void onServiceComplete(String stamps) {
			TextView stampInfoView = (TextView) findViewById(R.id.stamp_info);
			stampInfoView.setText(stamps);	
		}
	}

}
