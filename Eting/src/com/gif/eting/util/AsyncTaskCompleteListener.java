package com.gif.eting.util;

/**
 * AsyncTask 후속처리를 위한 콜백
 * @author lifenjoy51
 *
 * @param <T>
 */
public interface AsyncTaskCompleteListener<T> {

	public void onTaskComplete(T result);

}