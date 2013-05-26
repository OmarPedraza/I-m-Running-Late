//
//  NotifyActivity.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/25/13.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.maps.SupportMapFragment;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.services.LocationService;

import static android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS;

public class NotifyActivity extends Activity {
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_activity);

		final Context context = this;

		Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/handsean.ttf");

		Button phoneButton = (Button) findViewById(R.id.phone_button);
		phoneButton.setTypeface(typeface);
		phoneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		Button smsButton = (Button) findViewById(R.id.sms_button);
		smsButton.setTypeface(typeface);
		smsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		Button mailButton = (Button) findViewById(R.id.mail_button);
		mailButton.setTypeface(typeface);
		mailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});

		Button whatsappButton = (Button) findViewById(R.id.whatsapp_button);
		whatsappButton.setTypeface(typeface);
		whatsappButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					getPackageManager().getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);

					Intent intent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
				catch (PackageManager.NameNotFoundException exception) {
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

					alertDialog.setMessage(R.string.whatsapp_not_found_message);
					alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
					alertDialog.setPositiveButton(R.string.google_play, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							Intent intent;
							try {
								intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.whatsapp"));
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent);
							}
							catch (ActivityNotFoundException exception) {
								intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp"));
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								context.startActivity(intent);
							}
						}
					});
					alertDialog.setTitle(R.string.whatsapp_not_found_title);

					// Showing Alert Message
					alertDialog.show();
				}
			}
		});
	}
}
