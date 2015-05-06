package com.coeus.pdfreader.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import com.artifex.mupdflib.MuPDFActivity;
import com.coeus.pdfreader.R;
import com.coeus.pdfreader.adapters.CoverFlowAdapter;

@SuppressLint("NewApi")
public class DashboardFragment extends Fragment implements OnClickListener
{
	View rootView;
	private FancyCoverFlow fancyCoverFlow;
	Button btnDashboardOpenFile;
	String[] filelist;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
		this.fancyCoverFlow = (FancyCoverFlow) rootView.findViewById(R.id.fancyCoverFlow);
		loadUIComponents();
		registerClickListners();
		this.fancyCoverFlow.setAdapter(new CoverFlowAdapter());
		this.fancyCoverFlow.setUnselectedAlpha(1.0f);
		this.fancyCoverFlow.setUnselectedSaturation(0.0f);
		this.fancyCoverFlow.setUnselectedScale(0.5f);
		this.fancyCoverFlow.setSpacing(50);
		this.fancyCoverFlow.setMaxRotation(0);
		this.fancyCoverFlow.setScaleDownGravity(0.2f);
		this.fancyCoverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
		AssetManager assetManager = getActivity().getAssets();
		try {
			filelist = assetManager.list("pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		copyAssets();
		return rootView;

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
						Uri uri = Uri.parse("file://"+DestinationFile +filelist[1].toString());
						Intent intent = new Intent(getActivity(),MuPDFActivity.class);
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(uri);
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
