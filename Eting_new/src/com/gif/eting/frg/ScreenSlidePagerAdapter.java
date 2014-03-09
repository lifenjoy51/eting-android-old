package com.gif.eting.frg;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
	SparseArray<BaseFragment> registeredFragments = new SparseArray<BaseFragment>();

	public ScreenSlidePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * 페이지마다 Fragment를 반환함 #0 = 내 이야기 목록 #1 = 메인페이지 #2 = 내 이야기 쓰기
	 */
	@Override
	public BaseFragment getItem(int position) {
		switch (position) {

		case 0:
			return MyStoryListFragment.create(position);

		case 1:
			return MainFragment.create(position);

		case 2:
			return WriteMyStoryFragment.create(position);

		default:
			return MainFragment.create(position);

		}
	}

	/**
	 * 전체 페이지개수 반환
	 */
	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		BaseFragment fragment = (BaseFragment) super.instantiateItem(container,
				position);
		registeredFragments.put(position, fragment);
		return fragment;
	}

	public BaseFragment getFragment(int position) {
		return registeredFragments.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		registeredFragments.remove(position);
		super.destroyItem(container, position, object);
	}
}