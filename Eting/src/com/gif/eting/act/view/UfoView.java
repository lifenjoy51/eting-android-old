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
import com.gif.eting.util.Util;
import com.gif.eting.R;



/**
 * 위아래로 움직이는 UFO 이미지
 * 
 * @author lifenjoy51
 *
 */
public class UfoView extends View {
	private AnimateDrawable mDrawable;
	private long drawClock = 0;
	private boolean chk= false;
	private int cnt = 0;

	public UfoView(Context context) {
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
		
		setAnimationEvent(context, null);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
	}
	
	/**
	 * 왕복운동 애니메이션
	 * 
	 * @param context
	 * @param an
	 */
	private void setAnimationEvent(Context context, Animation an){

		Drawable dr = Util.getIntro_ufo(context);
		dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		//사이즈
//		int size - width * Util.

		int stPtX = width / 2 - dr.getIntrinsicWidth() / 2;
		int enPtX = width / 2 - dr.getIntrinsicWidth() / 2;
//		int stPtY = height / 100 * 28;	//수직 위치 시작
//		int enPtY = height / 100 * 34;	//수직 위치 끝

		int center = height * 43 / 100 - dr.getIntrinsicHeight() / 2;
		int stPtY = height * 41 / 100 - dr.getIntrinsicHeight() / 2;
		int enPtY = height * 45 / 100 - dr.getIntrinsicHeight() / 2;
		
		//처음 실행할때
		if(cnt==0){
			stPtY = center; //처음엔 중앙에서 시작한다
		}
		
		//위아래로 왔다갔다하게하기
		if(chk){
			an = new TranslateAnimation(enPtX, stPtX, enPtY, stPtY);
			chk= false;
		}else{
			an = new TranslateAnimation(stPtX, enPtX, stPtY, enPtY);					
			chk= true;
		}
		
		an.setAnimationListener(new AnimationControl(context));
		
		//처음 실행할때
		long duration = 1500;	//이동하는 시간
		if(cnt==0){
			an.setDuration(duration/2);
			cnt++;
		}else{
			an.setDuration(duration);			
		}
		an.setRepeatCount(0);
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
		
		public AnimationControl(Context context){
			this.context = context;
		}

		@Override
		public void onAnimationEnd(Animation an) {
			setAnimationEvent(context, an);
		}

		/**
		 * 애니메이션이 반복될때마다 실행
		 */
		@Override
		public void onAnimationRepeat(Animation an) {
		}

		@Override
		public void onAnimationStart(Animation arg0) {

		}
	}
}
