package com.gif.eting.act;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.gif.eting.R;

public class TutorialActivity extends Activity implements OnTouchListener {

	private ViewFlipper viewflipper;
	private WindowManager wm = null;
	private GestureDetector gestureDetector;
	private Boolean isFirst=false;
	
	Animation animFlipInForeward;
	Animation animFlipOutForeward;
	Animation animFlipInBackward;
	Animation animFlipOutBackward;

	// 터치 이벤트 발생 지점의 x좌표 저장
	private float xAtDown;
	private float xAtUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		
		Bundle bd = getIntent().getExtras();
		if(bd!=null){
			if(bd.containsKey("isFirst")){
				isFirst = (Boolean) bd.get("isFirst");
			}
		}
		
		
		gestureDetector = new GestureDetector(getApplicationContext(),
				simpleOnGestureListener);

		wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);

		viewflipper = (ViewFlipper) findViewById(R.id.viewflipper);
		animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
		animFlipOutForeward = AnimationUtils .loadAnimation(this, R.anim.flipout);
		animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
		animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);
		viewflipper.setOnTouchListener(this);

	}

	public void finishTutorial() {
		if (wm != null) {

			wm.removeView(viewflipper);
			viewflipper.setDisplayedChild(0);
			wm = null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return gestureDetector.onTouchEvent(event);

		/*
		 * // TODO Auto-generated method stub if (v != viewflipper) return
		 * false;
		 * 
		 * if (event.getAction() == MotionEvent.ACTION_DOWN) { xAtDown =
		 * event.getX(); // 터치 시작지점 x좌표 저장
		 * 
		 * } else if (event.getAction() == MotionEvent.ACTION_UP) { xAtUp =
		 * event.getX(); // 터치 끝난지점 x좌표 저장
		 * 
		 * System.out.println("11111"+viewflipper.getDisplayedChild());
		 * System.out.println("22222"+viewflipper.getChildCount());
		 * 
		 * //마지막이면 종료
		 * if(viewflipper.getDisplayedChild()==viewflipper.getChildCount()-1){
		 * finish(); }else{ // 다음 view 보여줌 viewflipper.showNext(); } }
		 */

	}

	GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {

		public boolean onDown(MotionEvent event) {
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			System.out.println("e1 "+e1.getX());
			System.out.println("e2 "+e2.getX());
			
			String swipe = "abc ";
			float sensitvity = 50;

			System.out.println("viewflipper.getDisplayedChild() "+viewflipper.getDisplayedChild());
			System.out.println("viewflipper.getChildCount() "+viewflipper.getChildCount());
			
			// TODO Auto-generated method stub
			if ((e1.getX() - e2.getX()) > sensitvity) {
				swipe += "Swipe Left\n";
				
				if (viewflipper.getDisplayedChild() == viewflipper.getChildCount() - 1) {
					if (isFirst != null) {
						if (isFirst) {
							startActivity(new Intent(getApplicationContext(),
									MainViewPagerActivity.class));
						}
					}
					finish();
				}else{
					viewflipper.setInAnimation(animFlipInForeward);
					viewflipper.setOutAnimation(animFlipOutForeward);
					
					viewflipper.showNext();
				}
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				swipe += "Swipe Right\n";

				if (viewflipper.getDisplayedChild()>0){
					viewflipper.setInAnimation(animFlipInBackward);
					viewflipper.setOutAnimation(animFlipOutBackward);
					viewflipper.showPrevious();
				}
			} else {
				swipe += "\n";
			}

			if ((e1.getY() - e2.getY()) > sensitvity) {
				swipe += "Swipe Up\n";
			} else if ((e2.getY() - e1.getY()) > sensitvity) {
				swipe += "Swipe Down\n";
			} else {
				swipe += "\n";
			}
			System.out.println(swipe);
			return true;
		}

	};

}