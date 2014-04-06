package com.gif.eting.etc;

import android.content.Context;
import android.widget.LinearLayout;

import com.gif.eting.BaseActivity;
import com.gif.eting.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AD {

	public static void ad(Context context) {

		/** The view to show the ad. */
		AdView adView;

		// Create an ad.
		adView = new AdView(context);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(Const.AD_UNIT_ID);

		// Add the AdView to the view hierarchy. The view will have no size
		// until the ad is loaded.
		LinearLayout layout = (LinearLayout) ((BaseActivity) context)
				.findViewById(R.id.adLayout);
		if (layout == null) {
			return;
		}
		layout.addView(adView);

		// Create an ad request. Check logcat output for the hashed device ID to
		// get test ads on a physical device.
		AdRequest adRequest = new AdRequest.Builder()
				.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
				.addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE").build();

		// Start loading the ad in the background.
		adView.loadAd(adRequest);

	}
}
