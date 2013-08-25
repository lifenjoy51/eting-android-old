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
public class CloudView extends View {
	private AnimateDrawable mDrawable;
	private boolean chk = false;
	private int cnt = 0;

	// 몇번째 구름인가
	private int seq = 0;

	public CloudView(Context context, int seq) {
		super(context);
		this.seq = seq;
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
		Drawable dr = context.getResources().getDrawable(R.drawable.cloud);
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

		switch (this.seq) {
		case 1:
			stPtX = width / 10 * 4;
			enPtX = width / 10 * 6;
			stPtY = height / 10 * 1;
			enPtY = height / 10 * 2;
			break;

		case 2:
			stPtX = width / 10 * 0;
			enPtX = width / 10 * 8;
			stPtY = height / 10 * 4;
			enPtY = height / 10 * 4;
			break;

		default:
			stPtX = 0;
			enPtX = 0;
			stPtY = 0;
			enPtY = 0;
			break;
		}

		System.out.println(stPtX + "," + enPtX + "," + stPtY + "," + enPtY);

		// 처음 실행할때
		if (cnt == 0) {
			stPtY = (stPtY + enPtY) / 2; // 처음엔 중앙에서 시작한다
			cnt++;
		}

		// 위아래로 왔다갔다하게하기
		if (chk) {
			System.out.println("chk" + chk);
			an = new TranslateAnimation(enPtX, stPtX, enPtY, stPtY);
			chk = false;
		} else {
			System.out.println("chk" + chk);
			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);
			chk = true;
		}

		an.setAnimationListener(new AnimationControl(context));

		long duration; // 이동하는 시간
		switch (seq) {
		case 1:
			duration = 3500;
			break;

		case 2:
			duration = 8000;
			break;

		default:
			duration = 5000;
			break;
		}

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