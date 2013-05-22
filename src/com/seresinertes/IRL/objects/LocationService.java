//
//  LocationService.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/22/13.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.objects;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;
import com.seresinertes.IRL.interfaces.iLocationListener;
import com.seresinertes.IRL.preferences.PreferencesHelper;

import java.io.IOException;
import java.util.List;

public class LocationService extends Service implements LocationListener {
	private static Boolean mRunning = false;
	private static iLocationListener mListener = null;
	private static LocationManager mLocationManager = null;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mRunning = true;

		initGPS();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mRunning = false;
	}

	@Override
	public ComponentName startService(Intent service) {
		return super.startService(service);
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}

	@Override
	public void onLocationChanged(Location location) {
		Geocoder geocoder = new Geocoder(getApplicationContext());
		try {
			List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
			if (addressList != null && addressList.size() != 0) {
				PreferencesHelper preferencesHelper = new PreferencesHelper(getApplicationContext());
				preferencesHelper.setAddress(addressList.get(0));
			}
		}
		catch (IOException exception) {};

		mListener.locationChanged();
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

	private void initGPS() {
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		mLocationManager.requestLocationUpdates(mLocationManager.getBestProvider(criteria, true), 30000, 15, this);
	}

	public static boolean isRunning() {
		return mRunning;
	}

	public static void registerListener(iLocationListener listener) {
		mListener = listener;
	}
}

