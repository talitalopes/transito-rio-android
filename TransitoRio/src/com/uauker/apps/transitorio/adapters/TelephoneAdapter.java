package com.uauker.apps.transitorio.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.models.others.Telephone;

public class TelephoneAdapter extends ArrayAdapter<Telephone> {

	private List<Telephone> datasource;
	private LayoutInflater inflater;
	private Activity ownerActivity;

	public TelephoneAdapter(Context context, int layout, List<Telephone> telephones) {
		super(context, 0, telephones);
		this.inflater = LayoutInflater.from(context);
		this.datasource = telephones;
		this.ownerActivity = (Activity) context;
	}

	public View getView(final int position, View contentView, ViewGroup parent) {
		View rowView = contentView;

		final Telephone telephone = getTelephone(position);

		rowView = inflater.inflate(R.layout.adapter_telephone, parent, false);

		TextView TelephoneUserName = (TextView) rowView
				.findViewById(R.id.adapter_telephone_name);
		TelephoneUserName.setText(telephone.name);

		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: Click to call
				
			}
		});
		
		return rowView;
	}

	@Override
	public int getCount() {
		return this.datasource.size();
	}

	public Telephone getTelephone(int position) {
		return this.datasource.get(position);
	}

}
