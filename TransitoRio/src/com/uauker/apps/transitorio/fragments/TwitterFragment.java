package com.uauker.apps.transitorio.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.adapters.TwitterAdapter;
import com.uauker.apps.transitorio.helpers.BannerHelper;
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

	private Activity ownerActivity;

	private TwitterAsyncTask task;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		ab.setTitle(R.string.traffic);

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
		loadTweets();
	}

	private void loadTweets() {
		this.task = new TwitterAsyncTask();
		this.task.execute();
	}

	class TwitterAsyncTask extends AsyncTask<Void, Void, List<Tweet>> {

		@Override
		protected List<Tweet> doInBackground(Void... params) {
			try {

				TwitterService twitter = new TwitterService();

				tweets = twitter.getTweetsFromUserList("uauker", "transito-rj",
						10);

			} catch (TwitterServiceException e) {
				// como tratar o erro??
			}

			return tweets;
		}

		@Override
		protected void onPostExecute(List<Tweet> result) {
			super.onPostExecute(result);

			TwitterAdapter twitterAdapter = new TwitterAdapter(ownerActivity, R.layout.adapter_twitter, tweets);
			twitterListView.setAdapter(twitterAdapter);
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
