//
//  LocationService.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/22/13.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.services;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.interfaces.iLocationListener;
import com.seresinertes.IRL.preferences.PreferencesHelper;

import static android.app.AlertDialog.*;
import static android.provider.Settings.*;

public class LocationService extends Service implements LocationListener {
	private static Boolean mStopped = true;
	private static Context mContext = null;
	private static iLocationListener mListener = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mStopped = false;

		initGPS();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mStopped = true;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mListener != null) {
			mListener.locationChanged(location);
		}
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {
	}

	@Override
	public void onProviderEnabled(String s) {
	}

	@Override
	public void onProviderDisabled(String s) {
	}

	public static Boolean isStopped() {
		return mStopped;
	}

	private void initGPS() {
		Integer minDistance = 50;
		Integer minTime = 30000;

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			showLocationSettingsAlert();
		}
		else {
			Location location = null;
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);

				location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (mListener != null && location != null) {
					mListener.locationChanged(location);
				}
			}
			else {
				showLocationSettingsAlert();
			}

			if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && location == null) {
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);

				location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (mListener != null && location != null) {
					mListener.locationChanged(location);
				}
			}
		}
	}

	public static void registerListener(iLocationListener listener) {
		mListener = listener;
	}

	public static void removeListener() {
		mListener = null;
	}

	public static void setContext(Context context) {
		mContext = context;
	}

	/**
	 * Function to show location settings alert dialog
	 * */
	private void showLocationSettingsAlert() {
		Builder alertDialog = new Builder(mContext);

		// Setting dialog message
		alertDialog.setMessage(R.string.location_settings);

		// Pressing Cancel button
		alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Pressing Settings button
		alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(ACTION_LOCATION_SOURCE_SETTINGS);
				mContext.startActivity(intent);
			}
		});

		// Setting dialog title
		alertDialog.setTitle(R.string.gps_disabled);

		// Showing Alert Message
		alertDialog.show();
	}
}

