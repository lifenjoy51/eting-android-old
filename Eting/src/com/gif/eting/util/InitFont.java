package com.gif.eting.util;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;

public class InitFont extends AsyncTask<Object, String, Object> {

	Counter cnt;

	public InitFont(Counter cnt) {
		this.cnt = cnt;
	}

	@Override
	protected Object doInBackground(Object... params) {
		Context context = (Context) params[0];
		Util.nanum = Typeface.createFromAsset(context.getAssets(),
				"fonts/NanumBarunGothic.ttf");

		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		this.cnt.setCnt();
	}

}
