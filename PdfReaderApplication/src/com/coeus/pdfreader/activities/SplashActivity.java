package com.coeus.pdfreader.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.artifex.mupdfdemo.R;

public class SplashActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intentSplash = new Intent(SplashActivity.this,
						MainActivity.class);
				startActivity(intentSplash);
				finish();
			}
		}, 3000);
	}
}
