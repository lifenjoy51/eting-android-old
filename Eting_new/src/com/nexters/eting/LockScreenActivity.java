package com.nexters.eting;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
	
	//GCM 관련 변수
	private boolean isGcm;
	private String storyId;
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//
//		try {
//			Class.forName("android.os.AsyncTask");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.lock_screen);
//		context = getApplicationContext();
//		
//		// 암호입력필드
//		EditText et = (EditText) findViewById(R.id.lockScreenPassword);
//		//et.setTypeface(Util.getNanum(getApplicationContext()));
//		et.clearComposingText();
//		
//		lockScreenButton = (ImageView) findViewById(R.id.lockScreenButton);
//		pwd_letter = (ImageView) findViewById(R.id.pwd_letter);
//		pwd_bg2 = (ImageView) findViewById(R.id.pwd_bg2);
//		
//		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
//		int height = metrics.heightPixels;
//		
//		//Enter Pwd 글씨, Pwd 칸 이미지, Pwd click 이미지
////		Drawable pwd_letter = context.getResources().getDrawable(R.id.pwd_letter);
////		Drawable lockScreenPassword = context.getResources().getDrawable(R.id.lockScreenPassword);
////		Drawable lockScreenButton = context.getResources().getDrawable(R.id.lockScreenButton);
//		// 수직 위치 시작
//		double stpwd1Y = height * 22.5 / 100;
//		double stpwd2Y = height * 33.5 / 100;
//		stpwd3Y = height * 43.8 / 100;
//		double stpwd4Y = height * 33.5 / 100;
//		
//		pwd_letter.setPadding(0, (int) stpwd1Y - 8, 0, 0);
//		pwd_bg2.setPadding(0, (int) stpwd2Y - 8, 0, 0);
//		lockScreenButton.setPadding(0, (int) stpwd3Y, 0, 0);
//		et.setPadding(0, (int) (height * 34.5 / 100), 0, 0);
//		
//		pwd_letter.bringToFront();
//		
//		lockScreenButton.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					lockScreenButton = (ImageView) findViewById(v.getId());
//					lockScreenButton.setImageResource(R.drawable.pwd_button_2);
//				}
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					if (!v.isPressed()) {
//						lockScreenButton
//								.setImageResource(R.drawable.pwd_button_1);
//						return true;
//					}
//
//				}
//
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					lockScreenButton.setImageResource(R.drawable.pwd_button_1);
//				}
//				return false;
//			}
//		});
//
//		lockScreenButton.setOnClickListener(this);
//		
//
//		/**
//		 * GCM으로 받은경우 페이지이동
//		 */
//        Intent intent = getIntent();
//		isGcm = intent.getBooleanExtra("GCM", false);	//GCM여부
//		if(isGcm){
//			storyId = intent.getStringExtra("storyId");			
//		}
//	}
//
//	public void onClick(View view) {
//		// 암호확인 버튼 클릭시
//		view.setPadding(0, (int) stpwd3Y, 0, 0);
//		if (view.getId() == R.id.lockScreenButton) {
//			this.checkPassword();
//		}
//	}
//
//		// 암호입력할때 4자리 다 채우면 자동으로 암호검사
////		et.addTextChangedListener(new TextWatcher() {
////
////			public void afterTextChanged(Editable s) {
////			}
////
////			public void beforeTextChanged(CharSequence s, int start, int count,
////					int after) {
////			}
////
////			public void onTextChanged(CharSequence s, int start, int before,
////					int count) {
////				if (s.length() == 4) {
////					checkPassword();
////				}
////			}
////		});
////	}
//
//	/**
//	 * 비밀번호 확인하기
//	 */
//	private void checkPassword() {
//		boolean isValid = false;
//		EditText et = (EditText) findViewById(R.id.lockScreenPassword); // 입력암호
//		String pw = et.getText().toString();
//
//		// 암호체크
//		PasswordService svc = new PasswordService(this);
//		isValid = svc.checkPassword(pw);
//
//		// 암호 성공/실패 분기처리
//		if (isValid) {
//			//키보드 숨기기
//			InputMethodManager imm = (InputMethodManager) context
//					.getSystemService(Service.INPUT_METHOD_SERVICE);
//			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
//			
//			//메인화면으로 이동
//			Intent intent =new Intent(this, MainViewPagerActivity.class);
//	        intent.putExtra("GCM", isGcm);
//	        intent.putExtra("storyId", storyId);
//			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//			startActivity(intent);
//			overridePendingTransition(R.anim.fade, R.anim.hold);
//			finish(); // 뒤로가기 안먹게
//		} else {
//			// 비밀번호 틀렸을때
//			Toast toast = Toast.makeText(this, R.string.wrong_password,
//					Toast.LENGTH_SHORT);
//			toast.show();
//
//			et.setText(""); // 암호입력필드 초기화
//		}
//	}
}
