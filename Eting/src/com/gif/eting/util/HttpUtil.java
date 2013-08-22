package com.gif.eting.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil  {

	public static String doPost(String urlStr, String params) {

		try {
			// (1)
			URL url = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setDefaultUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type",
					"application/x-www-form-urlencoded");

			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					con.getOutputStream(), "UTF-8"));
			pw.write(params);
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
			e.printStackTrace();
			return e.toString();
		}
	}
	
	
	
}
