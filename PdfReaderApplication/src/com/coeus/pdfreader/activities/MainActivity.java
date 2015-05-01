package com.coeus.pdfreader.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.artifex.mupdfdemo.R;
import com.coeus.pdfreader.fragments.DashboardFragment;
import com.coeus.pdfreader.listener.ChangeFragmentListener;

public class MainActivity extends FragmentActivity implements
ChangeFragmentListener
{
	public static ChangeFragmentListener changeFragmentListener;
	private Fragment currentFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		changeFragmentListener = this;
		replaceFramgment(new DashboardFragment(), true);
	}

	public void replaceFramgment(Fragment fragment, boolean addToBackStack) {
		FragmentManager mngr = getSupportFragmentManager();
		FragmentTransaction ft = mngr.beginTransaction();
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		currentFragment = fragment;
		ft.add(R.id.fragment_view, currentFragment).commitAllowingStateLoss();
		getSupportFragmentManager().executePendingTransactions();

	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			FragmentManager fm = getSupportFragmentManager();

			if (fm.getBackStackEntryCount() == 0) {
				MainActivity.this.finish();
			}

		} catch (Exception e) {
		}
	}
	@Override
	public void changeFramgent(Fragment fragment, boolean addToBackStack) {
		
	}
	
}
