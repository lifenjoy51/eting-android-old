package com.nexters.eting.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.nexters.eting.R;
import com.nexters.eting.etc.Const;
import com.nexters.eting.etc.Util;

/**
 * 위아래로 움직이는 UFO 이미지
 * 
 * @author lifenjoy51
 * 
 */
public class UfoView extends View {
	// 그릴 이미지
	private Drawable mDrawable;
	// 애니메이션 시작 지점
	private Point p1;
	// 애니메이션 끝나는 지점
	private Point p2;
	// 애니메이션 지속시간
	private float duration;
	// 현재위치
	private PointF p;
	// 이동속도
	private float speedX;
	private float speedY;
	// 진행방향
	private boolean direction = true;

	// 이미지 너비
	private int drWidth;
	// 이미지 높이
	private int drHeight;

	public UfoView(Context context) {
		super(context);

		// 이미지를 불러온다.
		mDrawable = context.getResources().getDrawable(R.drawable.intro_ufo);
		// 이미지 너비
		drWidth = mDrawable.getIntrinsicWidth();
		// 이미지 높이
		drHeight = mDrawable.getIntrinsicHeight();

		// 한쪽 끝
		this.p1 = new Point(Util.getX(50), Util.getY(41));
		// 다른쪽 끝
		this.p2 = new Point(Util.getX(50), Util.getY(45));
		// 현재위치
		this.p = new PointF(Util.getX(50), Util.getY(43));
		// 한번의 애니메이션을 표시하는데 걸리는 시간.
		this.duration = 1500 / Const.fps;

		// 이동하는 정도.
		this.speedX = Math.abs(p1.x - p2.x) / duration;
		this.speedY = Math.abs(p1.y - p2.y) / duration;

		// 애니메이션 시작
		Message msg = new Message();
		msg.obj = this;
		hdler.sendMessageDelayed(msg, delayTime);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		// 이미지 그릴 위치. 왼쪽, 위, 오른쪽, 아래
		int left = (int) (p.x - (drWidth / 2));
		int top = (int) (p.y - (drHeight / 2));
		int right = (int) (p.x + (drWidth) / 2);
		int bottom = (int) (p.y + (drHeight / 2));

		// 이미지가 그릴 위치를 설정한다.
		mDrawable.setBounds(left, top, right, bottom);
		// 이미지를 그린다.
		mDrawable.draw(canvas);

		// 방향에 따라 움직인다.
		if (direction) {
			p.x += speedX;
			p.y += speedY;
		} else {
			p.x -= speedX;
			p.y -= speedY;
		}

		// 위치를 판단해 방향을 설정한다.
		if (p.x > p2.x || p.y > p2.y) {
			direction = false;
		} else if (p.x < p1.x || p.y < p1.y) {
			direction = true;
		}
	}

	/**
	 * 애니메이션 관련 변수
	 */
	static Handler hdler = new MyHandler();
	static int delayTime = (int) Const.fps;

	static class MyHandler extends Handler {
		public void handleMessage(Message msg) {
			//뷰를 다시 그린다. (애니메이션 효과)
			View v = (View) msg.obj;
			v.invalidate();
			
			//다시 대기열에 등록해서 그리게 한다.
			Message newMsg = new Message();
			newMsg.obj = msg.obj;
			hdler.sendMessageDelayed(newMsg, delayTime);
		}
	}

}
