package com.gif.eting.act.frg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.act.ReadInboxActivity;
import com.gif.eting.act.SettingActivity;
import com.gif.eting.act.view.Cloud1View;
import com.gif.eting.act.view.Cloud2View;
import com.gif.eting.act.view.Cloud3View;
import com.gif.eting.act.view.Cloud4View;
import com.gif.eting.act.view.EtingLogoView;
import com.gif.eting.act.view.PlanetView;
import com.gif.eting.svc.InboxService;
import com.gif.eting.svc.StoryService;
import com.gif.eting_dev.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * 메인화면
 * 
 * @author lifenjoy51
 * 
 */
public class MainFragment extends SherlockFragment implements OnClickListener {
	@SuppressWarnings("unused")
	private ViewPager mPager;
	
	private TextView mainToday;
	private TextView mainEtingCnt;
	private TextView mainInboxCnt;
	private Typeface nanum;

	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.fl
	 */
	public static MainFragment create() {
		MainFragment fragment = new MainFragment();
		return fragment;
	}

	/**
	 * 생성자
	 */
	public MainFragment() {
	}

	/**
	 * ViewPager setter
	 * 
	 * @param mPager
	 */
	public void setViewPager(ViewPager mPager) {
		this.mPager = mPager;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout containing a title and body text.
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main,
				container, false);
		rootView.setAnimationCacheEnabled(true);
		rootView.setDrawingCacheEnabled(true);
		nanum = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumGothic.ttf");


		/**
		 * 애니메이션
		 */
		FrameLayout fr = (FrameLayout) rootView.findViewById(R.id.main_frame);
		fr.addView(new PlanetView(getActivity())); // 메인동그라미
		fr.bringToFront();
		
		
		
		//스크린크기
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		/**
		 * 로고이미지
		 */
		fr.addView(new EtingLogoView(getActivity())); // 메인동그라미
		
		/**
		 * 현재날짜
		 */
		mainToday =  (TextView) rootView.findViewById(R.id.main_today);

		mainToday.setTypeface(nanum);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
        Date date = new Date();
		String today = formatter.format(date);	
		mainToday.setText(today);
		
		//위치조정
		int dateX = width*10/100;
		int dateY = height*81/100;
		
		FrameLayout.LayoutParams dataParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		dataParams.leftMargin = dateX; //Your X coordinate
		dataParams.topMargin = dateY; //Your Y coordinate
		dataParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainToday.setLayoutParams(dataParams);

		
		
		/**
		 * 설정아이콘
		 */
		ImageButton settingBtn = (ImageButton) rootView.findViewById(R.id.setting_btn);
		settingBtn.bringToFront();
		
		//위치조정
		int settingX = width*84/100 - settingBtn.getWidth();		
		int settingY = height*82/100 - settingBtn.getHeight();
		
		FrameLayout.LayoutParams settingParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		settingParams.leftMargin = settingX; //Your X coordinate
		settingParams.topMargin = settingY; //Your Y coordinate
		settingParams.gravity = Gravity.LEFT | Gravity.TOP;
		settingBtn.setLayoutParams(settingParams);
		
		
		/**
		 * 별모양1
		 */
		ImageView mainAcc1 = (ImageView) rootView.findViewById(R.id.main_acc_1);
		//위치조정
		int mainAcc1X = width*15/100;
		int mainAcc1Y = height*19/100;
		FrameLayout.LayoutParams mainAcc1Params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		mainAcc1Params.leftMargin = mainAcc1X; //Your X coordinate
		mainAcc1Params.topMargin = mainAcc1Y; //Your Y coordinate
		mainAcc1Params.gravity = Gravity.LEFT | Gravity.TOP;
		mainAcc1.setLayoutParams(mainAcc1Params);
		
		/**
		 * 별모양2
		 */
		ImageView mainAcc2 = (ImageView) rootView.findViewById(R.id.main_acc_2);
		//위치조정
		int mainAcc2X = width*83/100;
		int mainAcc2Y = height*58/100;
		FrameLayout.LayoutParams mainAcc2Params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		mainAcc2Params.leftMargin = mainAcc2X; //Your X coordinate
		mainAcc2Params.topMargin = mainAcc2Y; //Your Y coordinate
		mainAcc2Params.gravity = Gravity.LEFT | Gravity.TOP;
		mainAcc2.setLayoutParams(mainAcc2Params);
		

		
        /**
         * 클릭이벤트 설정
         */
        rootView.findViewById(R.id.main_inbox_cnt).setOnClickListener(this);
  		rootView.findViewById(R.id.setting_btn).setOnClickListener(this);  		
  		
  		/**
  		 * 다시그리기
  		 */
  		rootView.refreshDrawableState();
  		
        return rootView;
    }
	
	

	@Override
	public void onResume() {
		super.onResume();
		
		/**
		 * 받은편지함 관련 정보 설정(개수, 우주선)
		 */
		setInboxCnt();
		
		/**
		 * gcm test
		 */
		int gcmChk = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());
		ConnectionResult cr = new ConnectionResult(gcmChk, null);
		System.out.println(cr.toString());
	}

	/**
	 * 클릭이벤트 실행
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.main_inbox_cnt:
			/**
			 * 받은이야기 보는 로직
			 */
			this.checkInbox();
			break;

		case R.id.setting_btn:
			/**
			 * 설정화면으로 이동
			 */
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;

		}

	}

	/**
	 * 받은편지함 개수 확인하고 열지 안열지 체크
	 */
	public void checkInbox() {
		//받은편지함 개수를 확인하고 한개 이상이면 실행
		InboxService is = new InboxService(getActivity());
		int inboxCnt = is.getInboxCnt();
		if(inboxCnt>0){
			startActivity(new Intent(getActivity(), ReadInboxActivity.class)); // 팝업
		}
	}
	
	/**
	 * 받은편지함 관련 정보 설정(개수, 우주선)
	 */
	public void setInboxCnt(){
		//스크린크기
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
//		Typeface boldtYoon = Typeface.createFromAsset(this.getAssets(), "fonts/yoon530_TT.ttf");
		
		
		/**
		 * 내 이야기개수 설정
		 */
//		etingTextView = (TextView) getView().findViewById(R.id.eting_textview);
		mainEtingCnt =  (TextView) getView().findViewById(R.id.main_eting_cnt);
		mainEtingCnt.setTypeface(nanum);
//		etingTextView.setTypeface(nanum);
		mainEtingCnt.setPaintFlags(mainEtingCnt.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		StoryService svc = new StoryService(getActivity());
		int storyCnt = svc.getStoryCnt();
		mainEtingCnt.setText(String.valueOf(storyCnt) + "  eting");
		
		Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "fonts/NanumGothic.ttf");
		mainEtingCnt.setTypeface(face);
				
		//위치조정
		int cntX = width*10/100;
		int cntY = height*74/100;	
		FrameLayout.LayoutParams mainEtingParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		mainEtingParams.leftMargin = cntX; //Your X coordinate
		mainEtingParams.topMargin = cntY; //Your Y coordinate
		mainEtingParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainEtingCnt.setLayoutParams(mainEtingParams);
		mainEtingCnt.bringToFront();

		/**
		 * 받은편지가 있으면 우주선표시
		 */
		/*
		Drawable dr = getActivity().getResources().getDrawable(R.drawable.spaceship);
		ImageView mainUfo = (ImageView) getView().findViewById(R.id.main_ufo);
		
		
		//위치조정
		int ufoX = width*76/100 - dr.getIntrinsicWidth()/2;
		int ufoY = height*13/100 - dr.getIntrinsicHeight()/2;
		
		FrameLayout.LayoutParams ufoParams 
		= new FrameLayout.LayoutParams(dr.getIntrinsicWidth(),dr.getIntrinsicHeight());
		ufoParams.leftMargin = ufoX; //Your X coordinate
		ufoParams.topMargin = ufoY; //Your Y coordinate
		ufoParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainUfo.setLayoutParams(ufoParams);
		*/
		/**
		 * 받은편지함 개수 설정
		 */
		Drawable dr = getActivity().getResources().getDrawable(R.drawable.spaceship);
		mainInboxCnt =  (TextView) getView().findViewById(R.id.main_inbox_cnt);
		mainInboxCnt.setTypeface(nanum);
		InboxService is = new InboxService(getActivity());
		int inboxCnt = is.getInboxCnt();
		mainInboxCnt.setText(String.valueOf(inboxCnt));
		mainInboxCnt.setBackgroundResource(R.drawable.spaceship);
		mainInboxCnt.setGravity(Gravity.CENTER);
		
		//위치조정
		int inboxX = width*76/100 - dr.getIntrinsicWidth()/2;
		int inboxY = height*13/100 - dr.getIntrinsicHeight()/2;	
		FrameLayout.LayoutParams mainInboxParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		mainInboxParams.leftMargin = inboxX; //Your X coordinate
		mainInboxParams.topMargin = inboxY; //Your Y coordinate
		mainInboxParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainInboxCnt.setLayoutParams(mainInboxParams);
		
		
		if(inboxCnt>0){
			//mainUfo.setVisibility(View.VISIBLE);
			//mainUfo.bringToFront();
			mainInboxCnt.setVisibility(View.VISIBLE);
			mainInboxCnt.bringToFront();
			Animation ani;
			ani = AnimationUtils.loadAnimation(getActivity(), R.anim.twinkle);
			//mainUfo.startAnimation(ani);
			//mainInboxCnt.startAnimation(ani);
		}else{
			//mainUfo.clearAnimation();
			//mainUfo.setVisibility(View.GONE);
			mainInboxCnt.clearAnimation();
			mainInboxCnt.setVisibility(View.GONE);
		}
	
	}
	
	/**
	 * 뒤로가기 눌렀을때 이벤트 처리를 위한 로직
	 * 
	 * @param keyCode
	 * @return
	 */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("onKeyDown SUB", String.valueOf(keyCode));
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Ask the user if they want to quit
			new AlertDialog.Builder(getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.quit)
					.setMessage(R.string.really_quit)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									// Stop the activity
									//getActivity().finish();
									System.exit(0);
									System.out.println("is end??");
								}

							}).setNegativeButton(R.string.no, null).show();

			return true;
		}
		return true;
	}

}