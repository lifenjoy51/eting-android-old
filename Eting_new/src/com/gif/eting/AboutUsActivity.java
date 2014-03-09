package com.gif.eting;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class AboutUsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);

		final View mogi = findViewById(R.id.mogi);
		final View kao = findViewById(R.id.kaonashi);

		Handler hdler = new Handler();

		hdler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Animation ani = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.blink);
				kao.startAnimation(ani);
				kao.setVisibility(View.VISIBLE);
				kao.bringToFront();
			}
		}, 1000);

		hdler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Animation ani = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.blink);
				mogi.startAnimation(ani);
				mogi.setVisibility(View.VISIBLE);
				mogi.bringToFront();
			}
		}, 10000);
	}

}
