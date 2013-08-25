package com.gif.eting.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.gif.eting.R;
import com.gif.eting.util.AnimateDrawable;

/**
 * 인트로화면
 * 
 * @author lifenjoy51
 *
 */
public class IntroActivity extends Activity {
	protected int cnt =0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);	//TODO 디자인만 입히면 된다.
		
		FrameLayout fr = (FrameLayout) findViewById(R.id.intro_layout);
		fr.addView(new UfoView(this));	//움직이는 UFO 등록
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				//TODO 비밀번호가 세팅되어있지 않다면 메인페이지로 바로 이동해야하는거 아닌가?
				Intent intent = new Intent(IntroActivity.this,
						LockScreenActivity.class);
				startActivity(intent);				
				finish();	// 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
			}
		}, 5000);	//5초후 이동
	}

	/**
	 * 위아래로 움직이는 UFO 이미지
	 * 
	 * @author lifenjoy51
	 *
	 */
	private static class UfoView extends View {
		private AnimateDrawable mDrawable;
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
			invalidate();
		}
		
		/**
		 * 왕복운동 애니메이션
		 * 
		 * @param context
		 * @param an
		 */
		private void setAnimationEvent(Context context, Animation an){

			System.out.println("onAnimationEnd");
			Drawable dr = context.getResources().getDrawable(
					R.drawable.intro_ufo);
			dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			int width = metrics.widthPixels;
			int height = metrics.heightPixels;

			int stPtX = width / 2 - dr.getIntrinsicWidth() / 2;
			int enPtX = width / 2 - dr.getIntrinsicWidth() / 2;
			int stPtY = height / 40 * 11;	//수직 위치 시작
			int enPtY = height / 40 * 15;	//수직 위치 끝
			
			//처음 실행할때
			if(cnt==0){
				stPtY = (stPtY+enPtY)/2; //처음엔 중앙에서 시작한다
			}
			
			//위아래로 왔다갔다하게하기
			if(chk){
				System.out.println("chk"+chk);
				an = new TranslateAnimation(enPtX, stPtX, enPtY, stPtY);
				chk= false;
			}else{
				System.out.println("chk"+chk);
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
			
			public AnimationControl(Context context){
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

}
