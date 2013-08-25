package com.uauker.apps.transitorio.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.activities.TweetsFromUserActivity;
import com.uauker.apps.transitorio.models.twitter.TwitterUser;

public class UsersAdapter extends ArrayAdapter<TwitterUser> {

	private List<TwitterUser> datasource;
	private LayoutInflater inflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private Activity ownerActivity;

	public UsersAdapter(Context context, int layout,
			List<TwitterUser> TwitterUsers) {
		super(context, 0, TwitterUsers);
		this.inflater = LayoutInflater.from(context);
		this.datasource = TwitterUsers;
		this.ownerActivity = (Activity) context;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		final TwitterUser twitterUser = getUser(position);

		rowView = inflater.inflate(R.layout.adapter_twitter, parent, false);

		ImageView twitterUserImage = (ImageView) rowView
				.findViewById(R.id.adapter_twitter_image);
		imageLoader.displayImage(twitterUser.profileImageURL, twitterUserImage,
				options);

		TextView TwitterUserUserName = (TextView) rowView
				.findViewById(R.id.adapter_twitter_user_name);
		TwitterUserUserName.setText(twitterUser.username);

		TextView TwitterUserText = (TextView) rowView
				.findViewById(R.id.adapter_twitter_text);
		TwitterUserText.setMovementMethod(LinkMovementMethod.getInstance());
		TwitterUserText.setText(twitterUser.description);

		rowView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ownerActivity, TweetsFromUserActivity.class);
				intent.putExtra(TweetsFromUserActivity.SELECTED_TWITTER_USER, twitterUser);
				ownerActivity.startActivity(intent);
			}
		});

		return rowView;
	}

	@Override
	public int getCount() {
		return this.datasource.size();
	}

	public TwitterUser getUser(int position) {
		return this.datasource.get(position);
	}

}
