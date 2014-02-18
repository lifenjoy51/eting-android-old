package com.nexters.eting.frg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Service;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nexters.eting.MainViewPagerActivity;
import com.nexters.eting.R;
import com.nexters.eting.etc.AsyncTaskCompleteListener;
import com.nexters.eting.task.SendStoryTask;
import com.nexters.eting.view.UfoWritePageView;

/**
 * 내 이야기 쓰기
 * 
 * @author lifenjoy51
 * 
 */
public class WriteMyStoryFragment extends BaseFragment implements
		OnClickListener {

	private ViewPager mPager = MainViewPagerActivity.mPager;
	private TextView tv;
	private Context context;
	private ViewGroup rootView;
	private ViewGroup writeContentArea;
	private UfoWritePageView ufo;
	private EditText et;
	
	/*
	 * sendingFlag
	 * 전송중이면 true
	 * 전송중이 아니면 false
	 */
	private boolean sendingFlag = false;
	
	/*
	 * sendingCount
	 * 애니메이션이 끝나면 +1
	 * 이야기전송이 성공적으로 끝나면 +1
	 */
	private int sendingCount = 0;

	/**
	 * Factory method for this fragment class. Constructs a new fragment for the
	 * given page number.
	 */
	public static WriteMyStoryFragment create(int pageNumber) {
		WriteMyStoryFragment fragment = new WriteMyStoryFragment();
		return fragment;
	}

	public WriteMyStoryFragment() {
	}

	public void setViewPager(ViewPager mPager) {
		this.mPager = mPager;
		this.mPager = (ViewPager) getActivity().findViewById(R.layout.activity_mainviewpager);
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writestory,
				container, false);
		
		rootView.bringToFront();

		context = getActivity();
		
		writeContentArea = (ViewGroup) rootView.findViewById(R.id.write_content_area);
		
		//폰트설정
		et = (EditText) rootView.findViewById(R.id.story_content);

		tv = (TextView) rootView.findViewById(R.id.write_story_dt);
		
		// 상단에 오늘날짜 설정
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd",
				Locale.KOREA);
		Date date = new Date();
		String today = formatter.format(date);
		tv.setPaintFlags(tv.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		tv.setText(today);

		// 클릭이벤트 설정
		rootView.findViewById(R.id.send_story_btn).setOnClickListener(this);
		
		et.requestFocus();
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(!hasFocus){
			    	hideIme();
			    }else{
			    	showIme();
			    }
			   }
			});

		return rootView;
	}

	/**
	 * 클릭이벤트
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.send_story_btn:
			// 입력한 문자 체크로직
			et = (EditText) getView().findViewById(R.id.story_content);
			String content = et.getText().toString(); // 이야기 내용

			// 입력값이 없으면 처리중단
			if (content == null || "".equals(content)) {
				// 입력해달라고 토스트 알람
				Toast.makeText(getActivity(), R.string.write_story_input,
						Toast.LENGTH_SHORT).show();
				return;
			}

			String firstChar = content.substring(0, 1); // 맨 처음 문자
			boolean chk = false;
			// 동일한 문자로 연속되는지 확인
			for (int i = 0; i < content.length(); i++) {
				if (!firstChar.equals(content.substring(i, i + 1))) {
					chk = true;
					break;
				}
			}

			// 문제가 없으면 저장시작
			if (chk) {
				if(!sendingFlag){
					sendAndSaveStory();
				}
			} else {
				// 문제있다고 알람
				Toast.makeText(getActivity(), R.string.write_story_validate,
						Toast.LENGTH_SHORT).show();
			}
			break;
		}

	}

	/**
	 * 작성한 이야기를 서버에 전송하고 자신의 폰에 저장한다.
	 */
	private void sendAndSaveStory() {
		//전송이 시작되면 플래그값을 바꾼다.
		sendingFlag = true;
		
		et = (EditText) getView().findViewById(R.id.story_content);
		final String content = et.getText().toString(); // 이야기 내용
		
		hideIme();		
		
		//페이드아웃 애니메이션
		Animation sa = new AlphaAnimation(1.0f, 0.0f);

		sa.setDuration(700);
		sa.setRepeatCount(0);
		sa.setFillAfter(true);
		sa.setFillEnabled(true);
		sa.setAnimationListener(new AnimationControl(context));
		
		/**
		 * 이야기 전송을 시작하며 동시와 페이드아웃 애니메이션도 시작한다.
		 */
		writeContentArea.startAnimation(sa);

		/**
		 * 서버로 이야기 전송
		 * 
		 * SendStoryTask생성할때 파라미터는 SendStoryTask가 수행되고 나서 실행될 콜백이다. execute의
		 * 파라미터가 실제 넘겨줄 자료들. parameter[0] = content. 이야기 내용 parameter[1] =
		 * getSherlockActivity(). Context.
		 */
		new SendStoryTask(new AfterSendStoryTask()).execute(content, getActivity());
	}
	
	public void hideIme(){
		et = (EditText) getView().findViewById(R.id.story_content);
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}
	
	public void showIme(){
		et = (EditText) getView().findViewById(R.id.story_content);
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, 0);
	}
	
	

	@Override
	public void onStop() {
		super.onStop();
		hideIme();
	}

	public void onDestroy() {
		//Log.i("test", "onDstory()");
		super.onDestroy();
	}

	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 */
	private class AfterSendStoryTask implements AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {
			//Log.i("onTaskComplete", result);
			
			/**
			 * 에러처리
			 */
			if("HttpUtil_Error".equals(result)){
				Toast toast = Toast.makeText(getActivity(), R.string.error_on_transfer,
						Toast.LENGTH_LONG);
				toast.show();
				
				//에러났을 때  초기화
				resetSendingStory();
				
			} else if("UnknownHostException".equals(result)){
				Toast toast = Toast.makeText(getActivity(),R.string.cannot_connect_to_internet,
						Toast.LENGTH_LONG);
				toast.show();
				
				//에러났을 때 초기화
				resetSendingStory();
				
			} else if ("Success".equals(result)){
				//정상으로 전송되었을 때.	
				sendingCount++;
				completeSendingStory();
				
			}else{
				//???
			}
		}
	}

	/**
	 * 뒤로가기 눌렀을때 이벤트 처리를 위한 로직
	 * 
	 * @param keyCode
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//Log.i("onKeyDown WriteMyStory", String.valueOf(keyCode));
		// 메인 화면으로 이동
		if (mPager != null) {
			mPager.setCurrentItem(1);
		}
		return true;
	}
	

	/**
	 * 페이드아웃 애니메이션 상태를 제어
	 * 
	 * @author lifenjoy51
	 *
	 */
	private class AnimationControl implements AnimationListener {
		Context context;
		
		public AnimationControl(Context context){
			this.context = context;
		}

		@Override
		public void onAnimationEnd(Animation an) {
			
			/**
			 * 페이드아웃이 끝나면 ufo를 나오게 한다.
			 */
			ufo = new UfoWritePageView(context, new AfterAnimationTask());
			rootView.addView(ufo); // 움직이는 UFO 등록
		}

		/**
		 * 애니메이션이 반복될때마다 실행
		 */
		@Override
		public void onAnimationRepeat(Animation an) {
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			//숨기기
			writeContentArea.setVisibility(View.GONE);
			//터치불가처리
			writeContentArea.setFocusable(false);

		}
	}
	
	/**
	 * UFO애니메이션이 끝나고 실행된다.
	 */
	private class AfterAnimationTask implements
			AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {			
			sendingCount++;
			completeSendingStory();
		}
	}
	
	/**
	 * 전송완료 로직
	 */
	private void completeSendingStory(){
		
		//System.out.println("completeSendingStory");
		
		/**
		 * 애니메이션이 모두 끝나고 && 전송이 제대로 이루어지면 진행, 아니면 리턴
		 */
		if(	sendingCount != 2){			
			return;
		}
		
		//resetSendingStory
		resetSendingStory();

		/**
		 * 전송완료 메세지
		 */
		if (getActivity() != null) {
			Toast toast = Toast.makeText(getActivity(),
					R.string.send_completed, Toast.LENGTH_SHORT);
			toast.show();
		}

		/**
		 * 쓰기화면 초기화
		 */
		et = (EditText) getView().findViewById(R.id.story_content);
		et.setText("");

		/**
		 * 메인화면으로 이동
		 */
		if (mPager != null) {
			mPager.setCurrentItem(1);
		}
		
	}
	
	/**
	 * 전송중 에러났을때 초기화로직
	 */
	private void resetSendingStory(){
		//전송중 플래그 초기화
		sendingFlag = false;
		
		//전송상태 확인 카운트 초기화
		sendingCount = 0;
		
		/**
		 * 쓰기 화면 원상복구
		 */
		Animation sa = new AlphaAnimation(0.0f, 1.0f);
		sa.setDuration(0);
		sa.setRepeatCount(0);
		sa.setFillAfter(true);
		sa.setFillEnabled(true);
		writeContentArea.startAnimation(sa);
		writeContentArea.setVisibility(View.VISIBLE);
		writeContentArea.setFocusableInTouchMode(true);
		
		/**
		 * UFO제거하고
		 */
		rootView.removeView(ufo);
		
	}

}
