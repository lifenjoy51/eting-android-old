package com.gif.eting.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.gif.eting.R;
import com.gif.eting.etc.Util;

/**
 * 우측 하단 별
 *
 * @author Hyun
 *
 */
public class StarView extends View {
	private Drawable mDrawable;
	private final Context context;
	private long lastPushTime;
	private final static int interval = 10000;
	public boolean isReady = false;

	public StarView(Context context) {
		super(context);
		this.context = context;

		// 마지막 클릭시간 설정
		SharedPreferences sharedPref = ((Activity) context)
				.getPreferences(Context.MODE_PRIVATE);
		lastPushTime = sharedPref.getLong("clickTime", 0);

		// 이미지 초기화
		mDrawable = getImage(R.drawable.star_icon01);
		// 위치설정
		Util.setPosition(this, mDrawable.getIntrinsicWidth(),
				mDrawable.getIntrinsicHeight(), 85, 65);
		// 클릭설정
		setFocusable(true);
		setFocusableInTouchMode(true);

		// 주기적 실행
		Message msg = new Message();
		msg.obj = this;
		hdler.sendMessage(msg);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		mDrawable.draw(canvas);
		invalidateDrawable(mDrawable);
	}

	/**
	 * 시간을 확인한다.
	 */
	public void checkTime() {
		// 현재 시간
		long current = System.currentTimeMillis();
		// 마지막 이야기를 받은 시간으로부터 경과 시간
		long timeFromLastGettingEting = current - lastPushTime;
		Log.i("time", String.valueOf(timeFromLastGettingEting));

		// 맨처음 이미지는 star_icon01
		if (timeFromLastGettingEting > interval * 5) { // star_icon03
			mDrawable = getImage(R.drawable.star_icon03);
			this.invalidate();
			// 이팅 받을 준비 완료
			isReady = true;
		} else if (timeFromLastGettingEting > interval * 4) { // star_icon02
			mDrawable = getImage(R.drawable.star_icon02);
			this.invalidate();
		} else if (timeFromLastGettingEting > interval * 3) { // star_icon01_4
			mDrawable = getImage(R.drawable.star_icon01_4);
			this.invalidate();
		} else if (timeFromLastGettingEting > interval * 2) { // star_icon01_3
			mDrawable = getImage(R.drawable.star_icon01_3);
			this.invalidate();
		} else if (timeFromLastGettingEting > interval * 1) { // star_icon01_2
			mDrawable = getImage(R.drawable.star_icon01_2);
			this.invalidate();
		}
	}

	/**
	 * 별 초기화
	 */
	public void initView() {
		mDrawable = getImage(R.drawable.star_icon01);
		lastPushTime = System.currentTimeMillis();
		isReady = false;

		// 마지막 클릭시간 설정
		SharedPreferences sharedPref = ((Activity) context)
				.getPreferences(Context.MODE_PRIVATE);
		sharedPref.edit().putLong("clickTime", lastPushTime).commit();

		// 다시 그리기
		this.invalidate();
	}

	/**
	 * 이미지를 불러온다.
	 *
	 * @param id
	 * @return
	 */
	private Drawable getImage(int id) {
		Drawable dr = context.getResources().getDrawable(id);
		// 이미지 너비
		int drWidth = dr.getIntrinsicWidth();
		// 이미지 높이
		int drHeight = dr.getIntrinsicHeight();
		// 크기설정
		dr.setBounds(0, 0, drWidth, drHeight);

		return dr;
	}

	/**
	 * 주기적 실행을 위한 변수
	 */
	static Handler hdler = new MyHandler();

	static class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			// 뷰를 다시 그린다. (애니메이션 효과)
			StarView sv = (StarView) msg.obj;
			sv.checkTime();

			// 다시 대기열에 등록해서 그리게 한다.
			Message newMsg = new Message();
			newMsg.obj = msg.obj;
			hdler.sendMessageDelayed(newMsg, interval);
		}
	}

}