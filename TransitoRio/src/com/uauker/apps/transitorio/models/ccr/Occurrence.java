package com.uauker.apps.transitorio.models.ccr;

import java.io.Serializable;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.uauker.apps.transitorio.R;

@SuppressWarnings("serial")
public class Occurrence implements Serializable {

	@SerializedName("Id")
	public int id;

	@SerializedName("Changed")
	public String changed;

	@SerializedName("Description")
	public String description;

	@SerializedName("LaneTypeName")
	public String laneTypeName;

	@SerializedName("City")
	public String city;

	@SerializedName("OccurrenceReasonSummary")
	public String occurrenceReasonSummary;

	@SerializedName("OccurrenceReasonDescription")
	public String occurrenceReasonDescription;

	@SerializedName("OccurrenceTypeImageIcon")
	public String occurrenceTypeImageIcon;

	@SerializedName("OccurrenceTypeName")
	public String occurrenceTypeName;

	@SerializedName("OccurrenceTypeCssClass")
	public String occurrenceTypeCssClass;

	@SerializedName("RoadName")
	public String roadName;

	@SerializedName("RoadId")
	public int roadId;

	@SerializedName("RoadDistanceLabel")
	public String roadDistanceLabel;

	@SerializedName("RoadDistanceShortLabel")
	public String roadDistanceShortLabel;

	@SerializedName("DistanceMarkerIni")
	public float distanceMarkerIni;

	@SerializedName("DistanceMarkerEnd")
	public float distanceMarkerEnd;

	@SerializedName("PixelCoord")
	public PixelCoord pixelCoord;

	@SerializedName("RoadDirectionName")
	public String roadDirectionName;

	@SerializedName("RoadDivisionName")
	public String roadDivisionName;

	@SerializedName("GeoLocation")
	public GeoLocation geoLocation;

	@SerializedName("Text")
	public String text;

	@SerializedName("TrafficText")
	public String trafficText;

	@SerializedName("SubtitleGreen")
	public String subtitleGreen;

	@SuppressLint("DefaultLocale")
	public String getStatus() {
		String status = this.occurrenceReasonSummary;

		if (status == null) {
			status = this.occurrenceTypeName;
		}

		status = status.toUpperCase();

		return status;
	}

	public int getColor() {
		if (occurrenceTypeCssClass.equalsIgnoreCase("green")) {
			return R.color.box_green;
		}

		if (occurrenceTypeCssClass.equalsIgnoreCase("yellow")) {
			return R.color.box_yellow;
		}

		if (occurrenceTypeCssClass.equalsIgnoreCase("orange")) {
			return R.color.box_orange;
		}

		return R.color.box_red;
	}

	public int getSecondColor() {
		if (occurrenceTypeCssClass.equalsIgnoreCase("green")) {
			return R.color.box_green_second;
		}

		if (occurrenceTypeCssClass.equalsIgnoreCase("yellow")) {
			return R.color.box_yellow_second;
		}

		if (occurrenceTypeCssClass.equalsIgnoreCase("orange")) {
			return R.color.box_orange_second;
		}

		return R.color.box_red_second;
	}

	@SuppressLint("DefaultLocale")
	public String getDistanceFormatted() {
		String distance = String.format("%.0fkm até %.0fkm",
				this.distanceMarkerIni, this.distanceMarkerEnd);

		if (this.distanceMarkerIni == this.distanceMarkerEnd) {
			distance = String.format("%.0fkm", this.distanceMarkerIni);
		}

		return distance;
	}
}
