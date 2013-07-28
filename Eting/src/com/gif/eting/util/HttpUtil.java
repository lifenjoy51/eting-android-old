package com.gif.eting.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	public String doPost(String url, String params) {
		try {
			// (1)
			HttpPost method = new HttpPost(url);

			DefaultHttpClient client = new DefaultHttpClient();

			// (2)POST데이타 설정
			StringEntity paramEntity = new StringEntity(params);
			paramEntity.setChunked(false);
			// (3)
			paramEntity.setContentType("application/x-www-form-urlencoded");
			method.setEntity(paramEntity);

			HttpResponse response = client.execute(method);
			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK)
				throw new Exception("");

			return EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			return e.toString();
		}
	}
}
