package com.coeus.pdfreader.fragments;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.artifex.mupdflib.MuPDFActivity;
import com.coeus.pdfreader.R;
import com.coeus.pdfreader.adapters.CoverFlowAdapter;
import com.coeus.pdfreader.model.PdfFileDataModel;
import com.coeus.pdfreader.ormlite.BookmarksORM;
import com.coeus.pdfreader.ormlite.DatabaseHelper;
import com.coeus.pdfreader.utilities.AppConstants;
import com.coeus.pdfreader.utilities.DownloadZipPdfFileUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

@SuppressLint("NewApi")
public class DashboardFragment extends AuthenTicateParentFragment implements OnClickListener
{
	View rootView;
	private FancyCoverFlow fancyCoverFlow;
	private TextView txtBookTitle;
	Button btnDashboardOpenFile,btnDashboardOpenBookmarkList;
	ArrayList<PdfFileDataModel> arrayListPdfFileDetail;
	private int coverNumber = 0;
	String noInternetMsg = "No Internet Connection";
	String noBookmarkFoundMsg = "No Bookmarks Found";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
		arrayListPdfFileDetail =  new ArrayList<PdfFileDataModel>();
		loadUIComponents();
		registerClickListners();
		rootView.getContext().registerReceiver(checkInternetConnection,
				new IntentFilter("internetMessage"));
		fixNetworkRestrictions();
		pdfBooksDataApiCall();
		
		return rootView;

	}

	private void fixNetworkRestrictions()
	{
		if (android.os.Build.VERSION.SDK_INT > 9) 
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	private void pdfBooksDataApiCall() 
	{
		ServiceHandler serviceHandler;
		try
		{
			serviceHandler = new ServiceHandler();
			if(AppConstants.isConnectingToInternet(getActivity()))
			{
				serviceHandler.execute(AppConstants.pdfBooksDataJasonUrl);
			} 
		} 
		catch (Exception e) 
		{
			Log.e("Exception in APi Call",""+ e.getStackTrace());
		}
	}

	private void setCoverFlow()
	{
		this.fancyCoverFlow.setAdapter(new CoverFlowAdapter(arrayListPdfFileDetail,getActivity()));
		this.fancyCoverFlow.setUnselectedAlpha(1.0f);
		this.fancyCoverFlow.setUnselectedSaturation(0.0f);
		this.fancyCoverFlow.setUnselectedScale(0.5f);
		this.fancyCoverFlow.setSpacing(50);
		this.fancyCoverFlow.setMaxRotation(0);
		this.fancyCoverFlow.setScaleDownGravity(0.2f);
		this.fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
		fancyCoverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				txtBookTitle.setText(arrayListPdfFileDetail.get(position).getBookTitle());
				coverNumber = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

				coverNumber = 0;
			}
		});
	}
	private void registerClickListners() 
	{

		rootView.setOnClickListener(this);
		btnDashboardOpenFile.setOnClickListener(this);
		btnDashboardOpenBookmarkList.setOnClickListener(this);
	}
	private void loadUIComponents() 
	{
		btnDashboardOpenFile = (Button) rootView.findViewById(R.id.btnOpenFile);
		btnDashboardOpenBookmarkList = (Button) rootView.findViewById(R.id.btnBookmarks);
		txtBookTitle = (TextView)rootView.findViewById(R.id.txtBookTitle);
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{

		case R.id.btnOpenFile:
			try
			{
			if(AppConstants.isConnectingToInternet(getActivity()))
			{
			DownloadZipPdfFileUtil.createDir(Environment.getExternalStorageDirectory().toString(),AppConstants.folderName);
			DownloadZipPdfFileUtil.createDir(AppConstants.filePath, AppConstants.subFolderName);
			String unzipFileLocation = AppConstants.filePath+"/"+AppConstants.subFolderName+"/";
			String zipFileLocation = AppConstants.filePath+"/"+AppConstants.subFolderName+"/"+arrayListPdfFileDetail.get(coverNumber).getPdfFileName();


			try 
			{
				File file = new File(unzipFileLocation, arrayListPdfFileDetail.get(coverNumber).getPdfFileName() +".pdf" );
				if (file.exists()) {
					
					String filePath = "file://"+unzipFileLocation+ arrayListPdfFileDetail.get(coverNumber).getPdfFileName() +".pdf";
					try 
					{
						openPdfFile(filePath,0);
					} catch (Exception e) 
					{
						Log.e("Open Pdf File Exception", "" + e.getStackTrace());
					}
					
					
				}
				else
				{
					new DownloadZipPdfFileUtil().downloadEventData(getActivity(),zipFileLocation, 
							unzipFileLocation, arrayListPdfFileDetail.get(coverNumber).getBookUrl());

				}

			} catch (Exception e) 
			{
				Log.e("Open Pdf File Exception", "" + e.getStackTrace());
			}
			}
			else
			{
				Toast.makeText(getActivity(), noInternetMsg , Toast.LENGTH_LONG).show();
			}
			} catch (Exception e) 
			{
				Log.e("Internet Exception", "" + e.getStackTrace());
			}
			
			break;

		case R.id.btnBookmarks:
			try
			{
				showBookMarksListFromDb();
			} catch (Exception e) {
				Log.e("Show Bookmarks DashBorad", "" + e.getStackTrace());
			}
			break;
		}

	}

	private void openPdfFile(String filePath,int pageNum) {
		Uri filePathUri = Uri.parse(filePath);
		Intent intent = new Intent(getActivity(),MuPDFActivity.class);
		if(pageNum!=0)
		{
			intent.putExtra("pageNum", pageNum);
		}
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(filePathUri);
		startActivity(intent);
	}

	private void showBookMarksListFromDb()
	{
		List<BookmarksORM> bookmarksListFromDb = getBookmarkListFromDataBase();
		try
		{
			ArrayList<String> bookmarksNameList  =  new ArrayList<String>();
			for (int i = 0; i < bookmarksListFromDb.size(); i++)
			{
				String[] splitedNamePageNum = bookmarksListFromDb.get(i).getBookmarkPageNum().split("_");
				String bookName = splitedNamePageNum[0] +"_"+splitedNamePageNum[2]; 
				
				bookName = bookName.substring(0,1).toUpperCase() + bookName.substring(1).toLowerCase();
				bookmarksNameList.add(bookName);
				Log.e("", "Pages: "+bookmarksListFromDb.get(i).getBookmarkPageNum());
			}
			if(bookmarksNameList.size()>0)
			{
			showBookMarksList(bookmarksNameList);
			}
			else
			{
				Toast.makeText(getActivity(), noBookmarkFoundMsg, Toast.LENGTH_SHORT).show();
			}

		}
		catch (Exception e) 
		{
			Log.e("showBookMarksListFromDb", "" + e.getStackTrace());
		}
	}

	private List<BookmarksORM> getBookmarkListFromDataBase()
	{
		Dao<BookmarksORM, Integer> bookmarkDao = null;
		List<BookmarksORM> bookmarksListDb = null;
		DatabaseHelper dbHelper = OpenHelperManager
				.getHelper(getActivity(),
						DatabaseHelper.class);
		try 
		{
			bookmarkDao= dbHelper
					.getBookmarkDao();
		} catch (SQLException e) {
			Log.e("getBookmarkListFromDataBase", "" + e.getStackTrace());
		}
		try {
			bookmarksListDb = bookmarkDao.queryForAll();
			bookmarkDao.closeLastIterator();
		} catch (SQLException e) 
		{
			Log.e("getBookmarkListFromDataBase List Exception", "" + e.getStackTrace());
		}
		return bookmarksListDb;
	}
	public void showBookMarksList(final List<String> list_FileName)
	{
		final Dialog dialogBookmarks;
		ListView listViewBookmarks;
		View viewList;
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		viewList = getActivity().getLayoutInflater().inflate(R.layout.listview_bookmarks,null);
		dialogBookmarks = new Dialog(getActivity());
		dialogBookmarks.setTitle("Bookmarks");
		dialogBookmarks.setContentView(viewList);        
		dialogBookmarks.show();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		listViewBookmarks = (ListView) viewList.findViewById(R.id.listviewBookmarks);
		listViewBookmarks.setBackgroundResource(R.color.black);	
		
		ArrayAdapter<String> adapterBookmarkList = (new ArrayAdapter<String>( getActivity(),android.R.layout.simple_list_item_1,list_FileName));
		listViewBookmarks.setAdapter(adapterBookmarkList);
		
		Collections.sort(list_FileName);
		
		listViewBookmarks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				dialogBookmarks.cancel();
				
				String[] splitedNamePageNum = list_FileName.get(position).split("_");
				String bookName = splitedNamePageNum[0].toLowerCase()+"_book.pdf"; 
				
				String pagenum = splitedNamePageNum[1];
				
				String filePath = "file://"+AppConstants.filePath+"/"+AppConstants.subFolderName+"/"+ bookName;
				openPdfFile(filePath,Integer.parseInt(pagenum));

			}
		});
	}

	@Override
	public void getAPIResult(String result) {
		String jasonResponse = result;
		try {
			JSONArray jasonPdfArray = new JSONArray(jasonResponse);

			for (int j = 0; j < jasonPdfArray.length(); j++) {

				arrayListPdfFileDetail.add(new PdfFileDataModel( new JSONObject(
						jasonPdfArray.getString(j))));
			}
			setCoverFlow();
			Log.e("Api Response: ",""+ jasonResponse);
		} catch (Exception e) {
			Log.e("Json Response Exception: ", "" + e.getStackTrace());
		}
		
	}

	private final BroadcastReceiver checkInternetConnection = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try
			{
			String internetStatus = intent.getStringExtra("internetStatus");
			if(internetStatus.equals("0"))
			{
				// show no internet popup
				noInternetDialog(getActivity(),AppConstants.internetMessage);
			}
			else if(internetStatus.equals("1"))
			{
				
				// wifi enabled
				Toast.makeText(context, AppConstants.internetConnectedMessage, Toast.LENGTH_LONG).show();
			}
			else if(internetStatus.equals("2"))
			{
				Toast.makeText(context, AppConstants.internetConnectedMessage, Toast.LENGTH_LONG).show();

				// mobile data enabled
			}
			}
			catch (Exception e) 
			{
				Log.e("Broadcast Receiver Exception: ", "" + e.getStackTrace());
			}
		}
	};

	public void noInternetDialog(final Context context,final String msgString)
	{
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		TextView title = new TextView(context);
		title.setText("Message");
		title.setPadding(10, 10, 10, 10);
		title.setGravity(Gravity.CENTER);
		title.setTextColor(Color.BLACK);
		title.setTextSize(20);
		alertDialog2.setCustomTitle(title);
		TextView msg = new TextView(context);
		msg.setText(msgString);
		msg.setPadding(10, 10, 10, 10);
		msg.setGravity(Gravity.CENTER);
		msg.setTextSize(18);
		msg.setTextColor(Color.BLACK);
		alertDialog2.setView(msg);
		alertDialog2.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
				dialog.cancel();
			}
		});
		alertDialog2.show();
	}
}
