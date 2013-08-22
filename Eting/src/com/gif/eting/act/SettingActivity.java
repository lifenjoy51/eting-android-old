package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.gif.eting.R;

public class SettingActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		// 버튼 온클릭이벤트 등록
		findViewById(R.id.password_img_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// 비밀번호관리 버튼 클릭시
		if (view.getId() == R.id.password_img_btn) {
			startActivity(new Intent(this, PasswordManagementActivity.class));
		}
	}


}
