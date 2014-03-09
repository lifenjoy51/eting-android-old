package com.gif.eting.etc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

/**
 * HTTP통신 유틸 한글문제 해결
 */
public class HttpUtil {

	/**
	 * Map으로 받아온 파라미터를 인코딩해서 넘긴다.
	 *
	 * @param urlStr
	 * @param map
	 * @return
	 */
	public static String doPost(String urlStr, Map<String, String> map) {
		StringBuffer sb = new StringBuffer();
		try {
			for (Entry<String, String> e : map.entrySet()) {
				sb.append("&");
				sb.append(e.getKey());
				sb.append("=");
				sb.append(URLEncoder.encode(e.getValue(), "UTF-8"));
			}
			sb.substring(1, sb.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String params = sb.toString();
		return doPost(urlStr, params);
	}

	public static String doPost(String urlStr, String params) {

		try {
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

			//TODO 테스트용
			//String msg = String.format("HTTP : %s \t %s \n",urlStr, sb.toString());
			//Log.i("HTTP", msg);

			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "HttpUtil_Error";
		}
	}

}
