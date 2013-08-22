package com.uauker.apps.transitorio.activities;

import roboguice.activity.RoboSplashActivity;
import android.content.Intent;
import android.os.Bundle;

import com.uauker.apps.transitorio.R;

public class SplashActivity extends RoboSplashActivity {

	@Override
	public void onCreate(Bundle bundle) {
		setContentView(R.layout.activity_splash);

		super.onCreate(bundle);
	}

	@Override
	protected void startNextActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}