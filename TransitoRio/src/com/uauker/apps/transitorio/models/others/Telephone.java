package com.uauker.apps.transitorio.models.others;
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Telephone implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5090645430953558070L;

	@SerializedName("label")
	public String name;

	@SerializedName("tels")
	public List<String> tels;

	public CharSequence[] toCharSequenceList() {
		return this.tels.toArray(new CharSequence[this.tels.size()]);
	}
}