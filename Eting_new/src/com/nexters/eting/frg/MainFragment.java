package com.nexters.eting.frg;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexters.eting.NotifyUfoActivity;
import com.nexters.eting.R;
import com.nexters.eting.ReadInboxActivity;
import com.nexters.eting.SettingActivity;
import com.nexters.eting.etc.Const;
import com.nexters.eting.etc.HttpUtil;
import com.nexters.eting.etc.Installation;
import com.nexters.eting.etc.Util;
import com.nexters.eting.svc.InboxService;
import com.nexters.eting.svc.StoryService;
import com.nexters.eting.view.EtingLogoView;
import com.nexters.eting.view.PlanetView;

/**
 * 메인화면
 * 
 * @author lifenjoy51
 * 
 */
@SuppressLint("HandlerLeak")
public class MainFragment extends BaseFragment implements OnClickListener {

	private TextView mainToday;
	private TextView mainEtingCnt;
	private TextView mainInboxCnt;
	private ImageView notifyUfo;
	private ImageView mainAcc1;
	private PlanetView pv;
	private EtingLogoView elv;

	// 기타 전역변수
	private StoryService storySvc;
	private InboxService inboxSvc;
	private static FrameLayout fr;
	private boolean hasNotifyUfo = false;
	private ViewPager mPager;
	
	//설정 저장소
	SharedPreferences pref;
	

	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.fl
	 */
	public static MainFragment create(int position) {
		MainFragment fragment = new MainFragment();
		return fragment;
	}

	/**
	 * 생성자
	 */
	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(
				R.layout.fragment_main, container, false);
		rootView.setAnimationCacheEnabled(true);
		rootView.setDrawingCacheEnabled(true);
		
		//서비스 초기화
		storySvc = new StoryService(getActivity());
		inboxSvc = new InboxService(getActivity());
		
		//저장소 초기화
		pref = getActivity().getSharedPreferences("eting", Context.MODE_PRIVATE);
		
		//mPager
		mPager = (ViewPager) rootView.getParent();

		// 레이아웃
		fr = (FrameLayout) rootView.findViewById(R.id.main_frame);

		// #####
		// 상단 UI
		// #####

		// 관리자 알람 우주선
		notifyUfo = (ImageView) rootView.findViewById(R.id.notify_ufo);
		Util.setPosition(notifyUfo, R.drawable.notify_ufo, 19, 20);

		// 왼쪽 위 행성모양
		mainAcc1 = (ImageView) rootView.findViewById(R.id.main_acc_1);
		Util.setPosition(mainAcc1, 15, 19);

		// 받은편지함 개수 설정
		mainInboxCnt = (TextView) rootView.findViewById(R.id.main_inbox_cnt);
		Util.setPosition(mainInboxCnt, R.drawable.spaceship, 76, 13);

		// #####
		// 중간 UI
		// #####

		// 돌아가는 동그라미 애니메이션
		pv = new PlanetView(getActivity());
		pv.setId(R.drawable.main_planet);
		fr.addView(pv);

		// 로고이미지
		elv = new EtingLogoView(getActivity());
		elv.setId(R.drawable.eting_logo);
		fr.addView(elv);

		// 별모양2 누르면 이야기를 받아온다.
		View mainAcc2 = rootView.findViewById(R.id.main_acc_2);
		Util.setPosition(mainAcc2, 83, 62);
		mainAcc2.bringToFront();

		// #####
		// 하단 UI
		// #####

		// 내 이야기개수 설정
		mainEtingCnt = (TextView) rootView.findViewById(R.id.main_eting_cnt);
		mainEtingCnt.setPaintFlags(mainEtingCnt.getPaintFlags()
				| Paint.FAKE_BOLD_TEXT_FLAG);
		Util.setPosition(mainEtingCnt, 10, 74);

		// 현재날짜
		mainToday = (TextView) rootView.findViewById(R.id.main_today);
		mainToday.setText(Util.getDate("."));
		Util.setPosition(mainToday, 10, 81);

		// 설정아이콘
		View settingBtn = rootView.findViewById(R.id.setting_btn);
		settingBtn.bringToFront();
		Util.setPosition(settingBtn, 84, 82);

		// ##########
		// 클릭이벤트 설정
		// ##########
		rootView.findViewById(R.id.main_inbox_cnt).setOnClickListener(this);
		rootView.findViewById(R.id.setting_btn).setOnClickListener(this);
		rootView.findViewById(R.id.notify_ufo).setOnClickListener(this);
		rootView.findViewById(R.id.main_acc_2).setOnClickListener(this);
		rootView.findViewById(R.drawable.eting_logo).setOnClickListener(this);
		rootView.findViewById(R.id.main_eting_cnt).setOnClickListener(this);
		rootView.findViewById(R.drawable.eting_logo).bringToFront();
		elv.setOnClickListener(this);

		// 프레임 레이아웃 앞으로 보내기
		// TODO 왜??
		fr.bringToFront();

		// 다시그리기
		// TODO 왜 다시그리지??
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
		 * 관리자 메세지 보여주기
		 */
		setNotifyUfo();
	}

	/**
	 * 클릭이벤트 실행
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.main_inbox_cnt:
			// 받은이야기 보는 로직
			this.checkInbox();
			break;

		case R.id.setting_btn:
			//설정화면으로 이동
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;

		case R.id.main_acc_2:
			// 메세지 저장
			getStoryFromServer();
			break;

		case R.id.notify_ufo:
			if (hasNotifyUfo) {
				startActivity(new Intent(getActivity(),
						NotifyUfoActivity.class)); // 팝업
			}
			break;

		case R.drawable.eting_logo:
			// 원을 누르면 쓰기페이지로 이동
			mPager = (ViewPager) getView().getParent();
			mPager.setCurrentItem(2);
			break;

		case R.id.main_eting_cnt:
			// 내 이팅개수를 누르면 목록으로
			mPager = (ViewPager) getView().getParent();
			mPager.setCurrentItem(0);
			break;
		}

	}

	/**
	 * 받은편지함 개수 확인하고 열지 안열지 체크
	 */
	public void checkInbox() {
		// 받은편지함 개수를 확인하고 한개 이상이면 실행
		int inboxCnt = inboxSvc.getInboxCnt();
		if (inboxCnt > 0) {
			startActivity(new Intent(getActivity(), ReadInboxActivity.class)); // 팝업
		}
	}

	/**
	 * 받은편지함 관련 정보 설정(개수, 우주선)
	 */
	public void setInboxCnt() {
		// 내 이야기개수 설정
		int storyCnt = storySvc.getStoryCnt();
		mainEtingCnt.setText(String.valueOf(storyCnt) + "  eting");
		mainEtingCnt.bringToFront();

		//받은편지함 개수 설정
		int inboxCnt = inboxSvc.getInboxCnt();
		mainInboxCnt.setText(String.valueOf(inboxCnt));

		// 받은편지함 처리
		if (inboxCnt > 0) {
			// 받은 이야기가 있으면 애니메이션 효과
			mainInboxCnt.setVisibility(View.GONE);
			mainInboxCnt.invalidate();
			new Handler() {
			}.postDelayed(new Runnable() {
				@Override
				public void run() {
					Animation ani = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),
							R.anim.main_ufo);
					mainInboxCnt.startAnimation(ani);
					mainInboxCnt.setVisibility(View.VISIBLE);
					mainInboxCnt.bringToFront();
				}
			}, 2000);
			
		} else {
			mainInboxCnt.clearAnimation();
			mainInboxCnt.setVisibility(View.GONE);
		}

	}
	
	/**
	 * 관리자 알람메세지를 보여주기
	 */
	public void setNotifyUfo(){
		
		String notifyUfoMsg = pref.getString("notify_ufo", "");
    	
    	//메세지 알림 우주선 보이기
		if (!Util.isEmpty(notifyUfoMsg)) {
			//공지메세지 있음
    		hasNotifyUfo = true;
    		
    		//행성이미지 숨기고
    		mainAcc1.setVisibility(View.GONE);
    		
    		//애니메이션 시작
			notifyUfo.setVisibility(View.GONE);
			notifyUfo.invalidate();
			
			new Handler() {
			}.postDelayed(new Runnable() {
				@Override
				public void run() {
					Animation ani = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.main_ufo);
					notifyUfo.startAnimation(ani);
					notifyUfo.setVisibility(View.VISIBLE);
					notifyUfo.bringToFront();
				}
			}, 1000);
			
    	}else{
    		//공지메세지 없음
    		hasNotifyUfo = false;
    		
    		//애니메이션 없애고
    		notifyUfo.clearAnimation();
    		notifyUfo.setVisibility(View.GONE);
    		
    		//행성이미지 살리고
    		mainAcc1.setVisibility(View.VISIBLE);
    	}
	}

	/**
	 * 뒤로가기 눌렀을때 이벤트 처리를 위한 로직
	 * 
	 * @param keyCode
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(getActivity())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setTitle(R.string.quit)
					.setMessage(R.string.really_quit)
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									System.exit(0);
								}
							}).setNegativeButton(R.string.no, null).show();
			
			return true;
		}
		return true;
	}
	
	/**
	 * 서버에서 이야기를 하나 받아온다!
	 */
	public void getStoryFromServer(){
		//10개가 넘지 않으면?
		int inboxCnt = inboxSvc.getInboxCnt();
		System.out.println(inboxCnt);
		if(inboxCnt == 0){
			new GetStoryTask().execute();
		}
	}
	
	/**
	 * 이야기를 받아온다.
	 * 
	 * @author lifenjoy51
	 */
	class GetStoryTask extends AsyncTask<Object, String, String> {

		/**
		 * 실제 실행되는 부분
		 */
		@Override
		protected String doInBackground(Object... params) {

			String phoneId = Installation.id(getActivity()
					.getApplicationContext()); // 기기 고유값
			String param = "phone_id=" + phoneId; // 서버에 전송할 파라미터 조립
			String urlStr = Const.serverContext + "/getRandomStory";

			String response = HttpUtil.doPost(urlStr, param); // Http전송
			System.out.println(response);
			return response;
		}
		
		/**
		 * 작업이 끝나면 자동으로 실행된다.
		 */
		@Override
		protected void onPostExecute(String result) {
			System.out.println(result);
			if(!"HttpUtil_Error".equals(result)){
				// 폰DB에 저장
				StoryService storyService = new StoryService(getActivity().getApplicationContext());
				storyService.saveToPhoneDB(result);
				setInboxCnt();
			}
		}

	}

}