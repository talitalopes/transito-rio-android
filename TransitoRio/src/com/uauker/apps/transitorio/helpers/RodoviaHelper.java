package com.uauker.apps.transitorio.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.uauker.apps.transitorio.R;

public class RodoviaHelper {

	public static String findByName(Activity activity, String partUrl) {
		int id = activity.getResources().getIdentifier(partUrl, "string",
				activity.getPackageName());

		int idString = (id == 0) ? R.string.ponte : id;

		return activity.getResources().getString(idString);
	}

	@SuppressLint("DefaultLocale")
	public static String findByTelephone(Activity activity, String part) {
		part += "_tel";
		part = part.toLowerCase();
		
		int id = activity.getResources().getIdentifier(part, "string",
				activity.getPackageName());

		int idString = (id == 0) ? R.string.ponte : id;

		return activity.getResources().getString(idString);
	}
	
}
