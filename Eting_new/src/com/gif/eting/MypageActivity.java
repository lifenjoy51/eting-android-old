package com.gif.eting;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.gif.eting.etc.AD;

public class MypageActivity extends BaseActivity implements OnClickListener,
		OnSeekBarChangeListener {

	SeekBar mSeekBar;

	// seekbar values
	int mMinValue = 10;
	int mMaxValue = 40;
	int mCurrentValue = 25;

	// current value
	TextView mValueText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 레이아웃 설정
		setContentView(R.layout.activity_mypage);

		// cuurent text
		mValueText = (TextView) findViewById(R.id.seek_current);

		// Put minimum and maximum
		((TextView) findViewById(R.id.seek_min)).setText(Integer
				.toString(mMinValue));
		((TextView) findViewById(R.id.seek_max)).setText(Integer
				.toString(mMaxValue));

		mSeekBar = (SeekBar) findViewById(R.id.age_seek_bar);
		mSeekBar.setMax(mMaxValue - mMinValue);
		mSeekBar.setProgress(mCurrentValue - mMinValue);
		mSeekBar.setOnSeekBarChangeListener(this);

		// ad
		AD.ad(this);
	}

	/**
	 * 클릭 이벤트
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.setting_save_pw_btn) {
		} else if (view.getId() == R.id.password_check) {

		}
	}

	@Override
	public void onProgressChanged(SeekBar seek, int value, boolean fromTouch) {
		mCurrentValue = value + mMinValue;
		mValueText.setText(Integer.toString(mCurrentValue));
	}

	@Override
	public void onStartTrackingTouch(SeekBar bar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar bar) {

	}

}
