package com.uauker.apps.transitorio.activities;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.github.rtyley.android.sherlock.roboguice.activity.RoboSherlockListActivity;
import com.google.analytics.tracking.android.Log;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.adapters.TwitterAdapter;
import com.uauker.apps.transitorio.helpers.AnalyticsHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper.OnClickToTryAgain;
import com.uauker.apps.transitorio.models.twitter.Tweet;
import com.uauker.apps.transitorio.models.twitter.TwitterUser;
import com.uauker.apps.transitorio.services.TwitterService;
import com.uauker.apps.transitorio.services.TwitterServiceException;

public class TweetsFromUserActivity extends RoboSherlockListActivity implements
		OnClickToTryAgain {

	public static final String SELECTED_TWITTER_USER = TweetsFromUserActivity.class
			.getName() + "#selected_twitter_user";

	TwitterUser twitterUser;

	@InjectView(R.id.twitter_loading)
	ViewStub loadingViewStub;

	@InjectView(R.id.twitter_internet_failure)
	ViewStub internetFailureViewStub;

	List<Tweet> tweets = new ArrayList<Tweet>();

	int page = 1;

	private Activity ownerActivity;

	private TwitterAsyncTask task;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets_from_user);

		AnalyticsHelper.sendView(AnalyticsHelper.SCREEN_HOME);

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		ab.setTitle(this.twitterUser.username);
		ab.setHomeButtonEnabled(true);

		TryAgainHelper tryAgainView = (TryAgainHelper) this.internetFailureViewStub
				.inflate();
		tryAgainView.setOnClickToTryAgain(this);

		loadTweets();

		// TODO: Implementar banner na tela de Tweets
		// BannerHelper.setUpAdmob(contentView);
	}

	private void loadTweets() {
		this.task = new TwitterAsyncTask();
		this.task.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.menu_reload, menu);
		return true;
	}

	@Override
	public void tryAgain() {
		page = 1;
		loadTweets();
	}

	class TwitterAsyncTask extends AsyncTask<Void, Void, List<Tweet>> {

		int lastTweetsSize = 0;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			loadingViewStub.setVisibility(View.VISIBLE);
			internetFailureViewStub.setVisibility(View.GONE);
			setVisible(false);
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

				tweets.addAll(twitter.getTweetsFromUser(twitterUser.screenName,
						page, lastTweetID));
			} catch (TwitterServiceException e) {
				Log.d("Error no TwitterServiceException: " + e.getMessage());
			}

			return tweets;
		}

		@Override
		protected void onPostExecute(List<Tweet> result) {
			super.onPostExecute(result);

			if (result.size() < 1) {
				loadingViewStub.setVisibility(View.GONE);
				internetFailureViewStub.setVisibility(View.VISIBLE);
				setVisible(false);

				return;
			}

			TwitterAdapter twitterAdapter = new TwitterAdapter(ownerActivity,
					R.layout.adapter_twitter, tweets);
			setListAdapter(twitterAdapter);
			setSelection(lastTweetsSize);

			loadingViewStub.setVisibility(View.GONE);
			internetFailureViewStub.setVisibility(View.GONE);
			setVisible(true);
		}

	}

}
