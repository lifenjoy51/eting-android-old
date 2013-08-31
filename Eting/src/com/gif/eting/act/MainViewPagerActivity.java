package com.gif.eting.act;

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

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gif.eting.R;
import com.gif.eting.act.frg.MainFragment;
import com.gif.eting.act.frg.MyStoryListFragment;
import com.gif.eting.act.frg.WriteMyStoryFragment;

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
        
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
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