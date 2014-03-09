package com.gif.eting.svc.gcm;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.gif.eting.R;
import com.gif.eting.SplashActivity;
import com.gif.eting.dao.InboxDAO;
import com.gif.eting.obj.Story;
import com.gif.eting.svc.StoryService;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	public static final String TAG = "GCM";
	public static final int NOTIFICATION_ID = Notification.FLAG_AUTO_CANCEL;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		String messageType = gcm.getMessageType(intent);

		// 메세지받아오면
		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

				String type = extras.getString("content_type");

				if ("Reply".equals(type)) {
					// 다른사람 코멘트일때
					handleReply(extras);
				} else if ("Notify".equals(type)) {
					// 관리자 메세지일때
					handleNotify(extras);
				} else if ("Story".equals(type)) {
					// 받은 이야기일때
					handleStory(extras);

				}
			}
		}

		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	/**
	 * 답글 저장
	 * 
	 * @param extras
	 */
	private void handleReply(Bundle extras) {

		String storyId = extras.getString("story_id");
		String emoticonList = extras.getString("emoticon_list"); // ,로
																	// 이어져있음
		String replyContent = extras.getString("reply_content");
		String replyId = extras.getString("reply_id");
		try {
			StoryService svc = new StoryService(this);
			int result = svc.updStoryReply(storyId, emoticonList, replyContent,
					replyId);
			if (result > 0) {
				makeNotification(storyId);
			}
		} catch (Exception e) {
			Log.i("stamped story", e.toString());
			return;
		}
		Log.i(TAG, "Received: " + extras.toString());
	}

	/**
	 * 공지메세지
	 * 
	 * @param extras
	 */
	private void handleNotify(Bundle extras) {

		String msgId = extras.getString("msgId");
		String content = extras.getString("content");
		saveAdminMessage(msgId, content);

		String isNoti = extras.getString("isNoti");
		// 관리자 알람일 때에는 그냥 이팅만 켠다.
		if ("Y".equals(isNoti)) {
			makeNotification("");
		}
	}

	/**
	 * 서버에서 받아온 다른사람의 이야기를 처리하는 부분
	 */
	private void handleStory(Bundle extras) {
		// 받아온 이야기
		Story recievedStoryDto = new Story();
		String recievedStoryId = extras.getString("story_id");
		String recievedContent = extras.getString("content");
		String recievedStoryDate = extras.getString("story_date");
		String recievedStoryTime = extras.getString("story_time");
		recievedStoryDto.setStory_id(recievedStoryId);
		recievedStoryDto.setStory_content(recievedContent);
		recievedStoryDto.setStory_date(recievedStoryDate);
		recievedStoryDto.setStory_time(recievedStoryTime);

		// 입력하고
		InboxDAO inboxDao = new InboxDAO(getApplicationContext());
		inboxDao.insStory(recievedStoryDto);
	}

	/**
	 * 푸쉬알림
	 * 
	 * @param storyId
	 * @param content
	 */
	@SuppressLint("InlinedApi")
	public void makeNotification(String storyId) {

		// 지워진 이야기면 끝내기
		StoryService svc = new StoryService(this);
		try {
			svc.getMyStory(storyId);
		} catch (Exception e) {
			Log.i("check story", e.toString());
			return;
		}

		// 알림여부
		SharedPreferences pref = getSharedPreferences("eting",
				Context.MODE_PRIVATE);
		boolean isAlarm = pref.getBoolean("push_alarm", true);

		// 알람설정 off면 끝내기
		if (!isAlarm) {
			return;
		}

		mNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);

		Intent intent = new Intent(this, SplashActivity.class);

		// 다른사람이 등록한 코멘트 알림일때만 로직 실행.
		if (!"".equals(storyId)) {
			intent.putExtra("GCM", true);
			intent.putExtra("storyId", storyId);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Eting")
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText("Eting!"))
				.setContentText("eting!").setAutoCancel(true);

		// TODO 진동기능 설정값으로 줘야하나?
		mBuilder.setVibrate(new long[] { 100, 100, 200, 200 });

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

	/**
	 * 관리자 알림 메세지 저장
	 * 
	 * @param msgId
	 * @param content
	 */
	public void saveAdminMessage(String msgId, String content) {
		SharedPreferences pref = getSharedPreferences("eting",
				Context.MODE_PRIVATE);

		pref.edit().putString("notify_id", msgId).commit();
		pref.edit().putString("notify_ufo", content).commit();
	}

}
