package com.nexters.eting.view;

import android.content.Context;
import android.widget.ImageView;

public class EmoticonView extends ImageView {

	private String emoticonId;

	public EmoticonView(Context context) {
		super(context);
	}

	public String getEmoticonId() {
		return emoticonId;
	}

	public void setEmoticonId(String emoticonId) {
		this.emoticonId = emoticonId;
	}

}
