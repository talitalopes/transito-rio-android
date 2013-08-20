package com.uauker.apps.transitorio.services;

public class TwitterServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2731117355878345218L;

	public TwitterServiceException(String message, Throwable thw) {
		super(message, thw);
	}

	public TwitterServiceException(String message) {
		super(message);
	}

	public TwitterServiceException(Throwable thw) {
		super(thw);
	}

}