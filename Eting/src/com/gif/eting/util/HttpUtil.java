package com.gif.eting.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public String doPost(String urlString, String params) {
		try {
			// (1)
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			
			con.setDefaultUseCaches(false);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(con.getOutputStream(),"UTF-8"));
			pw.write(params);
			pw.flush();
			
			BufferedReader bf  = new BufferedReader(new InputStreamReader(con.getInputStream(),"UTF-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			
			while((line = bf.readLine())!=null){
				sb.append(line);
			}

			return sb.toString();
		} catch (Exception e) {
			return e.toString();
		}
	}
}
