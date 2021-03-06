package com.uauker.apps.transitorio.fragments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.afinal.simplecache.ACache;

import android.annotation.SuppressLint;
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
import com.uauker.apps.transitorio.activities.SettingsActivity;
import com.uauker.apps.transitorio.adapters.RodoviaAdapter;
import com.uauker.apps.transitorio.helpers.AnalyticsHelper;
import com.uauker.apps.transitorio.helpers.BannerHelper;
import com.uauker.apps.transitorio.helpers.ConfigHelper;
import com.uauker.apps.transitorio.helpers.RodoviaHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper;
import com.uauker.apps.transitorio.helpers.TryAgainHelper.OnClickToTryAgain;
import com.uauker.apps.transitorio.models.ccr.Occurrence;

@SuppressLint("ValidFragment")
public class RodoviaFragment extends SherlockFragment implements
		OnClickToTryAgain, OnOpenListener, OnCloseListener {

	public static final int CACHE_TIME = 5 * 60;
	
	ImageView shadowView;

	ListView occurrencesListView;

	ViewStub loadingViewStub;
	ViewStub internetFailureViewStub;
	View emptyView;

	List<Occurrence> occurrences = new ArrayList<Occurrence>();

	AsyncHttpClient client = new AsyncHttpClient();

	ACache cache;

	private Activity ownerActivity;

	public String slugRodovia;

	public String rodoviaTitle;

	public ColorDrawable rodoviaColor;

	public RodoviaFragment() {
		super();
	}

	public RodoviaFragment(String slug, ColorDrawable color) {
		super();

		this.slugRodovia = slug;
		this.rodoviaColor = color;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.cache = ACache.get(ownerActivity);

		rodoviaTitle = RodoviaHelper.findByName(ownerActivity, slugRodovia);

		setGAScreenView();

		final ActionBar ab = ((SherlockFragmentActivity) ownerActivity)
				.getSupportActionBar();

		if (ownerActivity != null && slugRodovia != null) {
			ab.setTitle(rodoviaTitle);
		}

		setHasOptionsMenu(true);

		View contentView = inflater.inflate(R.layout.fragment_rodovia,
				container, false);

		this.shadowView = (ImageView) contentView
				.findViewById(R.id.rodovia_shadow);

		this.loadingViewStub = (ViewStub) contentView
				.findViewById(R.id.rodovia_loading);
		this.loadingViewStub.setLayoutResource(R.layout.loading);

		this.internetFailureViewStub = (ViewStub) contentView
				.findViewById(R.id.rodovia_internet_failure);
		this.internetFailureViewStub
				.setLayoutResource(R.layout.internet_failure);
		TryAgainHelper tryAgainView = (TryAgainHelper) this.internetFailureViewStub
				.inflate();
		tryAgainView.setOnClickToTryAgain(this);

		this.occurrencesListView = (ListView) contentView
				.findViewById(R.id.rodovia_listview);

		this.emptyView = contentView
				.findViewById(R.id.rodovia_list_empty_message);

		loadRodovias();

		if (contentView != null) {
			BannerHelper.setUpAdmob(contentView);
		}

		return contentView;
	}

	private void setGAScreenView() {
		if (rodoviaTitle.equalsIgnoreCase(getString(R.string.novadutra))) {
			AnalyticsHelper.sendView(AnalyticsHelper.SCREEN_NOVA_DUTRA);
		} else if (rodoviaTitle
				.equalsIgnoreCase(getString(R.string.rodoviadoslagos))) {
			AnalyticsHelper.sendView(AnalyticsHelper.SCREEN_VIA_LAGOS);
		} else if (rodoviaTitle.equalsIgnoreCase(getString(R.string.ponte))) {
			AnalyticsHelper.sendView(AnalyticsHelper.SCREEN_PONTE);
		}

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
			AnalyticsHelper.sendEvent(AnalyticsHelper.CATEGORY_RELOAD,
					rodoviaTitle);
			return true;

		case R.id.menu_telephone:
			calling();
			AnalyticsHelper.sendEvent(AnalyticsHelper.CATEGORY_LIGAR,
					rodoviaTitle);
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

		this.client.cancelRequests(ownerActivity, true);
	}

	@Override
	public void tryAgain() {
		loadRodovias();
	}

	private void calling() {
		String telephone = RodoviaHelper.findByTelephone(ownerActivity,
				slugRodovia);

		telephone = telephone.replace(" ", "");

		Intent intent = new Intent(Intent.ACTION_CALL);
		intent.setData(Uri.parse("tel:" + telephone));
		ownerActivity.startActivity(intent);
	}

	private void loadRodovias() {
		String url = ConfigHelper.urlFormat(slugRodovia);
		String cacheContent = cache.getAsString(url);

		SectionAsyncTask sectionAsyncTask = new SectionAsyncTask();
		sectionAsyncTask.url = url;
		
		if (cacheContent == null) {
			client.get(url, sectionAsyncTask);
		} else {
			sectionAsyncTask.isCached = true;
			sectionAsyncTask.onSuccess(cacheContent);
		}
	}

	class SectionAsyncTask extends AsyncHttpResponseHandler {

		public String url;
		public boolean isCached = false;

		@Override
		public void onStart() {
			super.onStart();

			RodoviaFragment.this.loadingViewStub.setVisibility(View.VISIBLE);
			RodoviaFragment.this.internetFailureViewStub
					.setVisibility(View.GONE);
			RodoviaFragment.this.occurrencesListView.setVisibility(View.GONE);
		}

		@Override
		public void onFailure(Throwable arg0, String arg1) {
			super.onFailure(arg0, arg1);

			RodoviaFragment.this.loadingViewStub.setVisibility(View.GONE);
			RodoviaFragment.this.internetFailureViewStub
					.setVisibility(View.VISIBLE);
			RodoviaFragment.this.occurrencesListView.setVisibility(View.GONE);
		}

		@Override
		public void onSuccess(String result) {
			super.onSuccess(result);

			try {
				Gson gson = new Gson();
				JsonArray content = new JsonParser().parse(result)
						.getAsJsonObject().getAsJsonArray("Occurences");

				Iterator<JsonElement> it = content.iterator();

				occurrences.clear();

				while (it.hasNext()) {
					JsonElement occurrenceJson = it.next();
					Occurrence occurrence = gson.fromJson(occurrenceJson,
							Occurrence.class);
					occurrences.add(occurrence);
				}
				
				if (!isCached) {
					cache.put(url, result, CACHE_TIME);
				}

				RodoviaFragment.this.loadingViewStub.setVisibility(View.GONE);
				RodoviaFragment.this.internetFailureViewStub
						.setVisibility(View.GONE);
				RodoviaFragment.this.occurrencesListView
						.setVisibility(View.VISIBLE);

				setupListView();
			} catch (Exception e) {
				onFailure(e, "");
			}
		}

		@Override
		public void onFinish() {
			super.onFinish();

			if (RodoviaFragment.this.occurrences.size() == 0
					&& RodoviaFragment.this.internetFailureViewStub
							.getVisibility() != View.VISIBLE) {
				RodoviaFragment.this.occurrencesListView
						.setEmptyView(RodoviaFragment.this.emptyView);
			}
		}

		private void setupListView() {
			RodoviaAdapter occurrenceAdapter = new RodoviaAdapter(
					RodoviaFragment.this.ownerActivity,
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
