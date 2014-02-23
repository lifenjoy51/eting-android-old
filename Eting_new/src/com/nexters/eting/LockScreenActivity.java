package com.nexters.eting;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nexters.eting.svc.PasswordService;

/**
 * 비밀번호 화면
 * 
 * @author lifenjoy51
 * 
 */
public class LockScreenActivity extends Activity implements OnClickListener {

	private ImageView lockScreenButton;
	private ImageView pwd_letter;
	private ImageView pwd_bg2;
	private EditText et;
	private Context context;
	private double stpwd3Y;

	// GCM 관련 변수
	private boolean isGcm;
	private String storyId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockscreen);
		context = getApplicationContext();

		// 암호입력필드
		et = (EditText) findViewById(R.id.lockScreenPassword);
		lockScreenButton = (ImageView) findViewById(R.id.lockScreenButton);
		pwd_letter = (ImageView) findViewById(R.id.pwd_letter);
		pwd_bg2 = (ImageView) findViewById(R.id.pwd_bg2);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int height = metrics.heightPixels;

		// 수직 위치 시작
		double stpwd1Y = height * 22.5 / 100;
		double stpwd2Y = height * 33.5 / 100;
		stpwd3Y = height * 43.8 / 100;

		pwd_letter.setPadding(0, (int) stpwd1Y - 8, 0, 0);
		pwd_bg2.setPadding(0, (int) stpwd2Y - 8, 0, 0);
		lockScreenButton.setPadding(0, (int) stpwd3Y, 0, 0);
		et.setPadding(0, (int) (height * 34.5 / 100), 0, 0);

		pwd_letter.bringToFront();

		lockScreenButton.setOnClickListener(this);

		// GCM으로 받은경우 페이지이동
		Intent intent = getIntent();
		isGcm = intent.getBooleanExtra("GCM", false); // GCM여부
		if (isGcm) {
			storyId = intent.getStringExtra("storyId");
		}
	}

	/**
	 * 클릭이벤트
	 */
	public void onClick(View view) {
		// 암호확인 버튼 클릭시
		if (view.getId() == R.id.lockScreenButton) {
			this.checkPassword();
		}
	}

	/**
	 * 비밀번호 확인하기
	 */
	private void checkPassword() {
		String pw = et.getText().toString();

		// 암호체크
		PasswordService svc = new PasswordService(this);

		// 암호 성공/실패 분기처리
		if (svc.checkPassword(pw)) {
			// 키보드 숨기기
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

			// 메인화면으로 이동
			Intent intent = new Intent(this, MainViewPagerActivity.class);
			intent.putExtra("GCM", isGcm);
			intent.putExtra("storyId", storyId);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish(); // 뒤로가기 안먹게
		} else {
			// 비밀번호 틀렸을때
			Toast.makeText(this, R.string.wrong_password, Toast.LENGTH_SHORT)
					.show();
			// 암호입력필드 초기화
			et.setText("");
		}
	}
}
