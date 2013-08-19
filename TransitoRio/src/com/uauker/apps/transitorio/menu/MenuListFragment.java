package com.uauker.apps.transitorio.menu;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uauker.apps.transitorio.R;
import com.uauker.apps.transitorio.activities.MainActivity;
import com.uauker.apps.transitorio.fragments.RodoviaFragment;
import com.uauker.apps.transitorio.helpers.SharedPreferencesHelper;

public class MenuListFragment extends ListFragment {

	public enum Source {
		PONTE, NOVADUTRA, RODOVIADOSLAGOS, RODONORTE, AUTOBAN, VIAOESTE, RODOANELOESTE, SPVIAS
	};

	private Activity ownerActivity;

	public final static String SELECTED_MENU_ROW = "selectedMenuRow";

	RodoviaMenuAdapter adapter;

	SharedPreferencesHelper sharedPreferences;

	List<SourceItemMenu> rodovias = new ArrayList<SourceItemMenu>();

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		sharedPreferences = SharedPreferencesHelper.getInstance(ownerActivity);

		String itemMenu = sharedPreferences.getString(SELECTED_MENU_ROW, null);

		if (itemMenu == null || itemMenu.trim().equalsIgnoreCase("")) {
			sharedPreferences.setString(SELECTED_MENU_ROW, "0");
		}

		return inflater.inflate(R.layout.menu_list, null);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		this.ownerActivity = activity;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new RodoviaMenuAdapter(ownerActivity);

		for (SourceItemMenu item : getRodoviaFromMenu()) {
			adapter.add(item);
		}

		setListAdapter(adapter);
	}

	private List<SourceItemMenu> getRodoviaFromMenu() {
		for (String item : getResources().getStringArray(R.array.menu)) {
			rodovias.add(new SourceItemMenu(item, R.color.home,
					R.color.home_second));
		}

		return rodovias;
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) {
			return;
		}

		MainActivity fca = (MainActivity) getActivity();
		fca.switchContent(fragment);
	}

	public class SourceItemMenu {
		public String name;
		public int color;
		public int secondColor;

		public SourceItemMenu(String name, int color, int secondColor) {
			this.name = name;
			this.color = color;
			this.secondColor = secondColor;
		}
	}

	public class RodoviaMenuAdapter extends ArrayAdapter<SourceItemMenu> {

		public RodoviaMenuAdapter(Context context) {
			super(context, 0);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final SourceItemMenu item = getItem(position);

			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.adapter_menu, null);
			}

			String positionShared = sharedPreferences
					.getString(SELECTED_MENU_ROW);

			TextView indicator = (TextView) convertView
					.findViewById(R.id.row_rodovia_indicator);
			indicator.setBackgroundColor(item.color);

			final TextView title = (TextView) convertView
					.findViewById(R.id.row_title);
			title.setText(item.name);

			if (String.valueOf(position).equalsIgnoreCase(positionShared)) {
				indicator.setBackgroundColor(item.secondColor);

				convertView.setBackgroundColor(item.color);
				title.setTextColor(getResources().getColor(R.color.white));
			} else {
				convertView.setBackgroundColor(ownerActivity.getResources()
						.getColor(R.color.background_default));
				title.setTextColor(getResources().getColor(R.color.black));
			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					Fragment newContent = null;

					Source itemRodovia = Source.values()[position];
					String slug = itemRodovia.name().toLowerCase();

					sharedPreferences.setString(SELECTED_MENU_ROW,
							String.valueOf(position));

					ColorDrawable rodoviaColor = new ColorDrawable(item.color);

					newContent = new RodoviaFragment(slug, rodoviaColor);

					if (newContent != null) {
						switchFragment(newContent);
					}

					title.setTextColor(getResources().getColor(R.color.white));

					notifyDataSetChanged();
				}
			});

			return convertView;
		}

	}

}