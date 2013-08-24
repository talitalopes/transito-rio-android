package com.uauker.apps.transitorio.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.activities.SettingsActivity;
import com.uauker.apps.transitorio.adapters.TelephoneAdapter;
import com.uauker.apps.transitorio.helpers.BannerHelper;
import com.uauker.apps.transitorio.models.others.Telephone;
import com.uauker.apps.transitorio.services.TelephoneService;

public class TelephoneFragment extends SherlockFragment implements
		OnOpenListener, OnCloseListener {

	ImageView shadowView;

	ListView telephoneListView;

	ViewStub loadingViewStub;
	ViewStub internetFailureViewStub;
	View emptyView;

	List<Telephone> telephones = new ArrayList<Telephone>();

	private TelephoneService telephoneService;

	private Activity ownerActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		ab.setTitle(R.string.telefones_uteis);

		setHasOptionsMenu(true);

		View contentView = inflater.inflate(R.layout.fragment_telephone,
				container, false);

		this.shadowView = (ImageView) contentView
				.findViewById(R.id.telephone_shadow);

		this.loadingViewStub = (ViewStub) contentView
				.findViewById(R.id.telephone_loading);
		this.loadingViewStub.setLayoutResource(R.layout.loading);

		this.telephoneListView = (ListView) contentView
				.findViewById(R.id.telephone_listview);

		this.emptyView = contentView
				.findViewById(R.id.telephone_list_empty_message);

		this.telephoneService = new TelephoneService(ownerActivity);
		this.telephones = this.telephoneService.getTelephones();
		setupListView();

		if (contentView != null) {
			BannerHelper.setUpAdmob(contentView);
		}

		return contentView;
	}

	private void setupListView() {
		TelephoneAdapter telephoneAdapter = new TelephoneAdapter(ownerActivity,
				R.layout.adapter_telephone, telephones);
		telephoneListView.setAdapter(telephoneAdapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_config, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.menu_settings:
			Intent intent = new Intent(ownerActivity, SettingsActivity.class);
			ownerActivity.startActivity(intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.ownerActivity = activity;
	}

	@Override
	public void onOpen() {
		if (this.shadowView != null) {
			this.shadowView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClose() {
		if (this.shadowView != null) {
			this.shadowView.setVisibility(View.GONE);
		}
	}

}
