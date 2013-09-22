package com.gif.eting.svc.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gif.eting.R;
import com.gif.eting.act.IntroActivity;
import com.gif.eting.act.LockScreenActivity;
import com.gif.eting.act.MainViewPagerActivity;
import com.gif.eting.dao.SettingDAO;
import com.gif.eting.svc.PasswordService;
import com.gif.eting.svc.task.CheckStampedStoryTask;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = Notification.FLAG_AUTO_CANCEL;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GcmIntentService() {
        super("GcmIntentService");
    }
    public static final String TAG = "GCM";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " + extras.toString());
            // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            	/**
            	 * 메세지받아오면
            	 */            	
                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.
                //TODO 환경설정값에 따라서 메세지를 보여줌
                String storyId = extras.getString("story_id");
                sendNotification(storyId);
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String storyId) {
    	Log.i("sendNotification", storyId);
    	
    	SettingDAO settingDao = new SettingDAO(getApplicationContext());
    	
    	settingDao.open();
    	Object alarm = settingDao.getsettingInfo("push_alarm");
    	settingDao.close();
    	
    	//알람설정 off면 끝내기
    	if(alarm!=null){
    		return;
    	}
    	
    	/**
		 * 스탬프찍힌 이야기 리스트 받아오기
		 * 
		 * execute의 파라미터가 실제 넘겨줄 자료들.
		 * parameter[0] = this. Context.
		 */
		new CheckStampedStoryTask(null).execute(this);
		
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        
        PasswordService psvc = new PasswordService(this);
		if (psvc.isPassword()) {
			intent = new Intent(this, LockScreenActivity.class);
		} else {
			intent = new Intent(this, MainViewPagerActivity.class);
		}
		
        intent.putExtra("GCM", true);
        intent.putExtra("storyId", storyId);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
                | Intent.FLAG_ACTIVITY_CLEAR_TOP 
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0 );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Eting")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText("Eting!"))
        .setContentText("Eting!!")
        .setAutoCancel(true);
        
        //TODO 진동기능 설정값으로 줘야하나?
        mBuilder.setVibrate(new long[] {100, 100, 200, 200});

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        
    }
}
