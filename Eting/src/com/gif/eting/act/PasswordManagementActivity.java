package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.svc.PasswordService;

public class PasswordManagementActivity extends Activity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_manage);

		// 버튼 온클릭이벤트 등록
		((Button) findViewById(R.id.setting_save_pw_btn))
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// 암호확인 버튼 클릭시
		if (view.getId() == R.id.setting_save_pw_btn) {
			savePassword();
		}
	}

	// 세팅화면에서 비밀번호 저장
	public void savePassword() {
		boolean isValid = false;

		EditText origin_pwd = (EditText) findViewById(R.id.origin_pwd);
		EditText setting_pw = (EditText) findViewById(R.id.setting_pw);
		EditText setting_pw2 = (EditText) findViewById(R.id.setting_pw2);

		String o_pw = origin_pwd.getText().toString(); // 예전 비밀번호
		String pw = setting_pw.getText().toString(); // 새로입력한 비밀번호
		String pw2 = setting_pw2.getText().toString(); // 새로입력한 비밀번호 확인

		// 암호체크
		PasswordService svc = new PasswordService(this);
		isValid = svc.checkPassword(o_pw);

		// 암호 성공/실패 분기처리
		if (isValid) {
			if (pw.equals(pw2)) {
				svc.savePassword(pw);

				Toast toast = Toast.makeText(this, "저장되었습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
				startActivity(new Intent(this, MainViewPagerActivity.class));
				finish(); // 뒤로가기 안먹게
			} else {
				Toast toast = Toast.makeText(this, "입력한 비밀번호가 다릅니다.",
						Toast.LENGTH_SHORT);
				toast.show();
			}

		} else {
			// 비밀번호 틀렸을때
			Toast toast = Toast.makeText(this, "기존의 비밀번호가 맞지 않습니다.",
					Toast.LENGTH_SHORT);
			toast.show();

			origin_pwd.setText(""); // 암호입력필드 초기화
		}

		// 메인화면으로 이동
		// startActivity(new Intent(context, ReadMyStoryActivity.class));
	}

}
