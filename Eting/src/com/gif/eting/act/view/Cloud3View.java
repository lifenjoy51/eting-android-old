package com.gif.eting.act.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import com.gif.eting.R;
import com.gif.eting.util.AnimateDrawable;

/**
 * 움직이는 구름
 * 
 * @author lifenjoy51
 * 
 */
@SuppressLint("ViewConstructor")
public class Cloud3View extends View {
	private AnimateDrawable mDrawable;
	private boolean chk = false;

	public Cloud3View(Context context) {
		super(context);
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
	 * 구름애니메이션
	 * 
	 * @param context
	 * @param an
	 */
	private void setAnimationEvent(Context context, Animation an) {

		System.out.println("onAnimationEnd");
		Drawable dr = context.getResources().getDrawable(R.drawable.main_cloud_3);
		dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		int objWidth = dr.getIntrinsicWidth();
		int objHeight = dr.getIntrinsicHeight();
		int stPtX; // 수평 위치 시작
		int enPtX; // 수평 위치 끝
		int stPtY; // 수직 위치 시작
		int enPtY; // 수직 위치 끝
		long duration = 16000; // 이동하는 시간
		
		// 위아래로 왔다갔다하게하기
		if (chk) {
			System.out.println("chk" + chk);

			stPtX = width / 10 * 1;
			enPtX = width / 10 * 10;
			stPtY = height / 10 * 1;
			enPtY = height / 10 * 1;

			System.out.println(stPtX + "," + enPtX + "," + stPtY + "," + enPtY);

			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);
			chk = false;
			duration = duration/100*90;
		} else {
			System.out.println("chk" + chk);

			stPtX = width / 10 * 0;
			enPtX = width / 10 * 1;
			stPtY = height / 10 * 1;
			enPtY = height / 10 * 1;

			System.out.println(stPtX + "," + enPtX + "," + stPtY + "," + enPtY);
			
			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);
			chk = true;
			duration = duration/100*10;
		}

		an.setAnimationListener(new AnimationControl(context));
		an.setDuration(duration);
		an.setRepeatCount(-1);
		an.initialize(objWidth, objHeight, width, height);

		mDrawable = new AnimateDrawable(dr, an);
		an.startNow();

	}

	/**
	 * 구름애니메이션 상태를 확인하는 클래스
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
			setAnimationEvent(context, an);
		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}
	}
}