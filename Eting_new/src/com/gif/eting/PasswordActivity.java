package com.gif.eting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gif.eting.etc.Util;
import com.gif.eting.svc.PasswordService;

public class PasswordActivity extends BaseActivity implements OnClickListener {
	private EditText existing_pw_et;
	private EditText new_pw_et;
	private EditText new_pw_confirm_et;
	private ImageView password_check;
	private boolean isValid = false;
	private PasswordService pwSvc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 레이아웃 설정
		setContentView(R.layout.activity_password);

		password_check = (ImageView) findViewById(R.id.password_check);
		existing_pw_et = (EditText) findViewById(R.id.existing_pw_et);
		new_pw_et = (EditText) findViewById(R.id.new_pw_et);
		new_pw_confirm_et = (EditText) findViewById(R.id.new_pw_confirm_et);

		// 암호체크
		pwSvc = new PasswordService(this);
		isValid = pwSvc.hasPassword();
		// 비밀번호가 설정되어 있으면
		if (isValid) {
			// 원래 비밀번호 입력란 보이고
			existing_pw_et.setVisibility(View.VISIBLE);
		} else {
			// 원래 비밀번호 입력란 없애고
			existing_pw_et.setVisibility(View.GONE);
			// 패스워드 on/off버튼
			password_check.setVisibility(View.GONE);
		}

		// 클릭 이벤트 등록
		findViewById(R.id.setting_save_pw_btn).setOnClickListener(this);
		findViewById(R.id.setting_save_btn).setOnClickListener(this);
		password_check.setOnClickListener(this);

	}

	/**
	 * 클릭 이벤트
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.setting_save_pw_btn) {
			// 유효성 검사 후
			if (validatePassword()) {
				savePassword();
			}
		} else if (view.getId() == R.id.password_check) {
			// 기존암호가 설정되어 있다면 실행
			if (isValid == true) {
				setPasswordOff();
			}
		}
	}

	/**
	 * 비밀번호 입력 유효성 검사.
	 *
	 * @return
	 */
	private boolean validatePassword() {

		String existingPw = existing_pw_et.getText().toString();
		String inputPw = new_pw_et.getText().toString();
		String inputPwConfirm = new_pw_confirm_et.getText().toString();

		// 기존 비밀번호 설정이 되어있다면
		if (isValid == true) {
			// 기존비밀번호, 새로입력한 비밀번호, 비밀번호 확인 모두 공란이 아니여야 한다.
			if (Util.isEmpty(existingPw) || Util.isEmpty(inputPw)
					|| Util.isEmpty(inputPwConfirm)) {
				Toast.makeText(this, R.string.enter_password_plz,
						Toast.LENGTH_SHORT).show();
				return false;
			}

		} else {
			// 기존 비밀번호 없이 새로 비밀번호를 입력하는것이면, 새로운 비밀번호와 비밀번호 확인만 공란이 아니면 된다.
			if (Util.isEmpty(inputPw) || Util.isEmpty(inputPwConfirm)) {
				Toast.makeText(this, R.string.enter_password_plz,
						Toast.LENGTH_SHORT).show();
				return false;
			}
		}

		return true;

	}

	/**
	 * 비밀번호를 해제한다.
	 */
	public void setPasswordOff() {

		AlertDialog.Builder alertDlg = new AlertDialog.Builder(
				PasswordActivity.this);
		alertDlg.setTitle("비밀번호");
		alertDlg.setMessage("비밀번호를 OFF 하시겠습니까?");
		alertDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
				// 비밀번호 초기화
				pwSvc.clearPassword();

				String msg = "Password Off 설정 되었습니다";
				Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT)
						.show();

				finish(); // 뒤로가기 안먹게
			}
		}).setNegativeButton("취소", null).show();

	}

	/**
	 * 비밀번호를 저장한다.
	 */
	public void savePassword() {
		String existingPw = existing_pw_et.getText().toString();
		String inputPw = new_pw_et.getText().toString();
		String inputPwConfirm = new_pw_confirm_et.getText().toString();

		// 기존암호를 비교해서 유효하면. 기존암호가 없다면 true.
		if (pwSvc.checkPassword(existingPw)) {
			// 입력한 암호와 확인암호가 같으면
			if (inputPw.equals(inputPwConfirm)) {
				pwSvc.savePassword(inputPw);

				Toast.makeText(this, R.string.save_completed,
						Toast.LENGTH_SHORT).show();

				finish(); // 뒤로가기 안먹게
			} else {
				Toast.makeText(this, R.string.wrong_input_password,
						Toast.LENGTH_SHORT).show();
			}

		} else { // 비밀번호 틀렸을때
			Toast.makeText(this, R.string.wrong_origin_password,
					Toast.LENGTH_SHORT).show();

			// 암호입력필드 초기화
			existing_pw_et.setText("");
		}

	}
}
