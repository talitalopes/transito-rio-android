package com.uauker.apps.transitorio.adapters;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.helpers.AnalyticsHelper;
import com.uauker.apps.transitorio.models.others.Telephone;

public class TelephoneAdapter extends ArrayAdapter<Telephone> {

	private List<Telephone> datasource;
	private LayoutInflater inflater;
	private Activity ownerActivity;

	public TelephoneAdapter(Context context, int layout,
			List<Telephone> telephones) {
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
				createTelephoneDialog(telephone);
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

	public void createTelephoneDialog(final Telephone telephone) {
		AlertDialog.Builder builder = new AlertDialog.Builder(ownerActivity);

		LayoutInflater inflater = ownerActivity.getLayoutInflater();

		ViewGroup dialogLayout = (ViewGroup) inflater.inflate(
				R.layout.dialog_telephone_buttons, null);
		builder.setView(dialogLayout);

		TextView dialogTitle = (TextView) dialogLayout
				.findViewById(R.id.dialog_telephone_buttons_title);
		dialogTitle.setText(telephone.name);

		builder.setNegativeButton(
				ownerActivity.getResources().getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		final AlertDialog dialog = builder.create();

		for (final String tel : telephone.tels) {
			Button buttonToCall = (Button) inflater.inflate(
					R.layout.button_telephone_dialog, null);
			buttonToCall.setText(tel);
			;
			buttonToCall.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AnalyticsHelper.sendEvent(AnalyticsHelper.CATEGORY_LIGAR,
							telephone.name);

					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + tel));
					ownerActivity.startActivity(callIntent);
				}
			});

			dialogLayout.addView(buttonToCall, setLayoutParams());
		}

		dialog.show();
	}

	private LinearLayout.LayoutParams setLayoutParams() {
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		layoutParams.setMargins(30, 0, 30, 5);
		return layoutParams;
	}

}
