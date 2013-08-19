package com.uauker.apps.transitorio.comparators;

import java.util.Comparator;

import android.annotation.SuppressLint;

import com.uauker.apps.transitorio.models.ccr.Occurrence;

public class OccurrenceComparator implements Comparator<Occurrence> {

	@SuppressLint("DefaultLocale")
	@Override
	public int compare(Occurrence lhs, Occurrence rhs) {
		String directionNameA = lhs.roadDirectionName.toLowerCase();
		String directionNameB = rhs.roadDirectionName.toLowerCase();

		int compared = directionNameA.compareTo(directionNameB);

		if (compared == 0 && lhs.distanceMarkerIni > rhs.distanceMarkerIni) {
			return 1;
		}

		if (compared == 0 && lhs.distanceMarkerIni < rhs.distanceMarkerIni) {
			return -1;
		}		
		
		return compared;
	}

}
