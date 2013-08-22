package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gif.eting.R;

/**
 * 인트로화면
 * 
 * @author lifenjoy51
 *
 */
public class IntroActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);	//TODO 디자인만 입히면 된다.

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				//TODO 비밀번호가 세팅되어있지 않다면 메인페이지로 바로 이동해야하는거 아닌가?
				Intent intent = new Intent(IntroActivity.this,
						LockScreenActivity.class);
				startActivity(intent);				
				finish();	// 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
			}
		}, 2000);	//2초후 이동
	}

}
