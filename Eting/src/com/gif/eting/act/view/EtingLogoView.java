package com.gif.eting.act.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;

import com.gif.eting.util.Util;
import com.gif.eting_dev.R;

/**
 * 움직이는 구름
 * 
 * @author lifenjoy51
 * 
 */
@SuppressLint("ViewConstructor")
public class EtingLogoView extends View {
	private Drawable mDrawable;

	public EtingLogoView(Context context) {
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

		
		Drawable dr = context.getResources().getDrawable(R.drawable.eting_logo);
		
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		dr = new ScaleDrawable(dr, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight()).getDrawable();
		int size = width * 42 / 100;	//mainRatio% 에 해당하는 크기
		dr.setBounds(0, 0, size, size);


		int objWidth = size;
		int objHeight = size;
		
		int ptX = width/2 - objWidth/2; // 수평 위치
		int ptY = height/100*42 - objHeight/2 ; // 수평 위치 끝
		
		FrameLayout.LayoutParams settingParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		settingParams.leftMargin = ptX; //Your X coordinate
		settingParams.topMargin = ptY; //Your Y coordinate
		settingParams.gravity = Gravity.LEFT | Gravity.TOP;
		this.setLayoutParams(settingParams);

		mDrawable = dr;

	}
}