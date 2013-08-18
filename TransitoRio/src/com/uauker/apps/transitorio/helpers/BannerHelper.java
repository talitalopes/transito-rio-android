package com.uauker.apps.transitorio.helpers;

import android.view.View;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.uauker.apps.transitorio.R;

public class BannerHelper {

	public static AdView setUpAdmob(View v) {
		AdView adView = (AdView) v.findViewById(R.id.adView);

		AdRequest request = new AdRequest();

		if (!ConfigHelper.isProduction) {
			request.addTestDevice("47764E7BF45BF0D60DD4D7185D18B260");
			request.addTestDevice("4D176AF8E94542B892FDDF9140EB3559");
			request.addTestDevice(AdRequest.TEST_EMULATOR);
		}

		if (adView != null) {
			adView.loadAd(request);
		}

		return adView;
	}
}