package com.uauker.apps.transitorio.helpers;

import android.util.Log;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;
import com.uauker.apps.transitorio.TransitoRioApplication;

public class AnalyticsHelper {

	private final static String ANALYTICS_ID_TRACKER_PRD = "UA-31546867-10";
	private final static String ANALYTICS_ID_TRACKER_DSV = "UA-31546867-8";

	private static GoogleAnalytics googleAnalytics;
	private static Tracker tracker;

	// Analytics tracker
	public final static String SCREEN_SPLASH = "/splash";
	public final static String SCREEN_HOME = "/home";
	public final static String SCREEN_PONTE = "/ponte-rio-niteroi";
	public final static String SCREEN_CONTAS_DO_TWITTER = "/contas_do_twitter";
	public final static String SCREEN_VIA_LAGOS = "/via-lagos";
	public final static String SCREEN_NOVA_DUTRA = "/nova-dutra";
	public final static String SCREEN_TELEPHONES = "/telephones";
	public final static String SCREEN_CONFIG = "/configuracoes";

	// Analytics tracker category
	public final static String CATEGORY_MENU_CONFIGURACOES = "Tela de configuracoes";
	public final static String CATEGORY_COMPARTILHAR = "Compartilhar";
	public final static String CATEGORY_LIGAR = "Botao de ligar";
	public final static String CATEGORY_RELOAD = "Botao de atualizar";

	// Analytics tracker action
	public final static String ACTION_INFORMAR_PROBLEMA = "Informar problema";
	public final static String ACTION_COMPARTILHAR_APP = "Compartilhar app.";
	public final static String ACTION_AVALIAR = "Avaliar aplicativo";
	public final static String ACTION_RELOAD_HOME = "Twitter da home";

	public static void sendView(String appScreen) {
		if (appScreen != null && appScreen != "") {
			setUpGoogleAnalytics();
			tracker.sendView(appScreen);
			Log.d("Analytics tracker: ", appScreen);
		}
	}

	public static void sendEvent(String category) {
		sendEvent(category, null);
	}

	public static void sendEvent(String category, String action) {
		sendEvent(category, action, null);
	}

	public static void sendEvent(String category, String action, String label) {
		sendEvent(category, action, label, null);
	}

	public static void sendEvent(String category, String action, String label,
			Long value) {
		setUpGoogleAnalytics();
		AnalyticsHelper.tracker.sendEvent(category, action, label, value);

		Log.d("Analytics event category: ", category);
		Log.d("Analytics event action: ", action != null ? action : "");
		Log.d("Analytics event label: ", label != null ? label : "");
	}

	private static void setUpGoogleAnalytics() {
		if (AnalyticsHelper.googleAnalytics == null) {
			AnalyticsHelper.googleAnalytics = GoogleAnalytics
					.getInstance(TransitoRioApplication
							.globalApplicationContext());

			String googleAnalyticsID = ConfigHelper.isProduction ? ANALYTICS_ID_TRACKER_PRD
					: ANALYTICS_ID_TRACKER_DSV;

			AnalyticsHelper.tracker = AnalyticsHelper.googleAnalytics
					.getTracker(googleAnalyticsID);
		}
	}

}
