package com.uauker.apps.transitorio.activities;

import roboguice.activity.RoboSplashActivity;
import android.content.Intent;
import android.os.Bundle;

import com.bugsense.trace.BugSenseHandler;
import com.uauker.apps.transitorio.R;

public class SplashActivity extends RoboSplashActivity {

	@Override
	public void onCreate(Bundle bundle) {
		BugSenseHandler.initAndStartSession(this, "5e8d9705");
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