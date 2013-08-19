package com.uauker.apps.transitorio.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnCloseListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.adapters.RodoviaAdapter;
import com.uauker.apps.transitorio.helpers.BannerHelper;
import com.uauker.apps.transitorio.helpers.ConfigHelper;
import com.uauker.apps.transitorio.helpers.RodoviaHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper.OnClickToTryAgain;
import com.uauker.apps.transitorio.models.Occurrence;

public class TwitterFragment extends SherlockFragment implements
		OnClickToTryAgain, OnOpenListener, OnCloseListener {

	ImageView shadowView;

	ListView occurrencesListView;

	ViewStub loadingViewStub;
	ViewStub internetFailureViewStub;
	View emptyView;

	List<Occurrence> occurrences = new ArrayList<Occurrence>();

	AsyncHttpClient client = new AsyncHttpClient();

	private Activity ownerActivity;

	public String slugTwitter;

	public ColorDrawable twitterColor;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		if (ownerActivity != null && slugTwitter != null) {
			ab.setTitle(RodoviaHelper.findByName(ownerActivity, slugTwitter));
		}

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

		this.occurrencesListView = (ListView) contentView
				.findViewById(R.id.twitter_listview);

		this.emptyView = contentView
				.findViewById(R.id.twitter_list_empty_message);

		loadRodovias();

		if (contentView != null) {
			BannerHelper.setUpAdmob(contentView);
		}

		return contentView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_reload_and_call, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			loadRodovias();
			return true;

		case R.id.menu_telephone:
			calling();
			return true;
			// case R.id.menu_settings:
			// Intent intent = new Intent(ownerActivity,
			// SettingsActivity.class);
			// ownerActivity.startActivity(intent);
			// return true;
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

		this.client.cancelRequests(ownerActivity, true);
	}

	@Override
	public void tryAgain() {
		loadRodovias();
	}

	private void calling() {
		String telephone = RodoviaHelper.findByTelephone(ownerActivity,
				slugTwitter);
		telephone = telephone.replace(" ", "");

		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + telephone));
		ownerActivity.startActivity(intent);
	}

	private void loadRodovias() {
		String url = ConfigHelper.urlFormat(slugTwitter);

		client.get(url, new TwitterAsyncTask());
	}

	class TwitterAsyncTask extends AsyncHttpResponseHandler {

		@Override
		public void onStart() {
			super.onStart();

			TwitterFragment.this.loadingViewStub.setVisibility(View.VISIBLE);
			TwitterFragment.this.internetFailureViewStub
					.setVisibility(View.GONE);
			TwitterFragment.this.occurrencesListView.setVisibility(View.GONE);
		}

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);

			TwitterFragment.this.loadingViewStub.setVisibility(View.GONE);
			TwitterFragment.this.internetFailureViewStub
					.setVisibility(View.VISIBLE);
			TwitterFragment.this.occurrencesListView.setVisibility(View.GONE);
		}

		@Override
		public void onSuccess(String result) {
			super.onSuccess(result);

			try {
				Gson gson = new Gson();
				JsonArray content = new JsonParser().parse(result)
						.getAsJsonObject().getAsJsonArray("Occurences");

				Iterator<JsonElement> it = content.iterator();

				while (it.hasNext()) {
					JsonElement occurrenceJson = it.next();
					Occurrence occurrence = gson.fromJson(occurrenceJson,
							Occurrence.class);
					occurrences.add(occurrence);
				}

				TwitterFragment.this.loadingViewStub.setVisibility(View.GONE);
				TwitterFragment.this.internetFailureViewStub
						.setVisibility(View.GONE);
				TwitterFragment.this.occurrencesListView
						.setVisibility(View.VISIBLE);

				setupListView();
			} catch (Exception e) {
				onFailure(e, "");
			}
		}

		@Override
		public void onFinish() {
			super.onFinish();

			if (TwitterFragment.this.occurrences.size() == 0
					&& TwitterFragment.this.internetFailureViewStub
							.getVisibility() != View.VISIBLE) {
				TwitterFragment.this.occurrencesListView
						.setEmptyView(TwitterFragment.this.emptyView);
			}
		}

		private void setupListView() {
			RodoviaAdapter occurrenceAdapter = new RodoviaAdapter(
					TwitterFragment.this.ownerActivity,
					R.layout.adapter_rodovia, occurrences);
			occurrencesListView.setAdapter(occurrenceAdapter);
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
