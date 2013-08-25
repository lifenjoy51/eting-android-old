package com.gif.eting.act.frg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
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
import com.gif.eting.R;
import com.gif.eting.act.ReadInboxActivity;
import com.gif.eting.act.SettingActivity;
import com.gif.eting.act.view.CloudView;
import com.gif.eting.svc.InboxService;

/**
 * 메인화면
 * 
 * @author lifenjoy51
 * 
 */
public class MainFragment extends SherlockFragment implements OnClickListener {
	@SuppressWarnings("unused")
	private ViewPager mPager;

	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.
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

		/**
		 * 애니메이션
		 */
		FrameLayout fr = (FrameLayout) rootView.findViewById(R.id.main_bg);
		
		fr.addView(new CloudView(getActivity(), 1)); // 움직이는 UFO 등록
		fr.addView(new CloudView(getActivity(), 2)); // 움직이는 UFO 등록
		
		//스크린크기
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		
		/**
		 * 현재날짜
		 */
		TextView mainToday =  (TextView) rootView.findViewById(R.id.main_today);
		String today = "2013-08-25";	//TODO 현재날짜 구해오는 로직 구현필요
		mainToday.setText(today);
		//위치조정
		int dateX = width/100*14;
		int dateY = height/100*65;
		
		FrameLayout.LayoutParams dataParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		dataParams.leftMargin = dateX; //Your X coordinate
		dataParams.topMargin = dateY; //Your Y coordinate
		dataParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainToday.setLayoutParams(dataParams);
		
		/**
		 * 설정아이콘
		 */
		ImageButton settingBtn = (ImageButton) rootView.findViewById(R.id.setting_btn);
		//위치조정
		int settingX = width/100*80;
		int settingY = height/100*80;
		
		FrameLayout.LayoutParams settingParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		settingParams.leftMargin = settingX; //Your X coordinate
		settingParams.topMargin = settingY; //Your Y coordinate
		settingParams.gravity = Gravity.LEFT | Gravity.TOP;
		settingBtn.setLayoutParams(settingParams);

        /**
         * 클릭이벤트 설정
         */
        rootView.findViewById(R.id.main_ufo).setOnClickListener(this);
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
	}

	/**
	 * 클릭이벤트 실행
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.main_ufo:
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
		/**
		 * 받은편지함 개수 설정
		 */
		TextView mainEtingCnt =  (TextView) getView().findViewById(R.id.main_eting_cnt);
		InboxService is = new InboxService(getActivity());
		int inboxCnt = is.getInboxCnt();
		mainEtingCnt.setText(String.valueOf(inboxCnt));	
		
		//스크린크기
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
				
		//위치조정
		int cntX = width/100*16;
		int cntY = height/100*72;	
		FrameLayout.LayoutParams mainEtingParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		mainEtingParams.leftMargin = cntX; //Your X coordinate
		mainEtingParams.topMargin = cntY; //Your Y coordinate
		mainEtingParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainEtingCnt.setLayoutParams(mainEtingParams);
		
		/**
		 * 받은편지가 있으면 우주선표시
		 */
		ImageView mainUfo = (ImageView) getView().findViewById(R.id.main_ufo);
		//위치조정
		int ufoX = width/100*70;
		int ufoY = height/100*15;
		FrameLayout.LayoutParams ufoParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT); //The WRAP_CONTENT parameters can be replaced by an absolute width and height or the FILL_PARENT option)
		ufoParams.leftMargin = ufoX; //Your X coordinate
		ufoParams.topMargin = ufoY; //Your Y coordinate
		ufoParams.gravity = Gravity.LEFT | Gravity.TOP;
		mainUfo.setLayoutParams(ufoParams);
		
		if(inboxCnt>0){
			mainUfo.setVisibility(View.VISIBLE);
			mainUfo.bringToFront();
			Animation ani;
			ani = AnimationUtils.loadAnimation(getActivity(), R.anim.twinkle);
			mainUfo.startAnimation(ani);
		}else{
			mainUfo.setVisibility(View.GONE);
		}
	
	}

}