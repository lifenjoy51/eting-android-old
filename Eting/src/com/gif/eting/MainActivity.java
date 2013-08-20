package com.gif.eting;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener {

	//통지 맴버변수
	private NotificationManager mNotification;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = pendingIntent();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	private PendingIntent pendingIntent() {
		Intent i = new Intent(getApplicationContext(), ReadMyStoryActivity.class);
		PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
		// 폰 화면의 상단에 쓰일 아이콘, 글자, 언제띄우는지
		int icon = R.drawable.ic_launcher;
		String notiText = "첫화면에알림";
		long when = System.currentTimeMillis();

		
		// Noti라는 객체 선언, 위에 쓰인 상단의 값들을 받아오면서 알람이 작동되면 폰 화면 상단에 적힘
		Notification noti = new Notification(icon, notiText, when);

		// 알람이 설정되면서 Activity동작하게함
		noti.setLatestEventInfo(MainActivity.this, "111", "222", pi);
		// Notification_id의 고유 id를 가지는 notification을 표시함
		mNotification.notify(1234, noti);
		// 추가***********************끝

		return pi;
	}
	
}
