package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.gif.eting.svc.PasswordService;
import com.gif.eting_dev.R;

/**
 * 비밀번호 화면
 * 
 * @author lifenjoy51
 *
 */
public class LockScreenActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_screen);

		//암호입력필드
		EditText et = (EditText) findViewById(R.id.lockScreenPassword);

		//암호입력할때 4자리 다 채우면 자동으로 암호검사
		et.addTextChangedListener(new TextWatcher() {

			public void afterTextChanged(Editable s) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 4) {
					checkPassword();
				}
			}
		});
	}

	/**
	 * 비밀번호 확인하기
	 */
	private void checkPassword() {
		boolean isValid = false;
		EditText et = (EditText) findViewById(R.id.lockScreenPassword); // 입력암호
		String pw = et.getText().toString();

		// 암호체크
		PasswordService svc = new PasswordService(this);
		isValid = svc.checkPassword(pw);

		// 암호 성공/실패 분기처리
		if (isValid) {
			startActivity(new Intent(this, MainViewPagerActivity.class));
			finish();	//뒤로가기 안먹게
		} else {
			// 비밀번호 틀렸을때
			Toast toast = Toast.makeText(this, "비밀번호가 맞지 않습니다.",
					Toast.LENGTH_SHORT);
			toast.show();

			et.setText(""); // 암호입력필드 초기화
		}
	}
}
