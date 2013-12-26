package com.gif.eting.svc.gcm;

import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.gif.eting.dao.AdminMsgDAO;
import com.gif.eting.dao.InboxDAO;
import com.gif.eting.dao.SettingDAO;
import com.gif.eting.dto.AdminMsgDTO;
import com.gif.eting.dto.StoryDTO;
import com.gif.eting.svc.StoryService;
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
                
                String type = extras.getString("type");
                
                //다른사람 코멘트일때
                if("Stamp".equals(type)){
                	String storyId = extras.getString("story_id");
                    String stamps = extras.getString("stamps");	//,로 이어져있음
                    String comment = extras.getString("comment");
                    
                    StoryService svc = new StoryService(this);
                    svc.updStoryStamp(storyId, stamps, comment);
                                    
                    sendNotification(storyId);
                    Log.i(TAG, "Received: " + extras.toString());
                    
                }else if("Admin".equals(type)){	//관리자 메세지일때
                	String msgId = extras.getString("msgId");
                	String content = extras.getString("content");
                	saveAdminMessage(msgId, content);
                	
                	String isNoti = extras.getString("isNoti");
                	if("Y".equals(isNoti)){
                		//관리자 알람일 때에는 그냥 이팅만 켠다.
                		makeNotification("", content);
                	}
                }else if("Inbox".equals(type)){	//받은 이야기일때
                	
        			/**
        			 * 서버에서 받아온 다른사람의 이야기를 처리하는 부분
        			 */

    				// 받아온 이야기
    				StoryDTO recievedStoryDto = new StoryDTO();
    				String recievedStoryId = extras.getString("story_id");
    				String recievedContent = extras.getString("content");
    				String recievedStoryDate = extras.getString("story_date");
    				String recievedStoryTime = extras.getString("story_time");
    				recievedStoryDto.setIdx(Long.parseLong(recievedStoryId));
    				recievedStoryDto.setContent(recievedContent);
    				recievedStoryDto.setStory_date(recievedStoryDate);
    				recievedStoryDto.setStory_time(recievedStoryTime);
    				
    				InboxDAO inboxDao = new InboxDAO(getApplicationContext());
    				inboxDao.open();	//열고		
    				inboxDao.insStory(recievedStoryDto);	//입력하고
    				inboxDao.close();	//닫고
                	
                }
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
    	
    	//지워진 이야기면 끝내기
    	StoryService svc = new StoryService(this);
    	try{
        	svc.getMyStory(storyId);    		
    	}catch(Exception e){
        	Log.i("check story", e.toString());
        	return;
    	}
		
    	makeNotification(storyId, "eting!");
        
    }
    
    @SuppressLint("InlinedApi")
	public void makeNotification(String storyId, String content){ 	
    	
    	SettingDAO settingDao = new SettingDAO(getApplicationContext());
    	
    	settingDao.open();
    	Object alarm = settingDao.getsettingInfo("push_alarm");
    	settingDao.close();
    	
    	//알람설정 off면 끝내기
    	if(alarm!=null){
    		return;
    	}
    	
    	mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent;
        
		intent = new Intent(this, IntroActivity.class);

		//다른사람이 등록한 코멘트 알림일때만 로직 실행.
		if(!"".equals(storyId)){ 			
	 		intent.putExtra("GCM", true);
	 		intent.putExtra("storyId", storyId);
		}
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
        .setSmallIcon(R.drawable.ic_launcher)
        .setContentTitle("Eting")
        .setStyle(new NotificationCompat.BigTextStyle()
        .bigText("Eting!"))
        .setContentText(content)
        .setAutoCancel(true);
        
        //TODO 진동기능 설정값으로 줘야하나?
        mBuilder.setVibrate(new long[] {100, 100, 200, 200});

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
    
    /**
     * 푸쉬알림 보내기
     * 
     * @param storyId
     */
    public void makeNotification(String storyId){
    	makeNotification(storyId, "");
    }
    
    /**
     * 관리자 알림 메세지 저장
     * @param msgId
     * @param content
     */
    public void saveAdminMessage(String msgId, String content){
    	AdminMsgDAO adminMsgDAO = new AdminMsgDAO(getApplicationContext());
    	adminMsgDAO.open();
    	AdminMsgDTO msgDto;
    	
    	msgDto = new AdminMsgDTO();    	
    	msgDto.setMsgId(msgId);
    	msgDto.setMsgContent(content);
    	adminMsgDAO.insAdminMsg(msgDto);
    	
    	adminMsgDAO.close();
    }
    
}
