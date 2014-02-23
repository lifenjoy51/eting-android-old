package com.nexters.eting;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.nexters.eting.etc.Util;
import com.nexters.eting.svc.PasswordService;
import com.nexters.eting.svc.gcm.GcmInitService;
import com.nexters.eting.task.GetRepliedStoryTask;
import com.nexters.eting.task.RegistrationTask;
import com.nexters.eting.view.UfoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashActivity extends Activity {

	private Context context;

	// UI 개체들
	private ImageView introCircle;
	private ImageView introLogo;
	private static UfoView ufoView;
	private FrameLayout fr;

	//GCM 관련 변수
	private boolean isGcm;
	private String storyId;

	//시작관련 변수
	private boolean isFirst = false;	//처음 실행 여부
	private boolean isDelayed = false;	//애니메이션을 보여주기 위해 일정시간 화면을 지연시켰는지 여부. 3초가 지났는지 안지났는지.
	//TODO 임시로 true로
	private final boolean isLoaded= true;	//캐쉬 로딩 여부
	private final Handler handler= new Handler();

	//설정 저장소
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		context = getApplicationContext();

		//저장소 초기화
		pref = context.getSharedPreferences("eting", Context.MODE_PRIVATE);

		// Util 초기화
		Util.init(context);

		// 레이아웃
		fr = (FrameLayout) findViewById(R.id.intro_layout);
		// 동그란 배경
		introCircle = (ImageView) findViewById(R.id.intro_circle_background);
		// 이팅로고
		introLogo = (ImageView) findViewById(R.id.intro_eting_logo);

		// 동그란 배경 위치지정 y 43%
		Util.setPosition(introCircle, R.drawable.intro_circle_background, 50,
				43);
		// 이팅 로고 위치지정 y 65%
		Util.setPosition(introLogo, R.drawable.intro_eting_logo, 50, 65);

		// 움직이는 ufo설정.
		ufoView = new UfoView(this);
		fr.addView(ufoView);

		// GCM 설정
		GcmInitService gcmInitService = new GcmInitService(context);

		// 기기 아이디가 없으면 서버에 기기 등록
		String deviceId = pref.getString("device_id", "");
		//System.out.println("deviceId \t "+deviceId);
		if(Util.isEmpty(deviceId)){
			//기기등록 고고씽
			new RegistrationTask().execute(context);
		}

		// 처음 실행 여부 체크
		isFirst = pref.getBoolean("isFirst", true);
		if (isFirst) {
			pref.edit().putBoolean("isFirst", false).commit();
		}else{
			//처음 실행이 아닌 경우에
			//GCM 등록 실패시
			String regId = gcmInitService.getRegId();
			if(Util.isEmpty(regId)){
				//TODO 서버에서 답글이 달린 이야기만 받아온다.
				new GetRepliedStoryTask().execute(context);
			}
		}

		//GCM으로 받은경우 페이지이동
        Intent intent = getIntent();
		isGcm = intent.getBooleanExtra("GCM", false);	//GCM여부
		if(isGcm){
			storyId = intent.getStringExtra("storyId");
		}

		// 3초후 페이지 이동
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				isDelayed = true;
				startNextActivity();
			}
		}, 3000);

		//TODO 테스트로 공지우주선 만들기
		pref.edit().putString("notify_id", "12").commit();
		pref.edit().putString("notify_ufo", "공지내용입니다 https://www.facebook.com/pages/eting-%EA%B0%90%EC%84%B1%ED%86%B5%EC%8B%A0-%EB%8B%A4%EC%9D%B4%EC%96%B4%EB%A6%AC-%EC%9D%B4%ED%8C%85/1411298635783398 이거는 넥스터즈 http://teamnexters.com/?page_id=188").commit();

	}

	/**
	 * 처음접속이면 튜토리얼
	 * 아니면 비밀번호화면으로
	 */
	@SuppressLint("InlinedApi")
	public void startNextActivity(){
		//자원이 다 로딩되지 않았거나, 3초가 지나지 않았으면 작업을 진행하지 않는다!
		if(!isLoaded || !isDelayed){
			return;
		}

		//처음 실행했다면 튜토리얼 페이지로
		if (isFirst) {

			Intent intent = new Intent(SplashActivity.this,
					TutorialActivity.class);
			intent.putExtra("isFirst", true);

			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
			finish();

		}else {
			moveToLockScreenActivity();
		}

	}
	/**
	 * 비밀번호 화면으로 이동
	 */
	private void moveToLockScreenActivity() {

		PasswordService ps = new PasswordService(context);
		Class<?> targetClass;

		//비밀번호 설정 여부에 따라 이동할 액티비티를 결정한다.
		if (ps.hasPassword()) {
			targetClass = LockScreenActivity.class;
		} else {
			targetClass = MainViewPagerActivity.class;
		}

		// 인텐트 설정
		Intent intent = new Intent(SplashActivity.this,
				targetClass);
		intent.putExtra("GCM", isGcm);
        intent.putExtra("storyId", storyId);

		// 시작하는 액티비티를 최 상단으로
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		startActivity(intent);
		overridePendingTransition(R.anim.fade, R.anim.hold);
		finish(); // 뒤로가기 했을경우 안나오도록 없애주기

	}
}
