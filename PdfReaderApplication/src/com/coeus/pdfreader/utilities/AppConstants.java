package com.coeus.pdfreader.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AppConstants {

	// PDF files Jason Url
	public static String pdfBooksDataJasonUrl = "http://pdfreaderfileserver.comli.com/pdfbookjason.json";

	// check connectivity
	public static boolean isConnectingToInternet(Context pContext)
	{
		ConnectivityManager cm =
				(ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
