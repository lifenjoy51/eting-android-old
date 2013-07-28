package com.gif.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LockScreenActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("Eting", "onCreate");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_screen);

		// 버튼 온클릭이벤트 등록
		Button btn = (Button) findViewById(R.id.lockScreenButton);
		btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		Log.i("LickScreenOnClick", String.valueOf(view.getId()));
		// 암호확인 버튼 클릭시
		if (view.getId() == R.id.lockScreenButton) {
			this.checkPassword();
		}

	}

	private void checkPassword() {
		boolean isValid = false;
		EditText et = (EditText) findViewById(R.id.lockScreenPassword); // 입력암호
		String pw = et.getText().toString();

		Log.i("password", pw);

		// 암호체크 임시로직
		if (pw.equals("1234")) {
			isValid = true;
		}

		// 암호 성공/실패 분기처리
		if (isValid) {
			Intent intent = new Intent(LockScreenActivity.this,
					MainActivity.class);
			startActivity(intent);
		} else {
			// 비밀번호 틀렸을때
			Toast toast = Toast.makeText(this, "비밀번호가 맞지 않습니다.",
					Toast.LENGTH_SHORT);
			toast.show();

			et.setText(""); // 암호입력필드 초기화
		}
	}

}
