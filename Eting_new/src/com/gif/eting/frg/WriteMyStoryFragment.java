package com.gif.eting.frg;

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

import com.gif.eting.MainViewPagerActivity;
import com.gif.eting.R;
import com.gif.eting.etc.AsyncTaskCompleteListener;
import com.gif.eting.etc.Util;
import com.gif.eting.task.SendStoryTask;
import com.gif.eting.view.UfoWritePageView;

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
	 * isSending 전송중이면 true 전송중이 아니면 false
	 */
	private boolean isSending = false;

	/*
	 * sendingCount 애니메이션이 끝나면 +1 이야기전송이 성공적으로 끝나면 +1 최종적으로 2가 되어야 정상적으로 전송을 시작할
	 * 상태.
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 컨텍스트 설정하고
		context = getActivity();

		// 레이아웃 설정
		rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writestory,
				container, false);
		rootView.bringToFront();

		// 뷰객체들 연결
		writeContentArea = (ViewGroup) rootView
				.findViewById(R.id.write_content_area);
		et = (EditText) rootView.findViewById(R.id.story_content);
		tv = (TextView) rootView.findViewById(R.id.write_story_dt);

		// 상단에 오늘날짜 설정
		String today = Util.getDate(".");
		tv.setPaintFlags(tv.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		tv.setText(today);

		// 클릭이벤트 설정
		rootView.findViewById(R.id.send_story_btn).setOnClickListener(this);

		// 키보드 자동 on/off
		setAutoKeyboardOnOff();

		return rootView;
	}

	/**
	 * 입력창으로 오렴 자동으로 키보드를 보이고, 벗어나면 키보드를 숨긴다.
	 */
	private void setAutoKeyboardOnOff() {
		et.requestFocus();
		et.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					hideIme();
				} else {
					showIme();
				}
			}
		});
	}

	/**
	 * 클릭이벤트
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.send_story_btn:
			// 유효성 검사 후
			if (isValidOnSaveStory()) {
				// 이야기 저장
				sendAndSaveStory();
			}
			break;
		}

	}

	/**
	 * 유효성 검사.
	 *
	 * @return
	 */
	private boolean isValidOnSaveStory() {
		// 입력한 문자 체크로직
		String content = et.getText().toString(); // 이야기 내용

		// 입력값이 없으면 처리중단
		if (Util.isEmpty(content)) {
			// 입력해달라고 토스트 알람
			Toast.makeText(getActivity(), R.string.write_story_input,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		// 맨 처음 문자
		String firstChar = content.substring(0, 1);
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
			// 이야기 전송중이 아니여야지만 전송 시작
			if (!isSending) {
				return true;
			}
		} else {
			// 문제있다고 알람
			Toast.makeText(getActivity(), R.string.write_story_validate,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return false;

	}

	/**
	 * 작성한 이야기를 서버에 전송하고 자신의 폰에 저장한다.
	 */
	private void sendAndSaveStory() {

		// 전송이 시작되면 플래그값을 바꾼다.
		isSending = true;

		// 이야기 내용
		final String content = et.getText().toString();

		// 키보드를 숨기고
		hideIme();

		// 페이드아웃 애니메이션
		Animation sa = new AlphaAnimation(1.0f, 0.0f);
		sa.setDuration(700);
		sa.setFillAfter(true);
		sa.setFillEnabled(true);
		sa.setAnimationListener(new AnimationControl(
				context));

		// 이야기 전송을 시작하며 동시와 페이드아웃 애니메이션도 시작한다.
		writeContentArea.startAnimation(sa);

		/**
		 * 서버로 이야기 전송
		 *
		 * SendStoryTask생성할때 파라미터는 SendStoryTask가 수행되고
		 * 나서 실행될 콜백이다. execute의 파라미터가 실제 넘겨줄 자료들.
		 *
		 * @parameter[0] = content. 이야기 내용
		 * @parameter[1] = getActivity(). Context.
		 */
		new SendStoryTask(new AfterSendStoryTask())
				.execute(content, getActivity());

	}

	/**
	 * 키보드를 숨긴다.
	 */
	public void hideIme() {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	/**
	 * 키보드를 다시 보인다.
	 */
	public void showIme() {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.showSoftInput(et, 0);
	}

	@Override
	public void onStop() {
		super.onStop();
		hideIme();
	}

	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 */
	private class AfterSendStoryTask implements
			AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {
			// 에러처리
			if ("HttpUtil_Error".equals(result)) {
				Toast.makeText(getActivity(), R.string.error_on_transfer,
						Toast.LENGTH_LONG).show();

				// 에러났을 때 초기화
				resetSendingStory();

			}else if ("Error".equals(result)) {
				Toast.makeText(getActivity(), R.string.error_on_transfer,
						Toast.LENGTH_LONG).show();

				// 에러났을 때 초기화
				resetSendingStory();

			} else if ("UnknownHostException".equals(result)) {
				Toast.makeText(getActivity(),
						R.string.cannot_connect_to_internet, Toast.LENGTH_LONG)
						.show();

				// 에러났을 때 초기화
				resetSendingStory();

			} else if ("Success".equals(result)) {
				// 정상으로 전송되었을 때.
				sendingCount++;
				// 완료처리
				completeSendingStory();

			} else {
				// ???
			}
		}
	}

	/**
	 * 뒤로가기 눌렀을때 이벤트 처리를 위한 로직
	 *
	 * @param keyCode
	 * @return
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 메인 화면으로 이동
		mPager = (ViewPager) getView().getParent();
		mPager.setCurrentItem(1);
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

		public AnimationControl(Context context) {
			this.context = context;
		}

		@Override
		public void onAnimationEnd(Animation an) {
			// 페이드아웃이 끝나면 ufo를 나오게 한다.
			ufo = new UfoWritePageView(context, new AfterAnimationTask());
			rootView.addView(ufo); // 움직이는 UFO 등록
		}

		@Override
		public void onAnimationRepeat(Animation an) {
		}

		@Override
		public void onAnimationStart(Animation arg0) {
			// 애니메이션이 시작하면 입력화면을 숨긴다.
			writeContentArea.setVisibility(View.GONE);
			// 입력화면 터치를 막는다.
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
			// 전송완료 로직
			completeSendingStory();
		}
	}

	/**
	 * 전송완료 로직
	 */
	private void completeSendingStory() {

		// 애니메이션이 모두 끝나고 && 전송이 제대로 이루어지면 진행, 아니면 리턴
		if (sendingCount != 2) {
			return;
		}

		// 입력화면 초기화
		resetSendingStory();

		// 전송완료 메세지
		if (getActivity() != null) {
			Toast.makeText(getActivity(), R.string.send_completed,
					Toast.LENGTH_SHORT).show();
		}

		// 쓰기화면 초기화
		et.setText("");

		// 메인화면으로 이동
		mPager = (ViewPager) getView().getParent();
		mPager.setCurrentItem(1);

	}

	/**
	 * 전송중 에러났을때 초기화로직
	 */
	private void resetSendingStory() {
		// 전송중 플래그 초기화
		isSending = false;

		// 전송상태 확인 카운트 초기화
		sendingCount = 0;

		// 쓰기 화면 원상복구
		Animation sa = new AlphaAnimation(0.0f, 1.0f);
		sa.setDuration(0);
		sa.setRepeatCount(0);
		sa.setFillAfter(true);
		sa.setFillEnabled(true);
		writeContentArea.startAnimation(sa);
		writeContentArea.setVisibility(View.VISIBLE);
		writeContentArea.setFocusableInTouchMode(true);

		// UFO제거하고
		rootView.removeView(ufo);

	}

}
