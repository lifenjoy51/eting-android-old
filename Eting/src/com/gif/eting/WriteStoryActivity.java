package com.gif.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gif.eting.svc.StoryService;
import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;

public class WriteStoryActivity extends Activity implements OnClickListener {
	
	private StoryService storyService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_story);

		// 버튼이벤트 삽입
		((ImageButton) findViewById(R.id.send_story_btn))
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.send_story_btn:
			sendAndSaveStory();
			break;
		}
	}
	
	private void sendAndSaveStory(){
		
		EditText et = (EditText) findViewById(R.id.story_content);
		String content = et.getText().toString();	//이야기 내용

		//StoryService초기화
		storyService = new StoryService(this.getApplicationContext());
		storyService.saveStoryToServer(content); // 서버에 이야기저장

		Toast toast = Toast.makeText(this, "이야기가 전송되었습니다", Toast.LENGTH_SHORT);
		toast.show();
		
		//내 이야기 읽기 화면으로 이동
		startActivity(new Intent(this, ReadMyStoryActivity.class));
		
	}

}
