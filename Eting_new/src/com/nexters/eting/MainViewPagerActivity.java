package com.nexters.eting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.nexters.eting.etc.CachedBitmap;
import com.nexters.eting.etc.Util;
import com.nexters.eting.frg.BaseFragment;
import com.nexters.eting.frg.ScreenSlidePagerAdapter;
import com.nexters.eting.view.CloudView;

/**
 * 메인 뷰페이져
 * 
 * @author lifenjoy51
 * 
 */
public class MainViewPagerActivity extends FragmentActivity {

	static public ViewPager mPager;
	private ScreenSlidePagerAdapter mPagerAdapter;
	
	private static FrameLayout fr;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		//이건 왜 추가했었지? 이유가 있었는데...
		try {
			Class.forName("android.os.AsyncTask");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mainviewpager);

		//Util클래스가 GC당할 경우를 대비하여 액티비티를 생성할때 다시 초기화시킨다
		Util.init(getApplicationContext());

		fr = (FrameLayout) findViewById(R.id.mainviewpager_frame);
		
		//구름 애니메이션 추가
		fr.addView(new CloudView(getApplicationContext(), R.drawable.main_cloud_01, -20, 8, 178, 40000));
		fr.addView(new CloudView(getApplicationContext(), R.drawable.main_cloud_02, 41, 28, 204, 38000));
		fr.addView(new CloudView(getApplicationContext(), R.drawable.main_cloud_03, -17, 49, 178, 38000));
		fr.addView(new CloudView(getApplicationContext(), R.drawable.main_cloud_04, 60, 70, 229, 28000));
		
		// Instantiate a ViewPager and a PagerAdapter.
		mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.bringToFront();
		mPager.setAdapter(mPagerAdapter);
		mPager.setCurrentItem(1); // 초기페이지설정

		/**
		 * 시간에따라 배경을 바꾼다 #시간에 맞게 이미지를 캐슁해놓는다.
		 */
		fr.setBackgroundDrawable(CachedBitmap.getMainBg(getApplicationContext()));		

		/**
		 * GCM으로 받은경우 페이지이동
		 */
		Intent intent = getIntent();
		boolean isGcm = intent.getBooleanExtra("GCM", false); // GCM여부
		if (isGcm) {
			//GCM에서 받아온 이야기 아이디
			String storyId = intent.getStringExtra("storyId");
			Log.i("gcm storyId", "Main Activity = " + storyId);
			// 내목록화면으로 이동
			mPager.setCurrentItem(0);
			// onGcm 메쏘드 실행
			mPagerAdapter.getItem(0).onGcm(storyId);
		}

	}
	
	/**
	 * 뒤로가기 버튼을 눌렀을 때 이벤트 처리를 위한 로직
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Log.i("onKeyDown Main", String.valueOf(keyCode));

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int curItem = mPager.getCurrentItem();
			BaseFragment fragment = mPagerAdapter.getFragment(curItem);
			return fragment.onKeyDown(keyCode,event);
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

}