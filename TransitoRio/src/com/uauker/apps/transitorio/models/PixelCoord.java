package com.uauker.apps.transitorio.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PixelCoord implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8510441932645465397L;

	@SerializedName("X")
	public int x;
	
	@SerializedName("Y")
	public int y;
}
