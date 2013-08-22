package com.uauker.apps.transitorio.models.twitter;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import org.ocpsoft.prettytime.PrettyTime;

import twitter4j.Status;

public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7953009711285241303L;
	
	public TwitterUser user;
	public Date publishDate;
	public String text;
	
	public Tweet(Status status) {
		this.user = new TwitterUser(status.getUser());
		this.publishDate = status.getCreatedAt();
		this.user.profileImageURL = status.getUser()
				.getProfileImageURL();
		this.text = status.getText();		
	}

	public String humanDate() {
		return humanDate(new Locale("PT"));
	}
	
	public String humanDate(Locale locale) {
		PrettyTime p = new PrettyTime(locale);
		return p.format(publishDate);
	}	
}