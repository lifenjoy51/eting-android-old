package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import com.gif.eting.R;
import com.gif.eting.act.view.UfoView;
import com.gif.eting.svc.task.CheckStampTask;
import com.gif.eting.util.AsyncTaskCompleteListener;

/**
 * 인트로화면
 * 
 * @author lifenjoy51
 *
 */
public class IntroActivity extends Activity {
	protected int cnt =0;
	private Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);	//TODO 디자인만 입히면 된다.
		
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
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if(cnt==0){
					moveToLockScreenActivity();
					cnt++;
				}
			}
		}, 3000);	//3초후 이동
	}
	
	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 * 애니메이션이 3초동안 지속되다가 이동하거나
	 * 스탬프 동기화가 완료되면 이동한다!
	 */
	private class AfterCheckStampTask implements AsyncTaskCompleteListener<String>{
		
		@Override
		public void onTaskComplete(String result) {
			Log.i("AfterCheckStampTask", result);
			if(cnt==0){
				moveToLockScreenActivity();
				cnt++;
			}
		}
	}
	
	/**
	 * 화면이동
	 */
	private void moveToLockScreenActivity(){
		//TODO 비밀번호가 세팅되어있지 않다면 메인페이지로 바로 이동해야하는거 아닌가?
		Intent intent = new Intent(IntroActivity.this,
				LockScreenActivity.class);
		startActivity(intent);				
		finish();	// 뒤로가기 했을경우 안나오도록 없애주기
		
	}

}
