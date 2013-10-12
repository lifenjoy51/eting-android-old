package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gif.eting.svc.PasswordService;
import com.gif.eting.util.Util;
import com.gif.eting.R;

public class PasswordResetActivity extends Activity implements
		OnClickListener {
	private EditText origin_pwd;
	private EditText setting_pw;
	private EditText setting_pw2;
	private TextView password_textView;
	private ImageView setting_save_pw_btn;
	private boolean isValid = false;
	private String o_pw;
	private PasswordService svc;
	private Typeface nanum = Util.nanum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_reset);
		// 버튼 온클릭이벤트 등록
		setting_save_pw_btn = (ImageView) findViewById(R.id.setting_save_pw_btn);
		origin_pwd = (EditText) findViewById(R.id.origin_pwd);
		setting_pw = (EditText) findViewById(R.id.setting_pw);
		setting_pw2 = (EditText) findViewById(R.id.setting_pw2);
		password_textView = (TextView) findViewById(R.id.password_textView);

		origin_pwd.setTypeface(nanum);
		setting_pw.setTypeface(nanum);
		setting_pw2.setTypeface(nanum);
		password_textView.setTypeface(nanum);
		password_textView.setPaintFlags(password_textView.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);

		setting_pw.setHintTextColor(Color.parseColor("#bbbbbb"));
		setting_pw2.setHintTextColor(Color.parseColor("#bbbbbb"));
		o_pw = origin_pwd.getText().toString(); // 예전 비밀번호

		// 암호체크
		svc = new PasswordService(this);
		isValid = svc.checkPassword(o_pw);

		if (isValid == true) {
//			origin_pwd.setClickable(false);
//			origin_pwd.setEnabled(false);
//			origin_pwd.setFocusable(false);
//			origin_pwd.setFocusableInTouchMode(false);
//			origin_pwd.setHintTextColor(Color.parseColor("#999999"));
			origin_pwd.setVisibility(View.GONE);
//			origin_pwd.setHint("Don't have password!!");
		}
		else {
			origin_pwd.setHintTextColor(Color.parseColor("#999999"));
			origin_pwd.setHint("origin passwrod");
			origin_pwd.setVisibility(View.VISIBLE);
		}

		setting_save_pw_btn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					setting_save_pw_btn = (ImageView) findViewById(v.getId());
					setting_save_pw_btn.setImageResource(R.drawable.submit_2);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (!v.isPressed()) {
						setting_save_pw_btn
								.setImageResource(R.drawable.submit_1);
						return true;
					}

				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					setting_save_pw_btn.setImageResource(R.drawable.submit_1);
				}

				return false;
			}
		});

		setting_save_pw_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		String s1 = origin_pwd.getText().toString();
		String s2 = setting_pw.getText().toString();
		String s3 = setting_pw2.getText().toString();

		if (isValid == true) {
			if ((s2 == null || "".equals(s2)) || (s3 == null || "".equals(s3))) {
				Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			} else {
				savePassword();
			}
		} else {
			if ((s1 == null || "".equals(s1)) || (s2 == null || "".equals(s2))
					|| (s3 == null || "".equals(s3))) {
				Toast.makeText(this, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
				return;
			} else {
				savePassword();
			}

		}

	}

	// 세팅화면에서 비밀번호 저장
	public void savePassword() {
		boolean isValid = false;

		String o_pw = origin_pwd.getText().toString(); // 예전 비밀번호
		String pw = setting_pw.getText().toString(); // 새로입력한 비밀번호
		String pw2 = setting_pw2.getText().toString(); // 새로입력한 비밀번호 확인

		// 암호체크
		PasswordService svc = new PasswordService(this);
		isValid = svc.checkPassword(o_pw);
		//
		// String s1 = origin_pwd.getText().toString();
		// if (isValid == false) {
		// origin_pwd.setClickable(false);
		// origin_pwd.setEnabled(false);
		// origin_pwd.setFocusable(false);
		// origin_pwd.setFocusableInTouchMode(false);
		// }
		// 암호 성공/실패 분기처리
		if (isValid) {
			if (pw.equals(pw2)) {
				svc.savePassword(pw);

				Toast toast = Toast.makeText(this, "저장되었습니다.",
						Toast.LENGTH_SHORT);
				toast.show();
				Intent intent = new Intent(PasswordResetActivity.this, MainViewPagerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent); 
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

	public void clear(View view) {
		origin_pwd.setText("");
		setting_pw.setText("");
		setting_pw2.setText("");
	}
}
