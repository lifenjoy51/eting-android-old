package com.gif.eting.act;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.gif.eting.R;

public class MainViewPager extends SherlockFragmentActivity {
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
        mPager.setCurrentItem(1);	//초기페이지설정
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
                ////supportInvalidateOptionsMenu();
            }
        });
    }
    
    public void setPage(int position){
    	mPager.setCurrentItem(position);
    }


    /**
     * A simple pager adapter
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			
			case 0:
				MyStoryList myStoryList = MyStoryList.create(position);
				return myStoryList;

			case 1:
				Main main = Main.create();
				main.setViewPager(mPager);
				return main;

			case 2:
				WriteMyStory writeMyStory = WriteMyStory.create(position);
				writeMyStory.setViewPager(mPager);
				return writeMyStory;
				
			default:
				return Main.create();
				
			}
		}

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}