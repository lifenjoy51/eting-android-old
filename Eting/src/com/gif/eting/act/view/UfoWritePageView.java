package com.gif.eting.act.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import com.gif.eting.util.AnimateDrawable;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting_dev.R;

/**
 * 위아래로 움직이는 UFO 이미지
 * 
 * @author lifenjoy51
 * 
 */
public class UfoWritePageView extends View {
	public AsyncTaskCompleteListener<String> callback;
	private AnimateDrawable mDrawable;
	private boolean chk = false;
	private int cnt = 0;
	private int chkStart = 0;
	
	public UfoWritePageView(Context context) {
		super(context);
	}

	public UfoWritePageView(Context context,
			AsyncTaskCompleteListener<String> callback) {
		super(context);
		this.callback = callback;
		setFocusable(true);
		setFocusableInTouchMode(true);

		setAnimationEvent(context, null);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
		invalidate();
	}

	/**
	 * 왕복운동 애니메이션
	 * 
	 * @param context
	 * @param an
	 */
	private void setAnimationEvent(Context context, Animation an) {

		System.out.println("ufoAnimation");
		Drawable dr = context.getResources().getDrawable(R.drawable.intro_ufo);
		dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		int stPtX = width / 2 - dr.getIntrinsicWidth() / 2;
		int enPtX = width / 2 - dr.getIntrinsicWidth() / 2;
		int stPtY = height * 43/ 100;
		int enPtY = height * 47/ 100;

		

		// 위아래로 왔다갔다하게하기
		if (chk) {
			an = new TranslateAnimation(enPtX, stPtX, enPtY, stPtY);
			chk = false;
		} else {
			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);
			chk = true;
		}

		an.setAnimationListener(new AnimationControl(context));

		// 처음 실행할때
		long duration = 1300; // 이동하는 시간
		an.setDuration(duration);
		an.setRepeatCount(-1);
		an.initialize(10, 10, 10, 10);

		mDrawable = new AnimateDrawable(dr, an);
		an.startNow();

	}

	/**
	 * 애니메이션 상태를 확인하는 클래스
	 * 
	 * @author lifenjoy51
	 * 
	 */
	private class AnimationControl implements AnimationListener {
		Context context;

		public AnimationControl(Context context) {
			this.context = context;
		}

		@Override
		public void onAnimationEnd(Animation an) {
		}

		/**
		 * 애니메이션이 반복될때마다 실행
		 */
		@Override
		public void onAnimationRepeat(Animation an) {
			cnt++;
			if (cnt == 4) {
				callback.onTaskComplete("");
			}
			setAnimationEvent(context, an);
		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}
	}
}
