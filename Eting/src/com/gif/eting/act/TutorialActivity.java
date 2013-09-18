package com.gif.eting.act;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ViewFlipper;

import com.gif.eting.R;

public class TutorialActivity extends Activity implements OnTouchListener {

	private ViewFlipper viewflipper;
	private WindowManager wm = null;
	private GestureDetector gestureDetector;

	// 터치 이벤트 발생 지점의 x좌표 저장
	private float xAtDown;
	private float xAtUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		gestureDetector = new GestureDetector(getApplicationContext(),
				simpleOnGestureListener);

		wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);

		viewflipper = (ViewFlipper) findViewById(R.id.viewflipper);
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
		System.out.println("onTouch");
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
			String swipe = "abc ";
			float sensitvity = 50;

			// TODO Auto-generated method stub
			if ((e1.getX() - e2.getX()) > sensitvity) {
				swipe += "Swipe Left\n";
				viewflipper.showPrevious();
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				swipe += "Swipe Right\n";
				viewflipper.showNext();
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