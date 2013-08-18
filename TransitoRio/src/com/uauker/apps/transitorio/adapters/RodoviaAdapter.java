package com.uauker.apps.transitorio.adapters;

import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.comparators.OccurrenceComparator;
import com.uauker.apps.transitorio.models.Occurrence;

@SuppressLint("DefaultLocale")
public class RodoviaAdapter extends ArrayAdapter<Occurrence> {

	private List<Occurrence> datasource;
	private LayoutInflater inflater;
	private Activity ownerActivity;

	public RodoviaAdapter(Context context, int layout,
			List<Occurrence> occurrences) {
		super(context, 0, occurrences);

		this.ownerActivity = (Activity) context;
		this.inflater = LayoutInflater.from(context);

		this.datasource = occurrences;

		OccurrenceComparator comparator = new OccurrenceComparator();
		Collections.sort(occurrences, comparator);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View rowView = convertView;

		final Occurrence occurrence = getOccurrence(position);
		int countText = occurrence.getStatus().length()
				+ occurrence.getDistanceFormatted().length();

		int layout = (countText > 35) ? R.layout.adapter_rodovia_big_text
				: R.layout.adapter_rodovia;

		rowView = inflater.inflate(layout, parent, false);

		TextView title = (TextView) rowView
				.findViewById(R.id.adapter_rodovia_title);
		title.setBackgroundColor(ownerActivity.getResources().getColor(
				occurrence.getSecondColor()));
		title.setText(occurrence.roadDirectionName.toUpperCase());

		View indicator = (View) rowView
				.findViewById(R.id.adapter_rodovia_indicator);
		indicator.setBackgroundColor(ownerActivity.getResources().getColor(
				occurrence.getColor()));

		View indicatorFooter = (View) rowView
				.findViewById(R.id.adapter_rodovia_indicator_footer);
		indicatorFooter.setBackgroundColor(ownerActivity.getResources()
				.getColor(occurrence.getColor()));

		TextView status = (TextView) rowView
				.findViewById(R.id.adapter_rodovia_status);
		status.setText(occurrence.getStatus());
		status.setBackgroundColor(ownerActivity.getResources().getColor(
				occurrence.getColor()));

		TextView kms = (TextView) rowView
				.findViewById(R.id.adapter_rodovia_kms);
		kms.setText(occurrence.getDistanceFormatted());

		TextView text = (TextView) rowView
				.findViewById(R.id.adapter_rodovia_text);
		text.setText(occurrence.text);

		notifyDataSetChanged();

		return rowView;
	}

	@Override
	public int getCount() {
		return this.datasource.size();
	}

	public Occurrence getOccurrence(int position) {
		return this.datasource.get(position);
	}

}
