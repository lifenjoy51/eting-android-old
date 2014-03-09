package com.gif.eting.frg;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

public abstract class BaseFragment extends Fragment {
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	public void onGcm(Context context, String... strings) {

	}

}
