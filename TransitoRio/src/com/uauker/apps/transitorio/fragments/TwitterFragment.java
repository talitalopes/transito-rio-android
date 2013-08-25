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
import com.uauker.apps.transitorio.adapters.TwitterAdapter;
import com.uauker.apps.transitorio.helpers.AnalyticsHelper;
import com.uauker.apps.transitorio.helpers.BannerHelper;
import com.uauker.apps.transitorio.helpers.ConfigHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper.OnClickToTryAgain;
import com.uauker.apps.transitorio.models.twitter.Tweet;
import com.uauker.apps.transitorio.services.TwitterService;
import com.uauker.apps.transitorio.services.TwitterServiceException;

public class TwitterFragment extends SherlockFragment implements
		OnClickToTryAgain, OnOpenListener, OnCloseListener {

	ImageView shadowView;

	ListView twitterListView;

	ViewStub loadingViewStub;
	ViewStub internetFailureViewStub;
	View emptyView;

	List<Tweet> tweets = new ArrayList<Tweet>();

	int page = 1;

	private Activity ownerActivity;

	private TwitterAsyncTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		AnalyticsHelper.sendView(AnalyticsHelper.SCREEN_HOME);

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		ab.setTitle(R.string.app_name);

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

		this.twitterListView = (ListView) contentView
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

		loadTweets();

		if (contentView != null) {
			BannerHelper.setUpAdmob(contentView);
		}

		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_reload, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			loadTweets();
			AnalyticsHelper.sendEvent(AnalyticsHelper.CATEGORY_RELOAD,
					AnalyticsHelper.ACTION_RELOAD_HOME);
			return true;
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

		loadTweets();
	}

	private void loadTweets() {
		this.task = new TwitterAsyncTask();
		this.task.execute();
	}

	class TwitterAsyncTask extends AsyncTask<Void, Void, List<Tweet>> {

		int lastTweetsSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			TwitterFragment.this.loadingViewStub.setVisibility(View.VISIBLE);
			TwitterFragment.this.internetFailureViewStub
					.setVisibility(View.GONE);
			TwitterFragment.this.twitterListView.setVisibility(View.GONE);
		}

		@Override
		protected List<Tweet> doInBackground(Void... params) {
			try {
				TwitterService twitter = new TwitterService();

				long lastTweetID = -1;

				if (page == 1) {
					tweets.clear();
				}

				if (tweets.size() > 0) {
					lastTweetID = tweets.get(tweets.size() - 1).id;
					lastTweetsSize = tweets.size() - 1;
				}

				tweets.addAll(twitter.getTweetsFromUserList(
						ConfigHelper.TWITTER_USERNAME,
						ConfigHelper.TWITTER_LISTNAME, page, lastTweetID));
			} catch (TwitterServiceException e) {
				Log.d("Error no TwitterServiceException: " + e.getMessage());
			}

			return tweets;
		}

		@Override
		protected void onPostExecute(List<Tweet> result) {
			super.onPostExecute(result);

			if (result.size() < 1) {
				TwitterFragment.this.loadingViewStub.setVisibility(View.GONE);
				TwitterFragment.this.internetFailureViewStub
						.setVisibility(View.VISIBLE);
				TwitterFragment.this.twitterListView.setVisibility(View.GONE);

				return;
			}

			TwitterAdapter twitterAdapter = new TwitterAdapter(ownerActivity,
					R.layout.adapter_twitter, tweets);
			twitterListView.setAdapter(twitterAdapter);
			twitterListView.setSelection(lastTweetsSize);

			TwitterFragment.this.loadingViewStub.setVisibility(View.GONE);
			TwitterFragment.this.internetFailureViewStub
					.setVisibility(View.GONE);
			TwitterFragment.this.twitterListView.setVisibility(View.VISIBLE);
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
