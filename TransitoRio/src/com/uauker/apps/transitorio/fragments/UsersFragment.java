package com.uauker.apps.transitorio.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.google.analytics.tracking.android.Log;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.activities.SettingsActivity;
import com.uauker.apps.transitorio.adapters.UsersAdapter;
import com.uauker.apps.transitorio.helpers.AnalyticsHelper;
import com.uauker.apps.transitorio.helpers.BannerHelper;
import com.uauker.apps.transitorio.helpers.ConfigHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper.OnClickToTryAgain;
import com.uauker.apps.transitorio.models.twitter.TwitterUser;
import com.uauker.apps.transitorio.services.TwitterService;
import com.uauker.apps.transitorio.services.TwitterServiceException;

public class UsersFragment extends SherlockFragment implements
		OnClickToTryAgain, OnOpenListener, OnCloseListener {

	ImageView shadowView;

	ListView twitterUsersListView;

	ViewStub loadingViewStub;
	ViewStub internetFailureViewStub;
	View emptyView;

	List<TwitterUser> users = new ArrayList<TwitterUser>();

	int page = 1;

	private Activity ownerActivity;

	private UsersAsyncTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		AnalyticsHelper.sendView(AnalyticsHelper.SCREEN_CONTAS_DO_TWITTER);

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		ab.setTitle(R.string.users);

		setHasOptionsMenu(true);

		View contentView = inflater.inflate(R.layout.fragment_twitter,
				container, false);

		this.shadowView = (ImageView) contentView
				.findViewById(R.id.twitter_shadow);

		this.loadingViewStub = (ViewStub) contentView
				.findViewById(R.id.twitter_loading);
		this.loadingViewStub.setLayoutResource(R.layout.loading);

		this.internetFailureViewStub = (ViewStub) contentView
				.findViewById(R.id.twitter_internet_failure);
		this.internetFailureViewStub
				.setLayoutResource(R.layout.internet_failure);
		TryAgainHelper tryAgainView = (TryAgainHelper) this.internetFailureViewStub
				.inflate();
		tryAgainView.setOnClickToTryAgain(this);

		this.twitterUsersListView = (ListView) contentView
				.findViewById(R.id.twitter_listview);

		// Button btnLoadMore = new Button(ownerActivity);
		// btnLoadMore.setText("Load More");
		// btnLoadMore.setOnClickListener(new Button.OnClickListener() {
		// public void onClick(View v) {
		// page++;
		//
		// loadTweets();
		// }
		// });
		//
		// this.twitterListView.addFooterView(btnLoadMore);

		this.emptyView = contentView
				.findViewById(R.id.twitter_list_empty_message);

		loadUsers();

		if (contentView != null) {
			BannerHelper.setUpAdmob(contentView);
		}

		return contentView;
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
	public void onDestroy() {
		super.onDestroy();

		if (task != null) {
			task.cancel(true);
		}
	}

	@Override
	public void tryAgain() {
		page = 1;

		loadUsers();
	}

	private void loadUsers() {
		this.task = new UsersAsyncTask();
		this.task.execute();
	}

	class UsersAsyncTask extends AsyncTask<Void, Void, List<TwitterUser>> {

		int lastTweetsSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			UsersFragment.this.loadingViewStub.setVisibility(View.VISIBLE);
			UsersFragment.this.internetFailureViewStub
					.setVisibility(View.GONE);
			UsersFragment.this.twitterUsersListView.setVisibility(View.GONE);
		}

		@Override
		protected List<TwitterUser> doInBackground(Void... params) {
			try {
				TwitterService twitter = new TwitterService();
				users.addAll(twitter.getUsersFromUserList(ConfigHelper.TWITTER_USERNAME, ConfigHelper.TWITTER_LISTNAME));
			} catch (TwitterServiceException e) {
				Log.d("Error no TwitterServiceException: " + e.getMessage());
			}

			return users;
		}

		@Override
		protected void onPostExecute(List<TwitterUser> result) {
			super.onPostExecute(result);

			if (result.size() < 1) {
				UsersFragment.this.loadingViewStub.setVisibility(View.GONE);
				UsersFragment.this.internetFailureViewStub
						.setVisibility(View.VISIBLE);
				UsersFragment.this.twitterUsersListView.setVisibility(View.GONE);

				return;
			}

			UsersAdapter userAdapter = new UsersAdapter(ownerActivity,
					R.layout.adapter_twitter, users);
			twitterUsersListView.setAdapter(userAdapter);
			twitterUsersListView.setSelection(lastTweetsSize);

			UsersFragment.this.loadingViewStub.setVisibility(View.GONE);
			UsersFragment.this.internetFailureViewStub
					.setVisibility(View.GONE);
			UsersFragment.this.twitterUsersListView.setVisibility(View.VISIBLE);
		}

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
