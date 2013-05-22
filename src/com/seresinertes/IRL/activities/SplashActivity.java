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
import android.location.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.interfaces.iLocationListener;
import com.seresinertes.IRL.objects.LocationService;
import com.seresinertes.IRL.preferences.PreferencesHelper;

import java.io.IOException;
import java.util.List;

public class SplashActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Context context = getApplicationContext();

		Intent intent = new Intent(context, LocationService.class);
		if (!LocationService.isRunning()) {
			LocationService.registerListener(new iLocationListener() {
				@Override
				public void locationChanged() {
					Intent intent = new Intent(getApplicationContext(), CurrentLocationActivity.class);
					startActivity(intent);
				}
			});

			startService(intent);
		}
		else {
			stopService(intent);
		}
	}
}
