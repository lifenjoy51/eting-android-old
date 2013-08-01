package com.gif.eting.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class HttpUtil extends AsyncTask<String, String, String> {
	
	private AsyncTaskCompleteListener<String> callback;
	private String url;
	private String params;

	
	public HttpUtil(String urlString, String params) {
		this(urlString, params, null);
	}
	
	public HttpUtil(String urlString, String params, AsyncTaskCompleteListener<String> callback) {
		this.url = urlString;
		this.params = params;
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... params) {

		try {
			// (1)
			URL url = new URL(this.url);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDefaultUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					con.getOutputStream(), "UTF-8"));
			pw.write(this.params);
			pw.flush();
			pw.close();

			BufferedReader bf = new BufferedReader(new InputStreamReader(
					con.getInputStream(), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;

			while ((line = bf.readLine()) != null) {
				sb.append(line);
			}
			bf.close();

			return sb.toString();
		} catch (Exception e) {
			return e.toString();
		}
	}
	
	

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		Log.i("onCancelled", "");
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(String result) {
		Log.i("onPostExecute", result);
		if (callback != null)
			callback.onTaskComplete(result);
	}

	@Override
	protected void onPreExecute() {
		Log.i("onPreExecute", "");
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(String... values) {
		Log.i("onProgressUpdate", values.toString());
		super.onProgressUpdate(values);
	}

	
	
}
