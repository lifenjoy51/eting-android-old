package com.nexters.eting;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.nexters.eting.svc.PasswordService;

/**
 * 모든 액티비티들이 상속받는 기반 액티비티
 * 
 * <p>
 * 백그라운드에서 포어그라운드로 올때 이벤트를 체크하는 로직 포함
 * http://www.vardhan-ds.co.in/2013/05/android
 * -solution-to-detect-when-android.html
 * </p>
 * 
 * @author lifenjoy51
 * 
 */
public class BaseActivity extends FragmentActivity {

	protected static final String TAG = BaseActivity.class.getName();

	public boolean isAppWentToBg = false;
	public static boolean isWindowFocused = false;
	public static boolean isBackPressed = false;

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);
		applicationWillEnterForeground();
		super.onStart();
	}

	private void applicationWillEnterForeground() {
		if (isAppWentToBg) {
			isAppWentToBg = false;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop ");
		applicationdidenterbackground();
	}

	public void applicationdidenterbackground() {
		if (!isWindowFocused) {
			isAppWentToBg = true;
		}
	}

	@Override
	public void onBackPressed() {
		if (!(this instanceof MainViewPagerActivity)) {
			isBackPressed = true;
		}
		super.onBackPressed();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		isWindowFocused = hasFocus;
		if (isBackPressed && !hasFocus) {
			isBackPressed = false;
			isWindowFocused = true;
		}
		super.onWindowFocusChanged(hasFocus);
	}

	/**
	 * 백그라운드에서 다시 실행될 때 잠금화면으로 이동
	 */
	@Override
	public void onRestart() {
		super.onRestart();
		if (this.isAppWentToBg) {
			PasswordService ps = new PasswordService(getApplicationContext());

			// 비밀번호가 설정되어 있으면
			if (ps.hasPassword()) {
				// 잠금화면으로 이동
				Intent intent = new Intent(this, LockScreenActivity.class);
				startActivity(intent);
			}
		}
	}
}
