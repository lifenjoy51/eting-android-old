package com.nexters.eting.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;

import com.nexters.eting.etc.Util;

/**
 * 움직이는 구름
 * 
 * @author lifenjoy51
 * 
 */
@SuppressLint("ViewConstructor")
public class CloudView extends View implements AnimationListener{
	// 그릴 이미지
	private Drawable mDrawable;
	// 애니메이션 시작 지점
	private Point p1;
	// 애니메이션 끝나는 지점
	private Point p2;
	//지속시간
	private int duration;
	
	// 애니메이션
	private Animation an;

	public CloudView(Context context, int resourceId, int stX,  int posY, int alpha, int duration) {
		super(context);

		// 이미지를 불러온다.
		mDrawable = context.getResources().getDrawable(resourceId);
		// 이미지 너비
		int drWidth = mDrawable.getIntrinsicWidth();
		// 이미지 높이
		int drHeight = mDrawable.getIntrinsicHeight();
		//투명도
		mDrawable.setAlpha(alpha);
		//크기설정
		mDrawable.setBounds(0, 0, drWidth, drHeight);
		//애니메이션 재시작할때 시작할 위치를 지정하기 위해
		int pWitdh = drWidth * 100 / Util.width;
		
		// 한쪽 끝
		this.p1 = new Point(Util.getX(-pWitdh), Util.getY(posY));
		// 다른쪽 끝
		this.p2 = new Point(Util.getX(100), Util.getY(posY));
		
		//지속시간
		this.duration = duration;
		
		//시작위치에 맞게 지속시간 조정
		float x1 = p2.x - stX;
		float x2 = p2.x - p1.x;
		float stDuration = duration * x1 / x2;
		
		//애니메이션 설정
		an = new TranslateAnimation(Util.getX(stX), p2.x, p1.y, p2.y);
		an.setDuration((long) stDuration);
		an.setRepeatCount(Animation.ABSOLUTE);
		an.setInterpolator(new LinearInterpolator());
		an.setAnimationListener(this);
		this.startAnimation(an);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// 이미지를 그린다.
		mDrawable.draw(canvas);
	}

	@Override
	public void onAnimationEnd(Animation ani) {
		//애니메이션 설정
		an = new TranslateAnimation(p1.x, p2.x, p1.y, p2.y);
		an.setDuration(duration);
		an.setRepeatCount(Animation.INFINITE);
		an.setInterpolator(new LinearInterpolator());
		this.startAnimation(an);
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
	}

	@Override
	public void onAnimationStart(Animation arg0) {		
	}

	
}