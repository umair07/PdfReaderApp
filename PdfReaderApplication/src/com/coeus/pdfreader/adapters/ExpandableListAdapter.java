package com.coeus.pdfreader.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.coeus.pdfreader.R;
import com.coeus.pdfreader.model.PdfFileDataModel;

public class ExpandableListAdapter  extends BaseExpandableListAdapter {

	 public ArrayList<PdfFileDataModel> groupItem;
	 public ArrayList<String> Childtem = new ArrayList<String>();
	 public LayoutInflater minflater;
	 public Activity activity;

	 public ExpandableListAdapter(Activity activity,ArrayList<PdfFileDataModel> grList, ArrayList<String> childItem) {
	  groupItem = grList;
	  this.activity = activity;
	  this.Childtem = childItem;
	  minflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 }

	 public void setInflater(LayoutInflater mInflater, Activity act) {
	  this.minflater = mInflater;
	  activity = act;
	 }

	 @Override
	 public Object getChild(int groupPosition, int childPosition) {
	  return null;
	 }

	 @Override
	 public long getChildId(int groupPosition, int childPosition) {
	  return 0;
	 }

	 @Override
	 public View getChildView(int groupPosition, final int childPosition,
	   boolean isLastChild, View convertView, ViewGroup parent) {
//	  tempChild = (ArrayList<PdfFileDataModel>) Childtem.get(groupPosition);
	  TextView text = null;
	  if (convertView == null) {
	   convertView = minflater.inflate(R.layout.expandable_lv_child, null);
	  }
	  text = (TextView) convertView.findViewById(R.id.txt_Child_Item_Name);
	  text.setClickable(false);
	  text.setText(Childtem.get(childPosition));
	  return convertView;
	 }

	 @Override
	 public int getChildrenCount(int groupPosition) {
	  return Childtem.size();
	 }

	 @Override
	 public Object getGroup(int groupPosition) {
	  return groupPosition;
	 }

	 @Override
	 public int getGroupCount() {
	  return groupItem.size();
	 }

	 @Override
	 public void onGroupCollapsed(int groupPosition) {
	  super.onGroupCollapsed(groupPosition);
	 }

	 @Override
	 public void onGroupExpanded(int groupPosition) {
	  super.onGroupExpanded(groupPosition);
	 }

	 @Override
	 public long getGroupId(int groupPosition) {
	  return 0;
	 }

	 public View getGroupView(int groupPosition, boolean isExpanded,
	   View convertView, ViewGroup parent) {
	  if (convertView == null) {
	   convertView = minflater.inflate(R.layout.expandable_lv_group, null);
	  }
	  TextView text = (TextView) convertView.findViewById(R.id.txt_Group_Item_Name);
	  text.setText(groupItem.get(groupPosition).getBookTitle());
	  text.setClickable(false);
	  if(isExpanded)
	  {
		  
		  Log.e("expand:", "expanded");
	  }
	  return convertView;
	 }

	 @Override
	 public boolean hasStableIds() {
	  return false;
	 }

	 @Override
	 public boolean isChildSelectable(int groupPosition, int childPosition) {
	  return true;
	 }

	}
