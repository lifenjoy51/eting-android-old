package com.gif.eting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gif.eting.util.HttpUtil;
import com.gif.eting.util.Installation;

public class WriteStoryActivity extends Activity implements OnClickListener {
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
			test();			
			//saveStoryToServer(); // 서버에 이야기저장
			break;
		}

	}

	private void saveStoryToServer() {
		this.createSessioin();

		HttpUtil http = new HttpUtil();
		EditText et = (EditText) findViewById(R.id.story_content);
		String content = et.getText().toString();
		String params = "content=" + content;
		String response = http.doPost(
				"http://lifenjoys.cafe24.com/story/insert", params);
		Log.i("http response", response);

		Toast toast = Toast.makeText(this, response, Toast.LENGTH_SHORT);
		toast.show();
	}

	private void createSessioin() {
		HttpUtil http = new HttpUtil();
		Context applicationContext = this.getApplicationContext();
		String phoneId = Installation.id(applicationContext);
		Log.i("phoneId", phoneId);
		String params = "phoneId=" + phoneId;
		String response = http.doPost("http://lifenjoys.cafe24.com/story/main",
				params);
		// Log.i("http response", response);

		Toast toast = Toast.makeText(this, response, Toast.LENGTH_SHORT);
		toast.show();

	}

	private void test() {
		Context applicationContext = this.getApplicationContext();
		String phoneId = Installation.id(applicationContext);
		Log.i("phoneId", phoneId);
		
		HttpUtil http = new HttpUtil();
		String params = "";
		String response = http.doPost(
				"http://lifenjoys.cafe24.com/exp/expInfoSimpleList", params);
		parsing(response);

	}

	private void parsing(String response) {
		try {
			JSONObject json = new JSONObject(response);
			JSONArray list = json.getJSONArray("list");
			for(int i=0; i<list.length(); i++){
				JSONObject obj = list.getJSONObject(i);
				String expName = obj.getString("exp_name");
				String expMemo = obj.getString("exp_memo");
				Log.i("json test", expName + " _ " + expMemo);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
