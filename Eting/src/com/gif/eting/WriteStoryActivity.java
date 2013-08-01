package com.gif.eting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gif.eting.svc.StoryService;
import com.gif.eting.util.ServiceCompleteListener;

public class WriteStoryActivity extends Activity implements OnClickListener{
	
	private StoryService storyService;
	private ProgressDialog progressDialog;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_story);
		this.context = getApplicationContext();

		
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
		
		//전송상태 나타냄
		progressDialog = ProgressDialog.show(this, "", getResources().getString(R.string.app_name), true, true);

		//StoryService초기화
		storyService = new StoryService(this.getApplicationContext());
		storyService.saveStoryToServer(content, new AfterSendAndSaveStory()); // 서버에 이야기저장, 파라미터로 콜백함수 넘김
	}
	
	public class AfterSendAndSaveStory implements ServiceCompleteListener<String>{

		@Override
		public void onServiceComplete(String result) {
			Log.i("onTaskComplete", result);

			if (progressDialog != null)
				progressDialog.dismiss();

			Toast toast = Toast.makeText(context, "이야기가 전송되었습니다", Toast.LENGTH_SHORT);
			toast.show();

			// 내 이야기 읽기 화면으로 이동
			startActivity(new Intent(context, ReadMyStoryActivity.class));
			
		}
	}

}
