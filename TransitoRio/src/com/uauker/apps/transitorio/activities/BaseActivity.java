package com.uauker.apps.transitorio.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.menu.MenuListFragment;

public class BaseActivity extends SlidingFragmentActivity {

	protected ListFragment mFrag;

	protected SlidingMenu sm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setUpMenu(savedInstanceState);
	}

	private void setUpMenu(Bundle savedInstanceState) {
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);

		// customize the SlidingMenu
		sm = getSlidingMenu();

		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager()
					.beginTransaction();
			mFrag = new MenuListFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment) this.getSupportFragmentManager()
					.findFragmentById(R.id.menu_frame);
		}

		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		// sm.setSelected(true);
		// sm.setSelectorEnabled(true);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
}
