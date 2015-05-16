package com.coeus.pdfreader.fragments;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.artifex.mupdflib.MuPDFActivity;
import com.coeus.pdfreader.MainActivity;
import com.coeus.pdfreader.R;
import com.coeus.pdfreader.adapters.CoverFlowAdapter;
import com.coeus.pdfreader.model.PdfFileDataModel;
import com.coeus.pdfreader.ormlite.BookmarksORM;
import com.coeus.pdfreader.ormlite.DatabaseHelper;
import com.coeus.pdfreader.servicehandler.ServiceHandler;
import com.coeus.pdfreader.utilities.AppConstants;
import com.coeus.pdfreader.utilities.DownloadZipPdfFileUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

@SuppressLint("NewApi")
public class DashboardFragment extends Fragment implements OnClickListener
{
	View rootView;
	private FancyCoverFlow fancyCoverFlow;
	Button btnDashboardOpenFile,btnDashboardOpenBookmarkList;
	ArrayList<PdfFileDataModel> arrayListPdfFileDetail;
	private int coverNumber = 0;
	String noBoommarkFoundMsg = "No Bookmarks Found";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
		arrayListPdfFileDetail =  new ArrayList<PdfFileDataModel>();
		loadUIComponents();
		registerClickListners();
		fixNetworkRestrictions();
		pdfBooksDataApiCall();
		setCoverFlow();
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
		String jasonResponse = null;
		try
		{
			serviceHandler = new ServiceHandler();
			if(AppConstants.isConnectingToInternet(getActivity()))
			{
				jasonResponse = serviceHandler.makeServiceCall(
						AppConstants.pdfBooksDataJasonUrl,
						ServiceHandler.GET);
				JSONArray jasonPdfArray = new JSONArray(jasonResponse);

				for (int j = 0; j < jasonPdfArray.length(); j++) {

					arrayListPdfFileDetail.add(new PdfFileDataModel( new JSONObject(
							jasonPdfArray.getString(j))));
				}
				Log.e("Api Response: ",""+ jasonResponse);
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
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{

		case R.id.btnOpenFile:
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
				bookmarksNameList.add(bookmarksListFromDb.get(i).getBookmarkPageNum());
				Log.e("", "Pages: "+bookmarksListFromDb.get(i).getBookmarkPageNum());
			}
			if(bookmarksNameList.size()>0)
			{
			showBookMarksList(bookmarksNameList);
			}
			else
			{
				Toast.makeText(getActivity(), noBoommarkFoundMsg, Toast.LENGTH_SHORT).show();
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
				String bookName = splitedNamePageNum[0] +"_"+splitedNamePageNum[1]; 
				
				String pagenum = splitedNamePageNum[2];
				
				String filePath = "file://"+AppConstants.filePath+"/"+AppConstants.subFolderName+"/"+ bookName;
				openPdfFile(filePath,Integer.parseInt(pagenum));

			}
		});
	}

	
}
