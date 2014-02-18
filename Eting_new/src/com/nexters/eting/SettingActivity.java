package com.nexters.eting;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class SettingActivity extends Activity implements OnClickListener {

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
//
//	private ImageView alarm_img_btn;
//	private ImageView password_img_btn;
//	private ImageView email_btn;
//	private ImageView credit_img_btn;
//	private ImageView tutorialBtn;
//	private TextView setting_textView;
//	private SettingDAO settingDao;
//
//	// private Uri fileUri;
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.setting);
//		
//		//Dao초기화
//		settingDao = new SettingDAO(getApplicationContext());
//		
//
//		// 버튼 온클릭이벤트 등록
//		alarm_img_btn = (ImageView) findViewById(R.id.alarm_img_btn);
//		password_img_btn = (ImageView) findViewById(R.id.password_img_btn);
//		email_btn = (ImageView) findViewById(R.id.email_btn);
//		credit_img_btn = (ImageView) findViewById(R.id.credit_img_btn);
//		tutorialBtn = (ImageView) findViewById(R.id.tutorialBtn);
//		setting_textView = (TextView) findViewById(R.id.setting_textView);
//		
//
//		//알람설정
//		settingDao.open();
//    	Object alarm = settingDao.getsettingInfo("push_alarm");
//    	settingDao.close();
//    	
//    	//알람설정 off면 이미지 교체
//    	if(alarm!=null){
//    		alarm_img_btn.setImageResource(R.drawable.alarm_off);
//    	}else{
//    		alarm_img_btn.setImageResource(R.drawable.alarm_on);
//    	}
//
//		// 글꼴
//		//setting_textView.setTypeface(Util.getNanum(getApplicationContext()));
//		setting_textView.setPaintFlags(setting_textView.getPaintFlags()
//				| Paint.FAKE_BOLD_TEXT_FLAG);
//		
//		password_img_btn.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					password_img_btn = (ImageView) findViewById(v.getId());
//					password_img_btn
//							.setImageResource(R.drawable.password_2);
//				}
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					if (!v.isPressed()) {
//						password_img_btn
//								.setImageResource(R.drawable.password_1);
//						return true;
//					}
//
//				}
//
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					password_img_btn
//							.setImageResource(R.drawable.password_1);
//				}
//
//				return false;
//			}
//		});
//		email_btn.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					email_btn = (ImageView) findViewById(v.getId());
//					email_btn.setImageResource(R.drawable.sendemail_2);
//				}
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					if (!v.isPressed()) {
//						email_btn.setImageResource(R.drawable.sendemail_1);
//						return true;
//					}
//
//				}
//
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					email_btn.setImageResource(R.drawable.sendemail_1);
//				}
//
//				return false;
//			}
//		});
//		credit_img_btn.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					credit_img_btn = (ImageView) findViewById(v.getId());
//					credit_img_btn.setImageResource(R.drawable.aboutus_2);
//				}
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					if (!v.isPressed()) {
//						credit_img_btn.setImageResource(R.drawable.aboutus_1);
//						return true;
//					}
//
//				}
//
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					credit_img_btn.setImageResource(R.drawable.aboutus_1);
//				}
//
//				return false;
//			}
//		});
//		tutorialBtn.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					tutorialBtn = (ImageView) findViewById(v.getId());
//					tutorialBtn.setImageResource(R.drawable.tutorial2);
//				}
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					if (!v.isPressed()) {
//						tutorialBtn.setImageResource(R.drawable.tutorial1);
//						return true;
//					}
//
//				}
//
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					tutorialBtn.setImageResource(R.drawable.tutorial1);
//				}
//
//				return false;
//			}
//		});
//
//		alarm_img_btn.setOnClickListener(this);
//		password_img_btn.setOnClickListener(this);
//		email_btn.setOnClickListener(this);
//		credit_img_btn.setOnClickListener(this);
//		tutorialBtn.setOnClickListener(this);
//
//	}
//
//	@Override
//	public void onClick(View view) {
//		// 비밀번호관리 버튼 클릭시
//		if (view.getId() == R.id.password_img_btn) {
//			startActivity(new Intent(this, PasswordActivity.class));
//		}
//		// 이메일첨부 버튼 클릭시
//		if (view.getId() == R.id.email_btn) {
//			startActivity(new Intent(this, ExportEmailActivity.class));
//		}
//		if (view.getId() == R.id.credit_img_btn) {
//			startActivity(new Intent(this, CreditActivity.class));
//		}
//		if (view.getId() == R.id.alarm_img_btn) {
//			Drawable tempImg = alarm_img_btn.getDrawable();
//			Drawable tempRes = SettingActivity.this.getResources().getDrawable(
//					R.drawable.alarm_on);
//			Bitmap tmpBitmap = ((BitmapDrawable) tempImg).getBitmap();
//			Bitmap tmpBitmapRes = ((BitmapDrawable) tempRes).getBitmap();
//
//			if (tmpBitmap.equals(tmpBitmapRes)) {
//				alarm_img_btn.setImageResource(R.drawable.alarm_off);
//
//				// 로직 수행
//				SettingDTO setting = new SettingDTO();
//				setting.setKey("push_alarm");
//				setting.setValue("N");
//				
//				settingDao.open();
//				settingDao.inssetting(setting);
//				settingDao.close();				
//			} else {
//				alarm_img_btn.setImageResource(R.drawable.alarm_on);
//
//				// 로직 수행
//				settingDao.open();
//				settingDao.delsetting("push_alarm");
//				settingDao.close();
//			}
//		}
//		if (view.getId() == R.id.tutorialBtn) {
//			startActivity(new Intent(this, TutorialActivity.class));
//		}
//
//	}

}