//
//  DestinationAsyncTask.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/23/13.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import com.google.android.gms.maps.model.LatLng;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.interfaces.iDestinationListener;
import com.seresinertes.IRL.preferences.PreferencesHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.provider.Settings.ACTION_SETTINGS;

public class DestinationAsyncTask extends AsyncTask<String, Void, LatLng> {
	private Context mContext = null;
	private Exception mException = null;
	private iDestinationListener mListener = null;
	private ProgressDialog mDialog = null;

	public DestinationAsyncTask(Context context, iDestinationListener listener) {
		super();

		mContext = context;
		mListener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		mDialog = new ProgressDialog(mContext);
		mDialog.setTitle(mContext.getResources().getString(R.string.app_name));
		mDialog.setMessage(mContext.getResources().getString(R.string.searching_destination));
		mDialog.show();
	}

	@Override
	protected LatLng doInBackground(String... strings) {
		String address = strings[0];
		try {
			Geocoder geocoder = new Geocoder(mContext);
			List<Address> addresses = geocoder.getFromLocationName(address, 1);
			if (addresses != null && addresses.size() > 0) {
				return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
			}
		}
		catch (IOException ioException) {
			try {
				String urlStr = "https://maps.googleapis.com/maps/api/geocode/json?sensor=true&address=" + java.net.URLEncoder.encode(address, "utf-8");
				URL url = new URL(urlStr);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					builder.append("\n");
				}

				try {
					JSONObject json = new JSONObject(builder.toString());
					if ("OK".equals(json.optString("status"))) {
						JSONArray resultsArray = json.optJSONArray("results");
						if (resultsArray != null) {
							JSONObject geometry = resultsArray.getJSONObject(0).optJSONObject("geometry");
							if (geometry != null) {
								JSONObject location = resultsArray.getJSONObject(0).optJSONObject("location");
								if (location != null) {
									return new LatLng(Double.parseDouble(location.optString("lat")), Double.parseDouble(location.optString("lng")));
								}
							}
						}
					}
				}
				catch (JSONException jsonException) {
					mException = jsonException;
				}
			}
			catch (Exception exception) {
				mException = exception;
			}
		}

		return null;
	}

	@Override
	protected void onPostExecute(LatLng latLng) {
		super.onPostExecute(latLng);

		if (mDialog.isShowing()) {
			mDialog.dismiss();
		}

		if (latLng != null) {
			mListener.destinationChanged(latLng);
		}
		else if (mException != null) {
			if (mException instanceof UnknownHostException) {
				showInternetSettingsAlert();
			}
		}
		else {
			mListener.destinationNotFound();
		}
	}

	/**
	 * Function to show internet settings alert dialog
	 * */
	private void showInternetSettingsAlert() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting dialog title
		alertDialog.setTitle(R.string.internet_disabled);

		// Setting dialog message
		alertDialog.setMessage(R.string.internet_settings);

		// Pressing Settings button
		alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ACTION_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		// Pressing Cancel button
		alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}
