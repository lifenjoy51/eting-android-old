package com.gif.eting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {
	private final int COUNT = 4; // 뷰 4개 등록 (main, write, read, setting)
	private ViewPager mPager; // 뷰 페이저

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 현진추가 */
		setContentView(R.layout.viewpager);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(new BkPagerAdapter(getApplicationContext()));
	}

//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.write_et_btn:
//			startActivity(new Intent(this, WriteStoryActivity.class));
//			break;
//		case R.id.read_et_btn:
//			startActivity(new Intent(this, ReadMyStoryActivity.class));
//			break;
//		case R.id.setting_btn:
//			startActivity(new Intent(this, SettingActivity.class));
//			break;
//		case R.id.inbox_btn:
//			startActivity(new Intent(this, InboxStoryPopupActivity.class));
//			break;
//		}

	public class BkPagerAdapter extends PagerAdapter {
		private LayoutInflater mInflater;

		public BkPagerAdapter(Context con) {
			super();
			mInflater = LayoutInflater.from(con);
		}

		@Override
		public int getCount() {
			return COUNT;
		}

		// 뷰페이저에서 사용할 뷰객체 생성/등록
		@Override
		public Object instantiateItem(View pager, int position) {
			View v = null;
			if (position == 0) {
				v = mInflater.inflate(R.layout.main, null);
			} else if (position == 1) {
				v = mInflater.inflate(R.layout.write_story, null);
			} else if (position == 2) {
				v = mInflater.inflate(R.layout.read_story, null);
			} else {
				v = mInflater.inflate(R.layout.setting, null);
			}

			((ViewPager) pager).addView(v, 0);
			return v;
		}

		// 뷰 객체 삭제
		@Override
		public void destroyItem(View pager, int position, Object view) {
			((ViewPager) pager).removeView((View) view);
		}

		// instantiateItem메소드에서 생성한 객체를 이용할 것인지
		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

}
