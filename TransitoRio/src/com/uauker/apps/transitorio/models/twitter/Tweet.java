package com.uauker.apps.transitorio.models.twitter;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7953009711285241303L;
	
	public TwitterUser user;
	public Date publishDate;
	public String text;
	
	public String humanDate(Locale locale) {
		PrettyTime p = new PrettyTime(locale);
		return p.format(publishDate);
	}	
}