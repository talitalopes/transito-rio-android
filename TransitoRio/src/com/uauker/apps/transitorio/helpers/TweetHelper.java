package com.uauker.apps.transitorio.helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import com.twitter.Extractor;
import com.twitter.Extractor.Entity;
import com.uauker.apps.transitorio.R;

public class TweetHelper {

	public static SpannableString colored(final Activity activity, String text) {
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
					activity.startActivity(i);
				}
			};

			textSpannableString.setSpan(clickableSpan, entity.getStart(),
					entity.getEnd(), 0);
		}

		return textSpannableString;
	}

}
