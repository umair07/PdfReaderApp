package com.coeus.pdfreader.fragments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;

import com.coeus.pdfreader.MainActivity;
import com.coeus.pdfreader.R;
import com.coeus.pdfreader.adapters.CoverFlowAdapter;
import com.coeus.pdfreader.model.PdfFileDataModel;
import com.coeus.pdfreader.ormlite.BookmarksORM;
import com.coeus.pdfreader.ormlite.DatabaseHelper;
import com.coeus.pdfreader.servicehandler.ServiceHandler;
import com.coeus.pdfreader.utilities.AppConstants;
import com.coeus.pdfreader.utilities.DownloadZipFileUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

@SuppressLint("NewApi")
public class DashboardFragment extends Fragment implements OnClickListener
{
	View rootView;
	private FancyCoverFlow fancyCoverFlow;
	Button btnDashboardOpenFile,btnDashboardOpenBookmarkList;
	ArrayList<PdfFileDataModel> pdfFileDetailList;
	String[] fileList;
	private int coverNumber = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
		pdfFileDetailList =  new ArrayList<PdfFileDataModel>();
		loadUIComponents();
		
		registerClickListners();
		fixNetworkRestrictions();
		pdfBooksDataApiCall();
		setCoverFlow();

		return rootView;

	}

	private void fixNetworkRestrictions()
	{
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}
	private void pdfBooksDataApiCall() {

		ServiceHandler serviceHandler;
		String jasonResponse = null;
		try{
			serviceHandler = new ServiceHandler();
			if(AppConstants.isConnectingToInternet(getActivity()))
			{
				jasonResponse = serviceHandler.makeServiceCall(
						AppConstants.pdfBooksDataJasonUrl,
						ServiceHandler.GET);
				JSONArray jasonPdfArray = new JSONArray(jasonResponse);
				
				for (int j = 0; j < jasonPdfArray.length(); j++) {
					
					pdfFileDetailList.add(new PdfFileDataModel( new JSONObject(
							jasonPdfArray.getString(j))));
				}
				Log.e("Exception in APi Call",""+jasonResponse);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Exception in APi Call",""+e.getStackTrace());

		}
	}

	private void setCoverFlow()
	{
		this.fancyCoverFlow.setAdapter(new CoverFlowAdapter(pdfFileDetailList,getActivity()));
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

				coverNumber =position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				coverNumber =0;
			}
		});
	}
	private void registerClickListners() {

		rootView.setOnClickListener(this);
		btnDashboardOpenFile.setOnClickListener(this);
		btnDashboardOpenBookmarkList.setOnClickListener(this);
	}
	private void loadUIComponents() {
		btnDashboardOpenFile = (Button) rootView.findViewById(R.id.btnOpenFile);
		btnDashboardOpenBookmarkList = (Button) rootView.findViewById(R.id.btnBookmarks);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnOpenFile:
			 DownloadZipFileUtil.createDir(Environment.getExternalStorageDirectory().toString(),AppConstants.folderName);
			 DownloadZipFileUtil.createDir(AppConstants.filePath, AppConstants.subFolderName);
	         String unzipLocation = AppConstants.filePath+"/"+AppConstants.subFolderName+"/";
	         String zipFile =AppConstants.filePath+"/"+AppConstants.subFolderName+"/"+pdfFileDetailList.get(coverNumber).getPdfFileName();
	         try {
	             new DownloadZipFileUtil().downloadEventData(getActivity(),zipFile, unzipLocation, pdfFileDetailList.get(coverNumber).getBookUrl());
	         } catch (Exception e) {
	             // TODO Auto-generated catch block
	             e.printStackTrace();
	         }
			

			break;
			
		case R.id.btnBookmarks:
			try {
				bookMarksLsit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			break;
		}

	}

	private void bookMarksLsit()
	{
		Dao<BookmarksORM, Integer> bookmarkDao = null;
		DatabaseHelper dbHelper = OpenHelperManager
				.getHelper(getActivity(),
						DatabaseHelper.class);
		try {
			bookmarkDao= dbHelper
					.getBookmarkDao();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			List<BookmarksORM> bookmarks = bookmarkDao.queryForAll();
			ArrayList<String> bookmarksName  =  new ArrayList<String>();
			
			for (int i = 0; i < bookmarks.size(); i++) {
				
				bookmarksName.add(bookmarks.get(i).getBookmarkPageNum());
				Log.e("", "Pages: "+bookmarks.get(i).getBookmarkPageNum());
			}
		
			MainActivity.changeFragmentListener.changeFramgent(new BookmarksListFragment(pdfFileDetailList,bookmarksName), true);
			bookmarkDao.closeLastIterator();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
