package com.uauker.apps.transitorio.models.ccr;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class GeoLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8652071757956881760L;

	@SerializedName("Lat")
	public float lat;
	
	@SerializedName("Lng")
	public float lng;
	
}
