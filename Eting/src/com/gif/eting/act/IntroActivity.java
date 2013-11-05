package com.gif.eting.act;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.gif.eting.R;
import com.gif.eting.act.view.UfoView;
import com.gif.eting.svc.PasswordService;
import com.gif.eting.svc.task.CheckStampTask;
import com.gif.eting.svc.task.RegistrationTask;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting.util.Counter;
import com.gif.eting.util.CounterListener;
import com.gif.eting.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

/**
 * 인트로화면
 * 
 * @author lifenjoy51
 * 
 */
public class IntroActivity extends Activity implements CounterListener{

	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private boolean isFirst = false;

	private boolean isDelayed = false;
	private boolean isLoaded= false;
	private Handler handler;

	private ImageView introBg1;
	private ImageView introBg2;
	private static UfoView ufo;
	
	//GCM 관련 변수
	private boolean isGcm;
	private String storyId;

	/**
	 * Substitute you own sender ID here. This is the project number you got
	 * from the API Console, as described in "Getting Started."
	 */
	String SENDER_ID = "112355150629";

	/**
	 * Tag used on log messages.
	 */
	static final String TAG = "GCM Demo";

	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	static Context context;
	String regid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		context = getApplicationContext();
		
		//android.os.SystemClock.sleep(3000);

		FrameLayout fr = (FrameLayout) findViewById(R.id.intro_layout);
		introBg1 = (ImageView) findViewById(R.id.intro_bg1);
		introBg2 = (ImageView) findViewById(R.id.intro_bg2);

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int height = metrics.heightPixels;
		
		//배경 원 이미지 Drawable
		Drawable introBg1Dr = context.getResources().getDrawable(R.drawable.intro_backgroung);

		// 수직 위치 시작
		int stBg1Y = height * 43 / 100 - (introBg1Dr.getIntrinsicHeight())/2;
		int stBg2Y = height * 62 / 100;
		
		introBg1.setPadding(0, stBg1Y, 0, 0);
		introBg2.setPadding(0, stBg2Y, 0, 0);

		ufo = new UfoView(this);
		fr.addView(ufo); // 움직이는 UFO 등록
		this.setRepeat();
		
		/**
		 * 서버와 스탬프목록 동기화
		 * 
		 * CheckStampTask 파라미터는 CheckStampTask 수행되고 나서 실행될 콜백이다. execute의 파라미터가
		 * 실제 넘겨줄 자료들. parameter[0] = this. Context.
		 */
		new CheckStampTask(new AfterCheckStampTask()).execute(this);

		/**
		 * 스탬프찍힌 이야기 리스트 받아오기
		 * 
		 * CheckStampedStoryTask 파라미터는 CheckStampedStoryTask 수행되고 나서 실행될 콜백이다.
		 * execute의 파라미터가 실제 넘겨줄 자료들. parameter[0] = this. Context.
		 */
		//new CheckStampedStoryTask(new AfterCheckStampedStoryTask()).execute(this);

		/**
		 * GCM 등록체크
		 */
		context = getApplicationContext();

		// Check device for Play Services APK. If check succeeds, proceed with
		// GCM registration.
		//System.out.println("checkPlayServices() "+checkPlayServices());
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(this);
			regid = getRegistrationId(context);

			if ("".equals(regid)) {
				registerInBackground();
				isFirst = true;
			} else {
				sendRegistrationIdToBackend(); // TODO 임시로 만들어놓은거.
			}
		} else {
			Log.i(TAG, "No valid Google Play Services APK found.");
		}
		
		//리스너 등록
		Counter.setListner(this);
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				isDelayed = true;
				startNextActivity();
				
			}
		}, 3000); // 3초후 이동
		
		/**
		 * GCM으로 받은경우 페이지이동
		 */
        Intent intent = getIntent();
		isGcm = intent.getBooleanExtra("GCM", false);	//GCM여부
		if(isGcm){
			storyId = intent.getStringExtra("storyId");			
		}
	}
	
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		/**
		 * Util클래스에 설정값 초기화
		 */
		Util.init(context);	//비동기 실행을 어느시점에 해야 먼저 애니메이션이 떠오르지?
		
		super.onPostCreate(savedInstanceState);
	}



	/**
	 * SendStoryTask수행 후 실행되는 콜백 애니메이션이 3초이상 지속되고 스탬프찍힌 이야기를 검사하고 스탬프 동기화가 완료되면
	 * 이동한다!
	 */
	private class AfterCheckStampTask implements
			AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {
			//Log.i("AfterCheckStampTask", result);
			if("UnknownHostException".equals(result)){
				processError();
			}
		}
	}

	/**
	 * SendStoryTask수행 후 실행되는 콜백 애니메이션이 3초이상 지속되고 스탬프 동기화를 마치고 스탬프찍힌 이야기를 검사하면
	 * 이동한다!
	 */
	@SuppressWarnings("unused")
	private class AfterCheckStampedStoryTask implements
			AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {
			//Log.i("AfterCheckStampTask", result);
			if("UnknownHostException".equals(result)){
				processError();
			}
		}
	}

	/**
	 * 화면이동
	 */
	@SuppressLint("InlinedApi")
	private void moveToLockScreenActivity() {
		if(isFirst){
			return;
		}
		
		PasswordService psvc = new PasswordService(this);
		if (psvc.isPassword()) {
			Intent intent = new Intent(IntroActivity.this,
					LockScreenActivity.class);
			intent.putExtra("GCM", isGcm);
	        intent.putExtra("storyId", storyId);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
		} else {

			Intent intent = new Intent(IntroActivity.this, MainViewPagerActivity.class);
			intent.putExtra("GCM", isGcm);
	        intent.putExtra("storyId", storyId);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
		}
		finish(); // 뒤로가기 했을경우 안나오도록 없애주기

	}

	/**
	 * Check the device to make sure it has the Google Play Services APK. If it
	 * doesn't, display a dialog that allows users to download the APK from the
	 * Google Play Store or enable it in the device's system settings.
	 */
	public boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(context);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				//System.out.println(GooglePlayServicesUtil.getErrorString(resultCode));
			} else {
				Log.i(TAG, "This device is not supported.");				
			}
			return false;
		}
		return true;
	}

	/**
	 * Gets the current registration ID for application on GCM service, if there
	 * is one.
	 * <p>
	 * If result is empty, the app needs to register.
	 * 
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGcmPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if ("".equals(registrationId)) {
			Log.i(TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			Log.i(TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGcmPreferences(Context context) {
		// This sample app persists the registration ID in shared preferences,
		// but
		// how you store the regID in your app is up to you.
		return getSharedPreferences(IntroActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and the app versionCode in the application's
	 * shared preferences.
	 */
	public void registerInBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcm == null) {
						gcm = GoogleCloudMessaging.getInstance(context);
					}
					regid = gcm.register(SENDER_ID);
					msg = "Device registered, registration ID=" + regid;

					// You should send the registration ID to your server over
					// HTTP, so it
					// can use GCM/HTTP or CCS to send messages to your app.
					sendRegistrationIdToBackend();

					// For this demo: we don't need to send it because the
					// device will send
					// upstream messages to a server that echo back the message
					// using the
					// 'from' address in the message.

					// Persist the regID - no need to register again.
					storeRegistrationId(context, regid);
				} catch (IOException ex) {
					ex.printStackTrace();
					msg = "Error :" + ex.getMessage();
					// If there is an error, don't just keep trying to register.
					// Require the user to click a button again, or perform
					// exponential back-off.
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				//Log.i("main onPostExecute ", msg + "\n");
			}
		}.execute(null, null, null);
	}

	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	/**
	 * Stores the registration ID and the app versionCode in the application's
	 * {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration ID
	 */
	private void storeRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGcmPreferences(context);
		int appVersion = getAppVersion(context);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		editor.putInt(PROPERTY_APP_VERSION, appVersion);
		editor.commit();
	}

	/**
	 * Sends the registration ID to your server over HTTP, so it can use
	 * GCM/HTTP or CCS to send messages to your app. Not needed for this demo
	 * since the device sends upstream messages to a server that echoes back the
	 * message using the 'from' address in the message.
	 */
	private void sendRegistrationIdToBackend() {
		//Log.i("sendRegistrationIdToBackend", regid);

		/**
		 * 폰고유ID와 메세지를 받기위한 고유ID를 서버에 전송
		 */
		new RegistrationTask().execute(regid, context);
	}
	
	/**
	 * 에러처리 로직
	 */
	private void processError(){
		//System.out.println("processError");
		
		Toast toast = Toast.makeText(this,R.string.cannot_connect_to_internet,
				Toast.LENGTH_LONG);
		toast.show();
	}
	

	/**
	 * 애니메이션 관련 변수
	 */
	static Handler hdler;
	static int delayTime = (int) Util.fps;

	static class  MyHandler extends Handler {
		public void handleMessage(Message msg) {
			doRepeatedly();
			hdler.sendMessageDelayed(new Message(), delayTime);
		}
	}

	public static void doRepeatedly() {		
		ufo.invalidate();
	}

	public void setRepeat() {
		hdler = new MyHandler();
		hdler.sendMessageDelayed(new Message(), delayTime);
	}

	@Override
	public void onEvent(Integer cnt) {
		isLoaded=true;
		startNextActivity();
		
	}
	
	/**
	 * 처음접속이면 튜토리얼
	 * 아니면 비밀번호화면으로
	 */
	@SuppressLint("InlinedApi")
	public void startNextActivity(){
		if(!isLoaded || !isDelayed){
			return;
		}
		
		hdler.removeMessages(0, null);
		
		if (isFirst) {

			Intent intent = new Intent(IntroActivity.this,
					TutorialActivity.class);
			intent.putExtra("isFirst", true);
			
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			
			startActivity(intent);
			overridePendingTransition(R.anim.fade, R.anim.hold);
		
			finish();

		}else {
			moveToLockScreenActivity();
		}
		
	}



}
