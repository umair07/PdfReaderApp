package com.coeus.pdfreader.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class AppConstants {

	// PDF files Jason Url
	public static String pdfBooksDataJasonUrl = "http://pdfreaderfileserver.comli.com/pdfbookjason.json";
	public static String folderName = "PdfDownloads" ;
	public static String filePath = Environment.getExternalStorageDirectory().toString()+"/"+folderName ;
	public static String subFolderName = "PdfFiles";
	public static String internetConnectedMessage = "Wifi or Mobile data enabled.";
	public static String internetMessage = "Internet disconnected";
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
