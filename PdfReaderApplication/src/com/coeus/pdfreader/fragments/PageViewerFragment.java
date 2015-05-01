package com.coeus.pdfreader.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.artifex.mupdfdemo.R;

public class PageViewerFragment  extends Fragment implements OnClickListener
{

	View rootView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_pageviewer, container, false);
		loadUIComponents();
		registerClickListners();
		return rootView;

	}
	private void registerClickListners() {

		rootView.setOnClickListener(this);
	}
	private void loadUIComponents() {

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		//		case R.id.abc:
		//			
		//			break;

		}

	}
}