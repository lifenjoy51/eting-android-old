package com.gif.eting.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.gif.eting.R;

/**
 * 상수값 모음
 * 
 * @author lifenjoy51
 * 
 */
public class Util {
	public static String serverContext = "http://eting.cafe24.com/eting"; // 서버
	// public static String serverContext = "http://192.168.123.123:8080/eting";
	// //로컬
	public static int mainRatio = 82;
	public static long fps = 10;
	public static Typeface nanum;

	// 캐쉬용 이미지들
	public static Drawable planet;
	public static Drawable etingLogo;
	public static Drawable cloud1;
	public static Drawable cloud2;
	public static Drawable cloud3;
	public static Drawable cloud4;
	public static Drawable mainBg;

	public static Drawable spaceship;
	public static Drawable adminSpaceship;
	public static Drawable main_acc_2;
	public static Drawable intro_ufo;

	public static void init(Context context, Counter cnt) {

		new InitFont(cnt).execute(context);

		/*
		 * Util.nanum = Typeface.createFromAsset(context.getAssets(),
		 * "fonts/NanumBarunGothic.ttf");
		 */

		new InitDrawable(cnt).execute(context, R.drawable.main_planet);
		new InitDrawable(cnt).execute(context, R.drawable.eting_logo);
		new InitDrawable(cnt).execute(context, R.drawable.main_cloud_1);
		new InitDrawable(cnt).execute(context, R.drawable.main_cloud_2);
		new InitDrawable(cnt).execute(context, R.drawable.main_cloud_3);
		new InitDrawable(cnt).execute(context, R.drawable.main_cloud_4);

		new InitDrawable(cnt).execute(context, R.drawable.spaceship);
		new InitDrawable(cnt).execute(context, R.drawable.admin_spaceship);
		new InitDrawable(cnt).execute(context, R.drawable.main_acc_2);
		new InitDrawable(cnt).execute(context, R.drawable.intro_ufo);

		// Drawable dr =
		// context.getResources().getDrawable(R.drawable.eting_logo);

		/**
		 * 시간에따라 배경을 바꾼다
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
		String thisHourStr = sdf.format(new Date());
		int thisHour = Integer.parseInt(thisHourStr);

		if (thisHour < 6) {
			new InitDrawable(cnt).execute(context, R.drawable.bg_4);
		} else if (thisHour < 12) {
			new InitDrawable(cnt).execute(context, R.drawable.bg_5);
		} else if (thisHour < 24) {
			new InitDrawable(cnt).execute(context, R.drawable.bg_1);
		} else {
			new InitDrawable(cnt).execute(context, R.drawable.bg_4);
		}

	}

	public static void init(Context context) {
		init(context, new Counter());
	}

	public static Typeface getNanum(Context context) {
		if (nanum == null) {
			Log.d("cached", "nanum created");
			nanum = Typeface.createFromAsset(context.getAssets(),
					"fonts/NanumBarunGothic.ttf");
		}
		Log.d("used", "nanum used");
		return nanum;
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

	public static Drawable getCloud1(Context context) {
		if (cloud1 == null) {
			Log.d("cached", "cloud1 created");
			cloud1 = context.getResources()
					.getDrawable(R.drawable.main_cloud_1);
		}
		Log.d("used", "cloud1 used");
		return cloud1;
	}

	public static Drawable getCloud2(Context context) {
		if (cloud2 == null) {
			Log.d("cached", "cloud2 created");
			cloud2 = context.getResources()
					.getDrawable(R.drawable.main_cloud_2);
		}
		Log.d("used", "cloud2 used");
		return cloud2;
	}

	public static Drawable getCloud3(Context context) {
		if (cloud3 == null) {
			Log.d("cached", "cloud3 created");
			cloud3 = context.getResources()
					.getDrawable(R.drawable.main_cloud_3);
		}
		Log.d("used", "cloud3 used");
		return cloud3;
	}

	public static Drawable getCloud4(Context context) {
		if (cloud4 == null) {
			Log.d("cached", "cloud4 created");
			cloud4 = context.getResources()
					.getDrawable(R.drawable.main_cloud_4);
		}
		Log.d("used", "cloud4 used");
		return cloud4;
	}

	public static Drawable getSpaceship(Context context) {
		if (spaceship == null) {
			Log.d("cached", "spaceship created");
			spaceship = context.getResources()
					.getDrawable(R.drawable.spaceship);
		}
		Log.d("used", "spaceship used");
		return spaceship;
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

	public static Drawable getMainBg(Context context) {
		if (mainBg == null) {
			Log.d("cached", "mainbg created");

			/**
			 * 시간에따라 배경을 바꾼다
			 */
			SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
			String thisHourStr = sdf.format(new Date());
			int thisHour = Integer.parseInt(thisHourStr);

			if (thisHour < 6) {
				mainBg = context.getResources().getDrawable(R.drawable.bg_4);
			} else if (thisHour < 12) {
				mainBg = context.getResources().getDrawable(R.drawable.bg_5);
			} else if (thisHour < 24) {
				mainBg = context.getResources().getDrawable(R.drawable.bg_1);
			} else {
				mainBg = context.getResources().getDrawable(R.drawable.bg_4);
			}
		}
		Log.d("used", "mainBg used");
		return mainBg;
	}
	
	public static Drawable getAdminSpaceship(Context context) {
		if (adminSpaceship== null) {
			Log.d("cached", "adminSpaceship created");
			adminSpaceship = context.getResources()
					.getDrawable(R.drawable.admin_spaceship);
		}
		Log.d("used", "adminSpaceship used");
		return adminSpaceship;
	}

}