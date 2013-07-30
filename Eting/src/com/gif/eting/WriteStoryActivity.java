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
			saveStoryToServer(); // 서버에 이야기저장
			break;
		}

	}

	//서버에 저장 후 자료 받아옴
	private void saveStoryToServer() {
		String phoneId = Installation.id(this.getApplicationContext());	//기기 고유값
		
		EditText et = (EditText) findViewById(R.id.story_content);
		String content = et.getText().toString();	//이야기 내용

		HttpUtil http = new HttpUtil();
		String params = "phone_id=" + phoneId+"&content=" + content;	//파라미터 설정
		
		//String response = http.doPost("http://lifenjoys.cafe24.com/eting/insert", params);	//서버주소
		String response = http.doPost("http://112.144.52.47:8080/eting/insert", params);	//개발서버주소
		
		Log.i("json response", response);
		
		//StoryService초기화
		storyService = new StoryService(this.getApplicationContext());
		storyService.saveToPhoneDB(response);
		storyService.dbTest();

		Toast toast = Toast.makeText(this, "이야기가 전송되었습니다", Toast.LENGTH_SHORT);
		toast.show();
		
		//내 이야기 읽기 화면으로 이동
		startActivity(new Intent(this, ReadMyStoryActivity.class));
	}

}
