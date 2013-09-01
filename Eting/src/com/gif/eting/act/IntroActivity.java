package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import com.gif.eting.R;
import com.gif.eting.act.view.UfoView;
import com.gif.eting.svc.PasswordService;
import com.gif.eting.svc.task.CheckStampTask;
import com.gif.eting.svc.task.CheckStampedStoryTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

/**
 * 인트로화면
 * 
 * @author lifenjoy51
 *
 */
public class IntroActivity extends Activity {
	protected int cnt =0;
	private int total = 3;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);	
		
		FrameLayout fr = (FrameLayout) findViewById(R.id.intro_layout);
		fr.addView(new UfoView(this));	//움직이는 UFO 등록
		
		/**
		 * 서버와 스탬프목록 동기화
		 * 
		 * CheckStampTask 파라미터는 CheckStampTask 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = this. Context.
		 */
		new CheckStampTask(new AfterCheckStampTask()).execute(this);
		
		/**
		 * 스탬프찍힌 이야기 리스트 받아오기
		 * 
		 * CheckStampedStoryTask 파라미터는 CheckStampedStoryTask 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = this. Context.
		 */
		new CheckStampedStoryTask(new AfterCheckStampedStoryTask()).execute(this);
		
		
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				cnt++;
				if(cnt==total){
					moveToLockScreenActivity();
				}
			}
		}, 3000);	//3초후 이동
	}
	
	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 * 애니메이션이 3초이상 지속되고
	 * 스탬프찍힌 이야기를 검사하고
	 * 스탬프 동기화가 완료되면 이동한다!
	 */
	private class AfterCheckStampTask implements AsyncTaskCompleteListener<String>{
		
		@Override
		public void onTaskComplete(String result) {
			Log.i("AfterCheckStampTask", result);
			cnt++;
			if(cnt==total){
				moveToLockScreenActivity();
			}
		}
	}
	
	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 * 애니메이션이 3초이상 지속되고
	 * 스탬프 동기화를 마치고
	 * 스탬프찍힌 이야기를 검사하면 이동한다!
	 */
	private class AfterCheckStampedStoryTask implements AsyncTaskCompleteListener<String>{
		
		@Override
		public void onTaskComplete(String result) {
			Log.i("AfterCheckStampTask", result);
			cnt++;
			if(cnt==total){
				moveToLockScreenActivity();
			}
		}
	}

	/**
	 * 화면이동
	 */
	private void moveToLockScreenActivity() {
		PasswordService psvc = new PasswordService(this);
		if (psvc.isPassword()) {
			Intent intent = new Intent(IntroActivity.this,
					LockScreenActivity.class);
			startActivity(intent);
		} else {
			startActivity(new Intent(this, MainViewPagerActivity.class));
		}
		finish(); // 뒤로가기 했을경우 안나오도록 없애주기

	}

}
