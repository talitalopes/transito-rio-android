package com.uauker.apps.transitorio.models.twitter;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7953009711285241303L;
	
	public TwitterUser user;
	public Date publishDate;
	public String text;

}