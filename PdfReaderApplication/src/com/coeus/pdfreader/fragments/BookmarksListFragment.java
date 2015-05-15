package com.coeus.pdfreader.fragments;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import com.coeus.pdfreader.R;
import com.coeus.pdfreader.adapters.ExpandableListAdapter;
import com.coeus.pdfreader.model.PdfFileDataModel;

@SuppressLint("NewApi")
public class BookmarksListFragment extends Fragment  implements OnClickListener
{
	View rootView;
	 private ExpandableListView expandableListView_Bookmarks;
	ArrayList<PdfFileDataModel> pdfFileDetailList;
	ArrayList<String> bookmarks;
	public BookmarksListFragment(ArrayList<PdfFileDataModel> pdfFileDetailList,
			ArrayList<String> bookmarks) {
		super();
		this.pdfFileDetailList = pdfFileDetailList;
		this.bookmarks = bookmarks;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_bookmark_list, container, false);
		 loadUIComponents();
		 registerClickListners();
		 ExpandableListAdapter adapter = new ExpandableListAdapter(getActivity(),pdfFileDetailList, bookmarks);
		 expandableListView_Bookmarks.setAdapter(adapter)	;	 
		 expandableListView_Bookmarks.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		 
		return rootView;
	}
	private void registerClickListners() {

		rootView.setOnClickListener(this);
	}
	private void loadUIComponents() {
		
		expandableListView_Bookmarks = (ExpandableListView) rootView.findViewById(R.id.expandableList_bookmarks);
	}
	
	@Override
	public void onClick(View v) {
		
	}
}
