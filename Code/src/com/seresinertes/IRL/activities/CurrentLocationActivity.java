//
//  CurrentLocationActivity.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/22/2013.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.interfaces.iLocationListener;
import com.seresinertes.IRL.services.LocationService;

public class CurrentLocationActivity extends FragmentActivity implements iLocationListener {
	private GoogleMap mMap = null;
	private LatLng mLocation = null;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_location_activity);

		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/handsean.ttf");

		TextView currentLocation = (TextView) findViewById(R.id.current_location);
		currentLocation.setTypeface(typeface);

		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.current_map)).getMap();

		Button startButton = (Button) findViewById(R.id.start_button);
		startButton.setTypeface(typeface);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationService.removeListener();
				LocationService.setContext(null);

				Intent intent = new Intent(getApplicationContext(), DestinationActivity.class);
				intent.putExtra("location", mLocation.latitude + "," + mLocation.longitude);
				startActivity(intent);
			}
		});

		Button preferencesButton = (Button) findViewById(R.id.preferences_button);
		preferencesButton.setTypeface(typeface);
		preferencesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), PreferencesActivity.class);
				startActivity(intent);
			}
		});

		registerLocationService();

		Bundle extras = getIntent().getExtras();
		String[] coordinates = extras.getString("location").split(",");
		Double latitude = Double.parseDouble(coordinates[0]);
		Double longitude = Double.parseDouble(coordinates[1]);

		mLocation = new LatLng(latitude, longitude);
		updateLocation(latitude, longitude);
	}

	@Override
	protected void onResume() {
		super.onResume();

		registerLocationService();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	@Override
	public void locationChanged(Location location) {
		mLocation = new LatLng(location.getLatitude(), location.getLongitude());
		updateLocation(location.getLatitude(), location.getLongitude());
	}

	private void registerLocationService() {
		LocationService.registerListener(this);
		LocationService.setContext(this);
		if (LocationService.isStopped()) {
			Intent intent = new Intent(getApplicationContext(), LocationService.class);
			startService(intent);
		}
	}

	private void updateLocation(Double latitude, Double longitude) {
		if (latitude != 0 && longitude != 0) {
			mMap.clear();

			LatLng location = new LatLng(latitude, longitude);

			BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.icon(bitmapDescriptor);
			markerOptions.position(location);

			mMap.addMarker(markerOptions);
			mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
		}
	}
}
