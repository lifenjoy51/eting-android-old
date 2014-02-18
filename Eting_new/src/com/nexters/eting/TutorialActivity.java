package com.nexters.eting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

public class TutorialActivity extends Activity implements OnTouchListener {

	private ViewFlipper viewflipper;
	private GestureDetector gestureDetector;
	private Boolean isFirst = false;

	// 애니메이션효과
	Animation animFlipInForeward;
	Animation animFlipOutForeward;
	Animation animFlipInBackward;
	Animation animFlipOutBackward;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);

		// 처음 실행 시 튜토리얼을 연것인지, 설정화면에서 연것인지 확인한다.
		Bundle bd = getIntent().getExtras();
		if (bd != null) {
			if (bd.containsKey("isFirst")) {
				isFirst = (Boolean) bd.get("isFirst");
			}
		}

		// 손가락 움직임을 탐지하는 설정을 적용한다.
		gestureDetector = new GestureDetector(getApplicationContext(),
				simpleOnGestureListener);
		viewflipper = (ViewFlipper) findViewById(R.id.viewflipper);
		viewflipper.setOnTouchListener(this);
		// viewflipper에 onTouch이벤트를 적용시키고, onTouch이벤트에선 gestureDetector에 등록된
		// simpleOnGestureListener를 실행시킨다.

		// 애니메이션 효과를 불러온다.
		animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
		animFlipOutForeward = AnimationUtils
				.loadAnimation(this, R.anim.flipout);
		animFlipInBackward = AnimationUtils.loadAnimation(this,
				R.anim.flipin_reverse);
		animFlipOutBackward = AnimationUtils.loadAnimation(this,
				R.anim.flipout_reverse);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}

	// 터치이벤트 컨트롤
	GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

		public boolean onDown(MotionEvent event) {
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			float sensitvity = 50;

			// 다음페이지로
			if ((e1.getX() - e2.getX()) > sensitvity) {
				// 마지막장에서 다음페이지로 넘기면 메인액티비티를 실행한다.
				if (viewflipper.getDisplayedChild() == viewflipper
						.getChildCount() - 1) {
					if (isFirst != null && isFirst) {
						startActivity(new Intent(getApplicationContext(),
								MainViewPagerActivity.class));
					}
					finish();

				} else {
					viewflipper.setInAnimation(animFlipInForeward);
					viewflipper.setOutAnimation(animFlipOutForeward);
					viewflipper.showNext();
				}

				// 이전페이지로
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				if (viewflipper.getDisplayedChild() > 0) {
					viewflipper.setInAnimation(animFlipInBackward);
					viewflipper.setOutAnimation(animFlipOutBackward);
					viewflipper.showPrevious();
				}
			}

			return true;
		}
	};
}