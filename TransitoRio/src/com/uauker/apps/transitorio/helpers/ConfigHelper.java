package com.uauker.apps.transitorio.helpers;


public class ConfigHelper {

	public final static boolean isProduction = false;

	public final static String CHARSET_DEFAULT = "UTF-8";	
	
	public final static String SOURCE_URL = "http://www.%s.com.br/generic/home/ListOccurrences";
	
	public static String urlFormat(String... values) {
		return String.format(SOURCE_URL, values);
	}
}
