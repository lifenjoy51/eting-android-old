package com.gif.eting.act.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

import com.gif.eting_dev.R;
import com.gif.eting.util.AnimateDrawable;

/**
 * 움직이는 구름
 * 
 * @author lifenjoy51
 * 
 */
@SuppressLint("ViewConstructor")
public class Cloud1View extends View {
	private AnimateDrawable mDrawable;
	private boolean chk = true;

	public Cloud1View(Context context) {
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
		Drawable dr = context.getResources().getDrawable(R.drawable.main_cloud_4);
		dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
		//dr.setAlpha(250);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;

		int objWidth = dr.getIntrinsicWidth();
		int objHeight = dr.getIntrinsicHeight();
		int stPtX; // 수평 위치 시작
		int enPtX; // 수평 위치 끝
		int stPtY; // 수직 위치 시작
		int enPtY; // 수직 위치 끝
		long duration = 40000; // 이동하는 시간
		
		int stX1 = -40;	//첫번째 애니메이션 시작위치
		int enX1 = 100;	//첫번째 애니메이션 끝 위치
		int stX2 = -100;	//두번째 애니메이션 시작위치
		int enX2 = stX1;	//두번째 애니메이션 끝 위치
		int lenX1 = enX1 - stX1;	//첫번째 애니메이션 이동길이
		int lenX2 = enX2 - stX2;	//두번째 애니메이션 이동길이
		int lenTot = lenX1 + lenX2;	//전체 이동길이
		int posY = 8;
		
		// 위아래로 왔다갔다하게하기
		if (chk) {
			System.out.println("chk" + chk);

			stPtX = width * stX1 / 100;
			enPtX = width * enX1 / 100;
			stPtY = height * posY / 100;
			enPtY = height * posY / 100;

			System.out.println(stPtX + "," + enPtX + "," + stPtY + "," + enPtY);
			
			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);
			chk = false;

			duration = duration*lenX1/lenTot;
			
		} else {
			System.out.println("chk" + chk);

			stPtX = width * stX2/ 100;
			enPtX = width * enX2 / 100;
			stPtY = height * posY / 100;
			enPtY = height * posY / 100;

			System.out.println(stPtX + "," + enPtX + "," + stPtY + "," + enPtY);
			
			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);
			chk = true;

			duration = duration*lenX2/lenTot;
		}

		an.setAnimationListener(new AnimationControl(context));
		an.setDuration(duration);
		an.setRepeatCount(-1);
		an.setInterpolator(new LinearInterpolator());
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