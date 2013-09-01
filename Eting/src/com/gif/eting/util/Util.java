package com.gif.eting.util;

import android.view.View;

/**
 * 상수값 모음
 * @author lifenjoy51
 *
 */
public class Util {
	public static String serverContext = "http://lifenjoys.cafe24.com/eting";	//서버
	


    /**
     * 절대위치 받아오기
     * 
     * @param v
     * @return
     */
	public static int getAbsolutePositionX(View v) {
		if (v == v.getRootView()) {
			return v.getLeft();
		} else {
			return v.getLeft() + getAbsolutePositionX((View) v.getParent());
		}

	}

	/**
	 * 절대위치 받아오기
	 * 
	 * @param v
	 * @return
	 */
	public static int getAbsolutePositionY(View v) {
		if (v == v.getRootView()) {
			return v.getTop();
		} else {
			return v.getTop() + getAbsolutePositionY((View) v.getParent());
		}
	}
	
}
