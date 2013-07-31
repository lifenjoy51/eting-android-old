package com.gif.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//버튼이벤트 삽입
		((ImageButton) findViewById(R.id.write_et_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.read_et_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.setting_btn)).setOnClickListener(this);
		((ImageButton) findViewById(R.id.inbox_btn)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.write_et_btn:
			startActivity(new Intent(this, WriteStoryActivity.class));
			break;
		case R.id.read_et_btn:
			startActivity(new Intent(this, ReadMyStoryActivity.class));
			break;
		case R.id.setting_btn:
			startActivity(new Intent(this, SettingActivity.class));
			break;
		case R.id.inbox_btn:
			startActivity(new Intent(this, InboxStoryPopupActivity.class));
			break;
		}

	}
}
