package com.gif.eting.act;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gif.eting.act.frg.MainFragment;
import com.gif.eting.act.frg.MyStoryListFragment;
import com.gif.eting.act.frg.WriteMyStoryFragment;
import com.gif.eting.act.view.Cloud1View;
import com.gif.eting.act.view.Cloud2View;
import com.gif.eting.act.view.Cloud3View;
import com.gif.eting.act.view.Cloud4View;
import com.gif.eting_dev.R;

/**
 * 메인 뷰페이져
 * 
 * @author lifenjoy51
 *
 */
public class MainViewPagerActivity extends SherlockFragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 3;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager);
        

		FrameLayout fr = (FrameLayout) findViewById(R.id.mainviewpager_frame);
		fr.addView(new Cloud1View(this)); // 구름애니메이션
		fr.addView(new Cloud2View(this)); // 구름애니메이션
		fr.addView(new Cloud3View(this)); // 구름애니메이션
		fr.addView(new Cloud4View(this)); // 구름애니메이션

        fr.setAnimationCacheEnabled(true);
        fr.setDrawingCacheEnabled(true);
        
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.bringToFront();
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1);		//초기페이지설정
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	//페이지 변경됐을때 이벤트처리
    			Fragment fragment = ((ScreenSlidePagerAdapter) mPagerAdapter)
    					.getFragment(position);

    			/**
    			 * 프래그먼트에 따른 조건분기
    			 */
    			if (fragment instanceof MainFragment) {
    				((MainFragment) fragment).setInboxCnt();
    			}
            	
            }
        });
		
		/**
		 * 시간에따라 배경을 바꾼다 
		 */
		SimpleDateFormat sdf = new SimpleDateFormat("HH", Locale.KOREA);
		String thisHourStr = sdf.format(new Date());
		int thisHour = Integer.parseInt(thisHourStr);
		Log.i("currunt hour", thisHourStr);
		
		if(thisHour<1 ){
			fr.setBackgroundResource(R.drawable.bg_1);
		}else if(thisHour<5 ){
			fr.setBackgroundResource(R.drawable.bg_2);
		}else if(thisHour<9 ){
			fr.setBackgroundResource(R.drawable.bg_3);
		}else if(thisHour<13 ){
			fr.setBackgroundResource(R.drawable.bg_4);
		}else if(thisHour<17 ){
			fr.setBackgroundResource(R.drawable.bg_5);
		}else if(thisHour<21 ){
			fr.setBackgroundResource(R.drawable.bg_6);
		}else{
			fr.setBackgroundResource(R.drawable.bg_1);
		}
        
		/**
		 * GCM으로 받은경우 페이지이동
		 */
        Intent intent = getIntent();
		boolean isGcm = intent.getBooleanExtra("GCM", false);	//GCM여부
		if(isGcm){
			String storyId = intent.getStringExtra("storyId");
			//내목록화면으로 이동
			setPage(0);
			Fragment fragment = ((ScreenSlidePagerAdapter) mPagerAdapter).getItem(0);
			if(fragment != null){
				if (fragment instanceof MyStoryListFragment) {
					 ((MyStoryListFragment)fragment).readMyStoryPopup(this, storyId);
				 }
			}
			
			
		}
    }
    
    /**
     * 페이지 변경
     * 
     * @param position
     */
    public void setPage(int position){
    	mPager.setCurrentItem(position);
    }


    /**
     * A simple pager adapter
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    	
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
		}

        /**
         * 페이지마다 Fragment를 반환함
         * 0 = 내 이야기 목록
         * 1 = 메인페이지
         * 2 = 내 이야기 쓰기
         */
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			
			case 0:
				MyStoryListFragment myStoryList = MyStoryListFragment.create(position);
				myStoryList.setViewPager(mPager);
				return myStoryList;

			case 1:
				MainFragment main = MainFragment.create();
				main.setViewPager(mPager);
				return main;

			case 2:
				WriteMyStoryFragment writeMyStory = WriteMyStoryFragment.create(position);
				writeMyStory.setViewPager(mPager);
				return writeMyStory;
				
			default:
				return MainFragment.create();
				
			}
		}

		/**
		 * 전체 페이지개수 반환
		 */
        @Override
        public int getCount() {
            return NUM_PAGES;
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }
        
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
        
        public Fragment getFragment(int position) {
            return registeredFragments.get(position);
        }

	}

	/**
	 * 뒤로가기 버튼을 눌렀을 때 이벤트 처리를 위한 로직
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.i("onKeyDown Main", String.valueOf(keyCode));

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			int curItem = mPager.getCurrentItem();
			Fragment fragment = ((ScreenSlidePagerAdapter) mPagerAdapter)
					.getFragment(curItem);

			/**
			 * 프래그먼트에 따른 조건분기
			 */
			if (fragment instanceof WriteMyStoryFragment) {
				return ((WriteMyStoryFragment) fragment).onKeyDown(keyCode, event);
			} else if (fragment instanceof MyStoryListFragment) {
				return ((MyStoryListFragment) fragment).onKeyDown(keyCode, event);
			} else if (fragment instanceof MainFragment) {
				return ((MainFragment) fragment).onKeyDown(keyCode, event);
			} else {
				return super.onKeyDown(keyCode, event);
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}