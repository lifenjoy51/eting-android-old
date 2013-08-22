package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.svc.PasswordService;

/**
 * 비밀번호 화면
 * 
 * @author lifenjoy51
 *
 */
public class LockScreenActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_screen);

		// 버튼 온클릭이벤트 등록
		findViewById(R.id.lockScreenButton).setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// 암호확인 버튼 클릭시
		switch (view.getId()) {
		case R.id.lockScreenButton:
			this.checkPassword();			
			break;

		default:
			break;
		}
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
