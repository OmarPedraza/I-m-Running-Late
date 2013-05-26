//
//  SplashActivity.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/21/2013.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.interfaces.iLocationListener;
import com.seresinertes.IRL.services.LocationService;

public class SplashActivity extends Activity implements iLocationListener {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);

		if (LocationService.isStopped()) {
			LocationService.registerListener(this);
			LocationService.setContext(this);

			Intent intent = new Intent(getApplicationContext(), LocationService.class);
			startService(intent);
		}
	}

	@Override
	public void locationChanged(Location location) {
		LocationService.removeListener();
		LocationService.setContext(null);

		final String coordinates = location.getLatitude() + "," + location.getLongitude();

		Runnable goToMainMenu = new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(getApplicationContext(), CurrentLocationActivity.class);
				intent.putExtra("location", coordinates);
				startActivity(intent);
			}
		};

		// Keep the splash screen a little bit more
		Handler handler = new Handler();
		handler.postDelayed(goToMainMenu, 1000);
	}
}
