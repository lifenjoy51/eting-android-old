package com.gif.eting.util;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.gif.eting.act.view.Snow;

public class SnowFactory {
	private static FrameLayout fr;
	public static int width;
	public static int height;
	private static Context context;
	private static Queue<Snow> snowQ;

	/**
	 * 변수모음
	 */
	// 최대 눈 개수
	private static int maxSnowCount = 100;
	// 애니메이션 딜레이 시간
	static int delayTime = 50;
	// 눈이 추가되는 간격
	static int interval = 800;
	// 눈 최소 크기
	static int snowMinSize = 3;
	// 눈 최대 크기
	static int snowMaxSize = 6;
	// 눈 최소 속도
	static int snowMinSpeed = 2;
	// 눈 최대 속도
	static int snowMaxSpeed = 5;

	public SnowFactory(Context context, FrameLayout fr) {
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;

		SnowFactory.context = context;

		snowQ = new ArrayBlockingQueue<Snow>(maxSnowCount);
		
		SnowFactory.fr = fr;
	}

	// 반복실행..
	public void setRepeat() {

		// 눈 추가
		hdlr = new SnowHandler();
		hdlr.sendMessageDelayed(new Message(), interval);
	}

	/**
	 * 눈생성
	 */
	static Handler hdlr;

	static class SnowHandler extends Handler {
		public void handleMessage(Message msg) {
			makeSnow();
			hdlr.sendMessageDelayed(new Message(), interval);
		}
	}

	// 반복되며 실행..
	public static void makeSnow() {

		int startPosition = (int) (Math.random() * width);
		int size = (int) (Math.random() * snowMaxSize) + snowMinSize;
		int speed = (int) (Math.random() * snowMaxSpeed) + snowMinSpeed;

		Snow snow = new Snow(context, startPosition, size, speed);

		fr.addView(snow);
		if (!snowQ.offer(snow)) {
			Snow removedSnow = snowQ.poll();
			fr.removeView(removedSnow);
		}
	}
}
