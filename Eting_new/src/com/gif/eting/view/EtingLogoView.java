package com.gif.eting.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.gif.eting.etc.CachedBitmap;
import com.gif.eting.etc.Util;

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

		// 로고이미지
		mDrawable = CachedBitmap.getEtingLogo(context);

		// 로고이미지 크기를 조절한다.
		int size = Util.width * 42 / 100; // 42% 에 해당하는 크기
		mDrawable.setBounds(0, 0, size, size);

		// 위치설정
		Util.setPosition(this, size, size, 50, 45);

		// 터치 가능하게
		setFocusable(true);
		setFocusableInTouchMode(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
		invalidateDrawable(mDrawable);
	}
}