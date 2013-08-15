package com.gif.eting.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.gif.eting.LockScreenActivity;
import com.gif.eting.R;

public class Intro extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(Intro.this,
						LockScreenActivity.class);
				startActivity(intent);
				// 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
				finish();
			}
		}, 2000);
	}

}
