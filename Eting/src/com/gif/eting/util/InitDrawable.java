package com.gif.eting.util;

import com.gif.eting.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

public class InitDrawable extends AsyncTask<Object, String, Drawable> {

	Counter cnt;

	public InitDrawable(Counter cnt) {
		this.cnt = cnt;
	}

	@Override
	protected Drawable doInBackground(Object... params) {
		Context context = (Context) params[0];
		int res = (Integer) params[1];
		switch (res) {
		
		case R.drawable.main_planet:
			Util.planet = context.getResources().getDrawable(res);
			break;
		case R.drawable.eting_logo:
			Util.etingLogo = context.getResources().getDrawable(res);
			break;
		case R.drawable.main_cloud_1:
			Util.cloud1 = context.getResources().getDrawable(res);
			break;
		case R.drawable.main_cloud_2:
			Util.cloud2 = context.getResources().getDrawable(res);
			break;
		case R.drawable.main_cloud_3:
			Util.cloud3 = context.getResources().getDrawable(res);
			break;
		case R.drawable.main_cloud_4:
			Util.cloud4 = context.getResources().getDrawable(res);
			break;
		case R.drawable.spaceship:
			Util.spaceship = context.getResources().getDrawable(res);
			break;
		case R.drawable.main_acc_2:
			Util.main_acc_2 = context.getResources().getDrawable(res);
			break;
		case R.drawable.intro_ufo:
			Util.intro_ufo = context.getResources().getDrawable(res);
			break;
			
		case R.drawable.bg_4:
			Util.mainBg = context.getResources().getDrawable(res);
			break;
		case R.drawable.bg_5:
			Util.mainBg = context.getResources().getDrawable(res);
			break;
		case R.drawable.bg_1:
			Util.mainBg = context.getResources().getDrawable(res);
			break;

		default:
			break;
		}
		
		

		return null;
	}

	@Override
	protected void onPostExecute(Drawable result) {
		cnt.setCnt();
		System.out.println("cnt=" + cnt);
	}

}
