package com.uauker.apps.transitorio.helpers;


public class ConfigHelper {

	public final static boolean isProduction = false;

	public final static String CHARSET_DEFAULT = "UTF-8";	
	
	public final static String TWITTER_CONSUMER_KEY = "WPDngmoDgU7SgRinKG5A";
	public final static String TWITTER_CONSUMER_SECRET = "2fbdQ69bdHAkhRLsrLKjj9gvdtdxJsKB7jgosO45nE";
	public final static String TWITTER_ACCESS_TOKEN = "25685569-SSMXlJ5SQqQ56u1DuedEDyikdQG9Uxc6y6hWGJ7gg";
	public final static String TWITTER_ACCESS_TOKEN_SECRET = "Vuc8NqLOwFXLVcKGphKKuh3XZnc8M5vEj61VQMEpWQ";
	
	public final static String SOURCE_URL = "http://www.%s.com.br/generic/home/ListOccurrences";
	
	public static String urlFormat(String... values) {
		return String.format(SOURCE_URL, values);
	}
}
