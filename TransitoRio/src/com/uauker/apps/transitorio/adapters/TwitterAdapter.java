package com.uauker.apps.transitorio.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitter.Extractor;
import com.twitter.Extractor.Entity;
import com.uauker.apps.transitorio.R;
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
		tweetText.setText(twitterTextSpannable(tweet.text),
				BufferType.SPANNABLE);

		TextView tweetPublishedAt = (TextView) rowView
				.findViewById(R.id.adapter_twitter_published_at);
		tweetPublishedAt.setText(tweet.humanDate());

		return rowView;
	}

	public SpannableString twitterTextSpannable(String text) {
		SpannableString textSpannableString = new SpannableString(text);

		if (Build.VERSION.SDK_INT < VERSION_CODES.ICE_CREAM_SANDWICH) {
			return textSpannableString;
		}

		Extractor extractor = new Extractor();

		for (final Entity entity : extractor.extractHashtagsWithIndices(text)) {
			textSpannableString.setSpan(new ForegroundColorSpan(
					R.color.twitter_hashtag), entity.getStart(), entity
					.getEnd(), 0);
		}

		for (final Entity entity : extractor
				.extractMentionedScreennamesWithIndices(text)) {
			textSpannableString.setSpan(new ForegroundColorSpan(
					R.color.twitter_screenname), entity.getStart(), entity
					.getEnd(), 0);
			textSpannableString.setSpan(new StyleSpan(Typeface.BOLD),
					entity.getStart(), entity.getEnd(), 0);
		}

		for (final Entity entity : extractor.extractURLsWithIndices(text)) {
			textSpannableString
					.setSpan(new ForegroundColorSpan(R.color.twitter_url),
							entity.getStart(), entity.getEnd(), 0);

			ClickableSpan clickableSpan = new ClickableSpan() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(entity.getValue()));
					ownerActivity.startActivity(i);
				}
			};

			textSpannableString.setSpan(clickableSpan, entity.getStart(),
					entity.getEnd(), 0);
		}

		return textSpannableString;
	}

	@Override
	public int getCount() {
		return this.datasource.size();
	}

	public Tweet getTweet(int position) {
		return this.datasource.get(position);
	}
}
