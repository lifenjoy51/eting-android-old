package com.gif.eting.act.frg;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.gif.eting.act.view.UfoWritePageView;
import com.gif.eting.svc.task.SendStoryTask;
import com.gif.eting.util.AsyncTaskCompleteListener;
import com.gif.eting_dev.R;

/**
 * 내 이야기 쓰기
 * 
 * @author lifenjoy51
 * 
 */
public class WriteMyStoryFragment extends SherlockFragment implements
		OnClickListener {
	private ViewPager mPager;
	private TextView send_textview;
	private TextView tv;
	private Typeface nanum;
	private Context context;
	private ViewGroup rootView;
	private Handler handle = new Handler();
	private Runnable mMyTask;
	private ViewGroup writeContentArea;
	private UfoWritePageView ufo;

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
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout containing a title and body text.
		rootView = (ViewGroup) inflater.inflate(R.layout.write_story,
				container, false);
		nanum = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/NanumGothic.ttf");

		context = getActivity();
		
		writeContentArea = (ViewGroup) rootView.findViewById(R.id.write_content_area);
		
		//폰트설정
		EditText et = (EditText) rootView.findViewById(R.id.story_content);
		et.setTypeface(nanum);

		tv = (TextView) rootView.findViewById(R.id.write_story_dt);
		tv.setTypeface(nanum);
		
		/*send_textview = (TextView) rootView.findViewById(R.id.send_textview);
		send_textview.setTypeface(nanum);
		send_textview.setPaintFlags(send_textview.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);*/

		// 상단에 오늘날짜 설정
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd",
				Locale.KOREA);
		Date date = new Date();
		String today = formatter.format(date);
		tv.setPaintFlags(tv.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
		tv.setText(today);

		// 클릭이벤트 설정
		rootView.findViewById(R.id.send_story_btn).setOnClickListener(this);

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
			EditText et = (EditText) getView().findViewById(R.id.story_content);
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
				sendAndSaveStory();
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
		EditText et = (EditText) getView().findViewById(R.id.story_content);
		final String content = et.getText().toString(); // 이야기 내용
		
		//페이드아웃 애니메이션
		Animation sa = new AlphaAnimation(1.0f, 0.0f);

		sa.setDuration(500);
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
		new SendStoryTask(new AfterSendStoryTask()).execute(content, getSherlockActivity());
		onDestroy();
	}

	public void onDestroy() {
		Log.i("test", "onDstory()");
		handle.removeCallbacks(mMyTask);
		super.onDestroy();
	}

	/**
	 * SendStoryTask수행 후 실행되는 콜백
	 */
	private class AfterSendStoryTask implements
			AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {
			Log.i("onTaskComplete", result);
			
			/**
			 * 여기엔 뭐하지??
			 */
		}
	}

	/**
	 * 뒤로가기 눌렀을때 이벤트 처리를 위한 로직
	 * 
	 * @param keyCode
	 * @return
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("onKeyDown WriteMyStory", String.valueOf(keyCode));
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

		}
	}
	
	/**
	 * UFO애니메이션이 끝나고 실행된다.
	 */
	private class AfterAnimationTask implements
			AsyncTaskCompleteListener<String> {

		@Override
		public void onTaskComplete(String result) {

			/**
			 * 전송완료 메세지
			 */
			Toast toast = Toast.makeText(getActivity(), "이야기가 전송되었습니다",
					Toast.LENGTH_SHORT);
			toast.show();
			
			/**
			 * 쓰기 화면 원상복구
			 */
			Animation sa = new AlphaAnimation(0.0f, 1.0f);
			sa.setDuration(0);
			sa.setRepeatCount(0);
			sa.setFillAfter(true);
			sa.setFillEnabled(true);
			writeContentArea.startAnimation(sa);
		
			System.out.println("animation end!!!");
			/**
			 * UFO제거하고
			 */
			rootView.removeView(ufo);

			/**
			 * 쓰기화면 초기화
			 */
			EditText et = (EditText) getView().findViewById(R.id.story_content);
			et.setText("");

			/**
			 * 메인화면으로 이동
			 */
			if (mPager != null) {
				mPager.setCurrentItem(1);
			}
		}
	}

}
