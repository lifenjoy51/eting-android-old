package com.gif.eting.etc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 기기 고유번호를 찍어주는 유틸
 *
 * @author lifenjoy51
 *
 */
public class Installation {
	private static String sID = null;
	private static final String INSTALLATION = "INSTALLATION";

	public synchronized static String id(Context context) {
		if (sID == null) {
			File installation = new File(context.getFilesDir(), INSTALLATION);
			try {
				if (!installation.exists())
					writeInstallationFile(installation);
				sID = readInstallationFile(installation);
			} catch (Exception e) {
				SharedPreferences prefs = context.getSharedPreferences("eting",
						Context.MODE_PRIVATE);
				String savedSID = prefs.getString("sid", "");
				if(Util.isEmpty(savedSID)){
					//임시로 현재시간을 key값으로 등록.
					long time = System.currentTimeMillis();
					long nano = System.nanoTime();
					String uuid = String.valueOf(time)+String.valueOf(nano);
					sID = String.format("%36s", uuid).replace(' ', '0');
					prefs.edit().putString("sid", sID).commit();
				}else{
					sID = savedSID;
				}
			}
		}
		return sID;
	}

	private static String readInstallationFile(File installation)
			throws IOException {
		RandomAccessFile f = new RandomAccessFile(installation, "r");
		byte[] bytes = new byte[(int) f.length()];
		f.readFully(bytes);
		f.close();
		return new String(bytes);
	}

	private static void writeInstallationFile(File installation)
			throws IOException {
		FileOutputStream out = new FileOutputStream(installation);
		String id = UUID.randomUUID().toString();
		out.write(id.getBytes());
		out.close();
	}
}