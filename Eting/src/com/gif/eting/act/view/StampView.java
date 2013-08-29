package com.gif.eting.act.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.gif.eting.dto.StampDTO;

public class StampView extends TextView {
	private StampDTO stamp;

	public StampView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public StampView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StampView(Context context) {
		super(context);
	}

	public StampDTO getStamp() {
		return stamp;
	}

	public void setStamp(StampDTO stamp) {
		this.stamp = stamp;
	}

}
