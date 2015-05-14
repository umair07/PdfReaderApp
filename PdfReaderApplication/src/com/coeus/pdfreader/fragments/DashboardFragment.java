package com.coeus.pdfreader.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
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

import com.artifex.mupdflib.MuPDFActivity;
import com.coeus.pdfreader.R;
import com.coeus.pdfreader.adapters.CoverFlowAdapter;
import com.coeus.pdfreader.model.PdfFileDataModel;
import com.coeus.pdfreader.servicehandler.ServiceHandler;
import com.coeus.pdfreader.utilities.AppConstants;

@SuppressLint("NewApi")
public class DashboardFragment extends Fragment implements OnClickListener
{
	View rootView;
	private FancyCoverFlow fancyCoverFlow;
	Button btnDashboardOpenFile;
	ArrayList<PdfFileDataModel> pdfFileDetailList;
	String[] fileList;
	private int coverNumber = 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
		pdfFileDetailList =  new ArrayList<PdfFileDataModel>();
		loadUIComponents();
		registerClickListners();
		fixNetworkRestrictions();



		pdfBooksDataApiCall();
		getPdfFilesList();
		copyAssets();
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



	private void getPdfFilesList()
	{
		AssetManager assetManager = getActivity().getAssets();
		try {
			fileList = assetManager.list("pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void setCoverFlow()
	{
		this.fancyCoverFlow.setAdapter(new CoverFlowAdapter());
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
	}
	private void loadUIComponents() {
		btnDashboardOpenFile = (Button) rootView.findViewById(R.id.btnOpenFile);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.btnOpenFile:
			String DestinationFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mypdf/";
			Uri filePathUri = Uri.parse("file://"+DestinationFile +fileList[coverNumber].toString());
			Intent intent = new Intent(getActivity(),MuPDFActivity.class);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(filePathUri);
			startActivity(intent);

			break;

		}

	}

	private void copyAssets() {
		AssetManager assetManager = getActivity().getAssets();
		String[] files = null;
		try {
			files = assetManager.list("pdf");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for(int i=0;i<files.length;i++)
		{
			Context Context = getActivity();
			String DestinationFile = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mypdf/";
			File file = new File(DestinationFile);
			//			String DestinationFile = Context.getFilesDir().getPath() + File.separator + "mypdf/";
			if (!file.exists()) {
				file.mkdir();

			}

			try {

				CopyFromAssetsToStorage(Context, "pdf/" +files[i], DestinationFile+files[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


		}
	}


	private void CopyFromAssetsToStorage(Context Context, String SourceFile, String DestinationFile) throws IOException {
		InputStream IS = Context.getAssets().open(SourceFile);
		OutputStream OS = new FileOutputStream(DestinationFile);
		CopyStream(IS, OS);
		OS.flush();
		OS.close();
		IS.close();
	}
	private void CopyStream(InputStream Input, OutputStream Output) throws IOException {
		byte[] buffer = new byte[5120];
		int length = Input.read(buffer);
		while (length > 0) {
			Output.write(buffer, 0, length);
			length = Input.read(buffer);
		}

	}

}
