package com.gif.eting.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gif.eting.svc.PasswordService;
import com.gif.eting_dev.R;

/**
 * 비밀번호 화면
 * 
 * @author lifenjoy51
 * 
 */
public class LockScreenActivity extends Activity implements OnClickListener  {

	private ImageView lockScreenButton;
	private ImageView pwd_letter;
	private ImageView pwd_bg2;
	private Context context;
	private double stpwd3Y;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_screen);
		context = getApplicationContext();
		
		// 암호입력필드
		EditText et = (EditText) findViewById(R.id.lockScreenPassword);
		lockScreenButton = (ImageView) findViewById(R.id.lockScreenButton);
		pwd_letter = (ImageView) findViewById(R.id.pwd_letter);
		pwd_bg2 = (ImageView) findViewById(R.id.pwd_bg2);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int height = metrics.heightPixels;
		
		//Enter Pwd 글씨, Pwd 칸 이미지, Pwd click 이미지
//		Drawable pwd_letter = context.getResources().getDrawable(R.id.pwd_letter);
//		Drawable lockScreenPassword = context.getResources().getDrawable(R.id.lockScreenPassword);
//		Drawable lockScreenButton = context.getResources().getDrawable(R.id.lockScreenButton);
		// 수직 위치 시작
		double stpwd1Y = height * 22.5 / 100;
		double stpwd2Y = height * 33.5 / 100;
		stpwd3Y = height * 43.8 / 100;
		double stpwd4Y = height * 33.5 / 100;
		
		pwd_letter.setPadding(0, (int) stpwd1Y, 0, 0);
		pwd_bg2.setPadding(0, (int) stpwd2Y, 0, 0);
		lockScreenButton.setPadding(0, (int) stpwd3Y, 0, 0);
		et.setPadding(0, (int) (height * 34.5 / 100), 0, 0);
		
		pwd_letter.bringToFront();
		
		lockScreenButton.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					lockScreenButton = (ImageView) findViewById(v.getId());
					lockScreenButton.setImageResource(R.drawable.pwd_button_2);
				}
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (!v.isPressed()) {
						lockScreenButton
								.setImageResource(R.drawable.pwd_button_1);
						return true;
					}

				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					lockScreenButton.setImageResource(R.drawable.pwd_button_1);
				}
				return false;
			}
		});

		lockScreenButton.setOnClickListener(this);
	}

	public void onClick(View view) {
		// 암호확인 버튼 클릭시
		view.setPadding(0, (int) stpwd3Y, 0, 0);
		if (view.getId() == R.id.lockScreenButton) {
			this.checkPassword();
		}
	}

		// 암호입력할때 4자리 다 채우면 자동으로 암호검사
//		et.addTextChangedListener(new TextWatcher() {
//
//			public void afterTextChanged(Editable s) {
//			}
//
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//			}
//
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (s.length() == 4) {
//					checkPassword();
//				}
//			}
//		});
//	}

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
			finish(); // 뒤로가기 안먹게
		} else {
			// 비밀번호 틀렸을때
			Toast toast = Toast.makeText(this, "비밀번호가 맞지 않습니다.",
					Toast.LENGTH_SHORT);
			toast.show();

			et.setText(""); // 암호입력필드 초기화
		}
	}
}
