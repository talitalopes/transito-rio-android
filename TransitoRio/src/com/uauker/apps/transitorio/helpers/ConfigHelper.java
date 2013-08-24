package com.uauker.apps.transitorio.helpers;


public class ConfigHelper {

	public final static boolean isProduction = true;

	public final static String CHARSET_DEFAULT = "UTF-8";	
	
	public final static String TWITTER_CONSUMER_KEY = "hlycH46ulhO4VSox1UnKw";
	public final static String TWITTER_CONSUMER_SECRET = "8DS8jaVCAV55h4sqGBc9IrHtz58lwldiHaTaJpnsU";
	public final static String TWITTER_ACCESS_TOKEN = "25685569-T5RzbFow859fZfKXVaAvhTjOfveTrB63jsK6EbV8";
	public final static String TWITTER_ACCESS_TOKEN_SECRET = "Hdi7Ox70HPVsiBYGDFFnrEeHNy2LBF8AbaqXemnzBM";
	
	public final static String SOURCE_URL = "http://www.%s.com.br/generic/home/ListOccurrences";
	
	public static String urlFormat(String... values) {
		return String.format(SOURCE_URL, values);
	}
}
