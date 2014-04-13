package com.gif.eting.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.gif.eting.etc.CachedBitmap;
import com.gif.eting.etc.Util;

/**
 * 움직이는 구름
 *
 * @author lifenjoy51
 *
 */
@SuppressLint("ViewConstructor")
public class PlanetView extends View {
	private final Drawable mDrawable;

	public PlanetView(Context context) {
		super(context);

		//메인 행성 이미지를 불러온다.
		mDrawable = CachedBitmap.getPlanet(context);
		//행성이미지 크기를 조절한다.
		int size = Util.width * 76 / 100;	//82% 에 해당하는 크기
		mDrawable.setBounds(0, 0, size, size);

		// 한번의 애니메이션을 표시하는데 걸리는 시간.
		long duration = 60000;

		//위치설정
		Util.setPosition(this, size, size, 50, 45);

		//애니메이션 설정
		Animation an = new RotateAnimation(0, 360, size/2, size/2);
		an.setDuration(duration);
		an.setRepeatCount(-1);
		an.setInterpolator(new LinearInterpolator());
		this.startAnimation(an);

		//터치 가능하게
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
	}

}