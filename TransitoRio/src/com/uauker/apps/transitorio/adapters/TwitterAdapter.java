package com.uauker.apps.transitorio.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.helpers.TweetHelper;
import com.uauker.apps.transitorio.models.twitter.Tweet;

public class TwitterAdapter extends ArrayAdapter<Tweet> {

	private List<Tweet> datasource;
	private LayoutInflater inflater;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private Activity ownerActivity;

	public TwitterAdapter(Context context, int layout, List<Tweet> tweets) {
		super(context, 0, tweets);
		this.inflater = LayoutInflater.from(context);
		this.datasource = tweets;
		this.ownerActivity = (Activity) context;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		final Tweet tweet = getTweet(position);

		rowView = inflater.inflate(R.layout.adapter_twitter, parent, false);

		ImageView tweetImage = (ImageView) rowView
				.findViewById(R.id.adapter_twitter_image);
		imageLoader.displayImage(tweet.user.profileImageURL, tweetImage,
				options);

		TextView tweetUserName = (TextView) rowView
				.findViewById(R.id.adapter_twitter_user_name);
		tweetUserName.setText(tweet.user.username);

		TextView tweetText = (TextView) rowView
				.findViewById(R.id.adapter_twitter_text);
		tweetText.setMovementMethod(LinkMovementMethod.getInstance());
		tweetText.setText(TweetHelper.colored(ownerActivity, tweet.text),
				BufferType.SPANNABLE);

		TextView tweetPublishedAt = (TextView) rowView
				.findViewById(R.id.adapter_twitter_published_at);
		tweetPublishedAt.setText(tweet.humanDate());

		return rowView;
	}

	@Override
	public int getCount() {
		return this.datasource.size();
	}

	public Tweet getTweet(int position) {
		return this.datasource.get(position);
	}
}
