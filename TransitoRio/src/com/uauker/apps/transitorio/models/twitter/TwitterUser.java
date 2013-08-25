package com.uauker.apps.transitorio.models.twitter;

import java.io.Serializable;

import twitter4j.User;

public class TwitterUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6800286239770930940L;
	
	public String username;
	public String screenName;
	public String profileImageURL;
	public String description;

	public TwitterUser(User user) {
		this.username = user.getName();
		this.screenName = user.getScreenName();
		this.profileImageURL = user.getProfileImageURL();
		this.description = user.getDescription();
	}
}