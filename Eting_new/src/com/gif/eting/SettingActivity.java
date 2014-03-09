package com.gif.eting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingActivity extends BaseActivity implements OnClickListener {


	private ImageView alarm_img_btn;
	private ImageView password_img_btn;
	private ImageView email_btn;
	private ImageView credit_img_btn;
	private ImageView tutorialBtn;
	private TextView setting_textView;
	
	//설정 저장소
	SharedPreferences pref;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//레이아웃 설정
		setContentView(R.layout.activity_setting);
		
		//저장소 초기화
		pref = getSharedPreferences("eting", Context.MODE_PRIVATE);

		// 버튼 온클릭이벤트 등록
		alarm_img_btn = (ImageView) findViewById(R.id.alarm_img_btn);
		password_img_btn = (ImageView) findViewById(R.id.password_img_btn);
		email_btn = (ImageView) findViewById(R.id.backup_btn);
		credit_img_btn = (ImageView) findViewById(R.id.credit_img_btn);
		tutorialBtn = (ImageView) findViewById(R.id.tutorial_btn);
		setting_textView = (TextView) findViewById(R.id.setting_textView);

		//알람설정
    	boolean alarm =pref.getBoolean("push_alarm", true);
    	
    	//알람설정 off면 이미지 교체
    	if(alarm){
    		alarm_img_btn.setImageResource(R.drawable.setting_btn_alarm_on);
    	}else{
    		alarm_img_btn.setImageResource(R.drawable.setting_btn_alarm_off);
    	}

		// 글시 굵게
		setting_textView.setPaintFlags(setting_textView.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);

		//클릭이벤트 설정
		alarm_img_btn.setOnClickListener(this);
		password_img_btn.setOnClickListener(this);
		email_btn.setOnClickListener(this);
		credit_img_btn.setOnClickListener(this);
		tutorialBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		// 비밀번호관리 버튼 클릭시
		if (view.getId() == R.id.password_img_btn) {
			startActivity(new Intent(this, PasswordActivity.class));
		}
		// 이메일첨부 버튼 클릭시
		if (view.getId() == R.id.backup_btn) {
			startActivity(new Intent(this, BackupActivity.class));
		}
		if (view.getId() == R.id.credit_img_btn) {
			startActivity(new Intent(this, AboutUsActivity.class));
		}
		if (view.getId() == R.id.alarm_img_btn) {
			boolean alarm =pref.getBoolean("push_alarm", true);

			if (alarm) {
				alarm_img_btn.setImageResource(R.drawable.setting_btn_alarm_off);
				pref.edit().putBoolean("push_alarm", false).commit();	
			} else {
				alarm_img_btn.setImageResource(R.drawable.setting_btn_alarm_on);
				pref.edit().putBoolean("push_alarm", true).commit();
			}
		}
		if (view.getId() == R.id.tutorial_btn) {
			startActivity(new Intent(this, TutorialActivity.class));
		}

	}

}