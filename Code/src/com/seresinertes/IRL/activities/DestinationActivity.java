//
//  DestinationActivity.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/25/13.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.interfaces.iDestinationListener;
import com.seresinertes.IRL.services.DestinationAsyncTask;
import com.seresinertes.IRL.services.LocationService;

public class DestinationActivity extends FragmentActivity implements iDestinationListener {
	private Button mDestinationButton = null;
	private GoogleMap mMap = null;
	private LatLng mDestination = null;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.destination_activity);

		final Context context = this;
		final iDestinationListener listener = this;

		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/handsean.ttf");

		TextView destination = (TextView) findViewById(R.id.destination);
		destination.setTypeface(typeface);

		mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.destination_map)).getMap();

		mDestinationButton = (Button) findViewById(R.id.destination_button);
		mDestinationButton.setTypeface(typeface);
		mDestinationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

				final EditText input = new EditText(context);

				alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
					public void onClick(DialogInterface dialog, int which) {
						String destination = input.getText().toString();
						if (destination != null && !destination.isEmpty()) {
							DestinationAsyncTask destinationAsyncTask = new DestinationAsyncTask(context, listener);
							destinationAsyncTask.execute(destination);
						}
					}
				});
				alertDialog.setTitle(R.string.set_destination);
				alertDialog.setView(input);

				alertDialog.show();
			}
		});

		Button googleButton = (Button) findViewById(R.id.google_button);
		googleButton.setTypeface(typeface);
		googleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationService.removeListener();
				LocationService.setContext(null);

				if (mDestination != null) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + mDestination.latitude + "," + mDestination.longitude));
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				else {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

					alertDialog.setMessage(R.string.destination_not_set_message);
					alertDialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					alertDialog.setTitle(R.string.destination_not_set_title);

					alertDialog.show();
				}
			}
		});

		Button notifyButton = (Button) findViewById(R.id.notify_button);
		notifyButton.setTypeface(typeface);
		notifyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LocationService.removeListener();
				LocationService.setContext(null);

				Intent intent = new Intent(getApplicationContext(), NotifyActivity.class);
				startActivity(intent);
			}
		});

		Bundle extras = getIntent().getExtras();
		String[] coordinates = extras.getString("location").split(",");
		Double latitude = Double.parseDouble(coordinates[0]);
		Double longitude = Double.parseDouble(coordinates[1]);

		addDestinationToMap(new LatLng(latitude, longitude));
	}

	@Override
	public void destinationChanged(LatLng latLng) {
		mDestination = latLng;
		mDestinationButton.setText(R.string.change_destination);
		addDestinationToMap(latLng);
	}

	@Override
	public void destinationNotFound() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(DestinationActivity.this);

		alertDialog.setMessage(R.string.destination_not_found_message);
		alertDialog.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.setTitle(R.string.destination_not_found_title);

		alertDialog.show();
	}

	private void addDestinationToMap(LatLng latLng) {
		mMap.clear();

		BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.icon(bitmapDescriptor);
		markerOptions.position(latLng);

		mMap.addMarker(markerOptions);
		mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	}
}
