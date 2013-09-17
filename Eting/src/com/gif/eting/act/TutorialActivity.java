package com.gif.eting.act;

import com.gif.eting.R;

import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ViewFlipper;

public class TutorialActivity extends Activity implements OnTouchListener {

	private ViewFlipper viewflipper;
	private WindowManager wm = null;

	// 터치 이벤트 발생 지점의 x좌표 저장
	private float xAtDown;
	private float xAtUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);

		wm = (WindowManager) getSystemService(Service.WINDOW_SERVICE);

		viewflipper = (ViewFlipper) findViewById(R.id.viewflipper);
		viewflipper.setOnTouchListener(this);

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v != viewflipper)
			return false;

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xAtDown = event.getX(); // 터치 시작지점 x좌표 저장

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			xAtUp = event.getX(); // 터치 끝난지점 x좌표 저장
			// 다음 view 보여줌
			viewflipper.showNext();
		}
//		v.invalidate();
		return true;
	}
//	public void finishTutorial() {
//		if (wm != null) {
//			
//			wm.removeView(viewflipper);
//			viewflipper.setDisplayedChild(0);
//			wm = null;
//		}
//	}
}