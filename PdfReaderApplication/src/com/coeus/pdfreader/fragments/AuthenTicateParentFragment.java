package com.coeus.pdfreader.fragments;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.coeus.pdfreader.R;

public abstract class AuthenTicateParentFragment extends Fragment {

	public static String methodResponse = "";
	public static Dialog dialog;
	public static Animation ballAnim;
	/****************************************** on Create View *********************************************************/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	/******************************* OnAttach/ToHideKeyBoard *******************************/
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}


	/*********************************************** API Call ******************************************************/

	public class ServiceHandler extends AsyncTask<String, Void, String> {
		boolean showLoader = true;

		public ServiceHandler(boolean showLoader) {
			// TODO Auto-generated constructor stub

			this.showLoader = showLoader;
		}

		public ServiceHandler() {
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (showLoader) {
				showLoading(getActivity());
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String responseData = "";

			try {
				HttpParams httpParameters = new BasicHttpParams();
				int timeoutConnection = 30000;
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				int timeoutSocket = 31000;
				HttpConnectionParams
				.setSoTimeout(httpParameters, timeoutSocket);

				HttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpGet httpGet = new HttpGet(params[0]);
				httpGet.setHeader("Content-type", "application/json");
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(httpGet);
				} catch (Exception e) {
				}

				responseData = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);

				Log.d("Response", "" + responseData);

			} catch (Exception ex) {
				Log.d("EXCEPTION IS", ex.getMessage() + "");
			}
			return responseData;
		}

		@Override
		protected void onPostExecute(String result) {

			if (showLoader) {
				hideLoading(getActivity());
			}

				getAPIResult(result);
		}

	}

	

	/*************************************************************************************************/

	public static void showLoading(final Context context) {

		Handler h = new Handler();
		h.post(new Runnable() {
			public void run() {

				try {
					dialog.dismiss();
					dialog = null;
				} catch (Exception e) {
				}
				try {
					ballAnim = AnimationUtils.loadAnimation(context,
							R.anim.ball_rotatation);
				} catch (Exception e) {
					return;
				}
				dialog = new Dialog(context, android.R.style.Theme_Translucent);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.ball_progress_dialog);

				dialog.setCancelable(false);

				final ImageView ivBall = (ImageView) dialog
						.findViewById(R.id.ivBall);
				ivBall.startAnimation(ballAnim);
				dialog.show();
			}
		});

	}


	public abstract void getAPIResult(String result);

	public static void hideLoading(final Context context) {

		try {
			dialog.dismiss();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}


}
