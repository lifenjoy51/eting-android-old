package com.gif.eting.act.task;

import android.graphics.Canvas;
import android.os.AsyncTask;

import com.gif.eting.act.view.PlanetView;
import com.gif.eting.util.AnimateDrawable;
import com.gif.eting.util.AsyncTaskCompleteListener;

/**
 * 조회할 이야기에 해당하는 스탬프를 서버에서 받아오는 작업 AsyncTask를 상속받았기에 Main쓰레드와 별도로 실행된다.
 * 
 * @author lifenjoy51
 * 
 */
public class UfoAnimationTask extends AsyncTask<Object, String, String> {
	/**
	 * 작업 수행 후 이 클래스를 호출한 객체에서 후속작업을 실행시키기 위한 콜백.
	 */
	private AsyncTaskCompleteListener<String> callback;

	/**
	 * 생성자로 콜백을 받아온다.
	 * 
	 * @param callback
	 */
	public UfoAnimationTask(AsyncTaskCompleteListener<String> callback) {
		this.callback = callback;
	}

	/**
	 * 실제 실행되는 부분
	 */
	@Override
	protected String doInBackground(Object... views) {
		PlanetView view = (PlanetView) views[0];
		Canvas canvas = (Canvas) views[1];
		AnimateDrawable mDrawable = (AnimateDrawable) views[2];
		
		mDrawable.draw(canvas);
		view.invalidate();
		
		return "";
	}

	/**
	 * 작업이 끝나면 자동으로 실행된다.
	 */
	@Override
	protected void onPostExecute(String result) {

		// 호출한 클래스 콜백
		if (callback != null)
			callback.onTaskComplete(result);
	}

}
