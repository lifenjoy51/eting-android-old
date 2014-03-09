package com.gif.eting.etc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.gif.eting.R;

public class CachedBitmap {

	public static Drawable mainBg;
	public static Drawable planet;
	public static Drawable etingLogo;
	public static Drawable main_acc_2;
	public static Drawable intro_ufo;
	
	public static Drawable getMainBg(Context context){
		if (mainBg == null) {
			Log.d("cached", "mainbg created");

			/**
			 * 시간에따라 배경을 바꾼다
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
			String thisHourStr = sdf.format(new Date());
			int thisHour = Integer.parseInt(thisHourStr);

			if (thisHour < 6) {
				mainBg = context.getResources().getDrawable(R.drawable.bg_blue);
			} else if (thisHour < 12) {
				mainBg = context.getResources().getDrawable(R.drawable.bg_purple);
			} else if (thisHour < 24) {
				mainBg = context.getResources().getDrawable(R.drawable.bg_green);
			} else {
				mainBg = context.getResources().getDrawable(R.drawable.bg_blue);
			}
		}
		Log.d("used", "mainBg used");
		return mainBg;
	}
	
	public static Drawable getPlanet(Context context) {
		if (planet == null) {
			Log.d("cached", "planet created");
			planet = context.getResources().getDrawable(R.drawable.main_planet);
		}
		Log.d("used", "planet used");
		return planet;
	}
	
	public static Drawable getEtingLogo(Context context) {
		if (etingLogo == null) {
			Log.d("cached", "etingLogo created");
			etingLogo = context.getResources().getDrawable(
					R.drawable.eting_logo);
		}
		Log.d("used", "etingLogo used");
		return etingLogo;
	}
	
	public static Drawable getMain_acc_2(Context context) {
		if (main_acc_2 == null) {
			Log.d("cached", "main_acc_2 created");
			main_acc_2 = context.getResources().getDrawable(
					R.drawable.main_acc_2);
		}
		Log.d("used", "main_acc_2 used");
		return main_acc_2;
	}
	
	public static Drawable getIntro_ufo(Context context) {
		if (intro_ufo == null) {
			Log.d("cached", "intro_ufo created");
			intro_ufo = context.getResources()
					.getDrawable(R.drawable.intro_ufo);
		}
		Log.d("used", "intro_ufo used");
		return intro_ufo;
	}
}
