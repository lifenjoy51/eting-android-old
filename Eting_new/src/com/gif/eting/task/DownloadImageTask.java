package com.gif.eting.task;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.gif.eting.etc.AsyncTaskCompleteListener;



public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	ImageView bmImage;
	AsyncTaskCompleteListener<String> callback;

	public DownloadImageTask(ImageView bmImage) {
	    this.bmImage = bmImage;
	}

	public DownloadImageTask(ImageView bmImage, AsyncTaskCompleteListener<String> callback) {
		this.bmImage = bmImage;
		this.callback = callback;

	}

	@Override
	protected Bitmap doInBackground(String... urls) {
	    String urldisplay = urls[0];
	    Bitmap mIcon11 = null;
	    try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	    } catch (Exception e) {
	        Log.e("Error", e.getMessage());
	        e.printStackTrace();
	    }
	    return mIcon11;
	}

	@Override
	protected void onPostExecute(Bitmap result) {
	    bmImage.setImageBitmap(result);

	    if(callback != null){
	    	callback.onTaskComplete("");
	    }
	}

}