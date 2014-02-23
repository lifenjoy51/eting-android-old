package com.nexters.eting.etc;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

//TODO GC당하면 우짜지??
public class Util {
	private static Context context;
	public static int width = 0;
	public static int height = 0;

	/**
	 * 초기화
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		Util.context = context;
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		width = metrics.widthPixels;
		height = metrics.heightPixels;
	}
	
	/**
	 * 서버에 등록된 기기 고유번호를 불러온다.
	 * @param context
	 * @return
	 */
	public static String DeviceId(Context context){
		SharedPreferences prefs = context.getSharedPreferences("eting",
				Context.MODE_PRIVATE);
		String deviceId = prefs.getString("device_id", "");
		Log.d("HTTP", "deviceId = "+deviceId);
		return deviceId;
	}
	
	/**
	 *  FrameLayout에서 View의 위치를 지정한다. (기본)
	 */
	public static void setPosition(View view, int x, int y) {
		setPosition(view, view.getWidth(), view.getHeight(), x, y);
	}

	/**
	 * FrameLayout에서 View의 위치를 지정한다. (이미지ID로)
	 * 
	 * @param view
	 *            위치를 조정할 View
	 * @param id
	 *            위치를 조정할 View에 사용한 이미지 ID
	 * @param x
	 *            x 위치(%)
	 * @param y
	 *            y 위치(%)
	 */
	public static void setPosition(View view, int id, int x, int y) {
		//TODO 널포인터 생기네...
		Drawable dr = context.getResources().getDrawable(id);
		int width = dr.getIntrinsicWidth();
		int height = dr.getIntrinsicHeight();
		setPosition(view, width, height, x, y);
	}

	/**
	 * FrameLayout에서 View의 위치를 지정한다. (이미지 너비,높이)
	 * 
	 * @param view
	 *            위치를 조정할 View
	 * @param width
	 *            view 너비
	 * @param height
	 *            view 높이
	 * @param x
	 *            x 위치(%)
	 * @param y
	 *            y 위치(%)
	 */
	public static void setPosition(View view, int width, int height, int x,
			int y) {

		int px = Util.width * x / 100 - (width/2);
		int py = Util.height * y / 100 - (height/2);
		
		if(width == 0) width = LayoutParams.WRAP_CONTENT;
		if(height == 0) height = LayoutParams.WRAP_CONTENT;
		
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width,
				height);
		lp.leftMargin = px;
		lp.topMargin = py;
		lp.gravity = Gravity.LEFT | Gravity.TOP;
		view.setLayoutParams(lp);
	}

	/**
	 * %값에 맞게 화면상에 위치를 반환한다.
	 * 
	 * @param percentX
	 *            표시할 위치의 %값
	 * @return
	 */
	public static int getX(int drWidth, int percentX) {
		return percentX * width / 100 - (drWidth / 2);
	}

	public static int getX(int percentX) {
		return percentX * width / 100;
	}

	/**
	 * %값에 맞게 화면상에 위치를 반환한다.
	 * 
	 * @param prcentY
	 *            표시할 위치의 %값
	 * @return
	 */
	public static int getY(int percentY) {
		return percentY * height / 100;
	}
	
	/**
	 * 지정한 구분자로 이어진 날짜를 받아온다.
	 * @param delimeter
	 * 구분자 ex) - . 등
	 * @return
	 */
	public static String getDate(String delimeter) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy" + delimeter
				+ "MM" + delimeter + "dd", Locale.KOREA);
		Date date = new Date();
		String today = formatter.format(date);
		return today;
	}
	
	/**
	 * 널 or 빈값 체크.
	 * 
	 * @param str
	 *            체크할 문자열
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 현재 시간을 가져온다. HH:mm:ss.
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		String time = new SimpleDateFormat("HH:mm:ss").format(Calendar
				.getInstance().getTime());
		return time;
	}

}
