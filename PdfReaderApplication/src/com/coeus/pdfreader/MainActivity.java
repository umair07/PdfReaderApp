package com.coeus.pdfreader;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.coeus.pdfreader.fragments.DashboardFragment;
import com.coeus.pdfreader.listener.ChangeFragmentListener;

public class MainActivity extends Activity implements
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
		FragmentManager mngr = getFragmentManager();
		FragmentTransaction ft = mngr.beginTransaction();
		if (addToBackStack) {
			ft.addToBackStack(null);
		}
		currentFragment = fragment;
		ft.add(R.id.fragment_view, currentFragment).commitAllowingStateLoss();
		getFragmentManager().executePendingTransactions();

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			FragmentManager fm = getFragmentManager();

			if (fm.getBackStackEntryCount() == 0) {
				MainActivity.this.finish();
			}

		} catch (Exception e) {
		}
	}
	@Override
	public void changeFramgent(Fragment fragment, boolean addToBackStack) {
		replaceFramgment(fragment, addToBackStack);
	}
	
}
