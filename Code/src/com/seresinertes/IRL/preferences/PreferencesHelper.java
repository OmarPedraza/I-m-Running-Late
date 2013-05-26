//
//  PreferencesHelper.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/21/2013.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;
import com.google.android.gms.maps.model.LatLng;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.services.LocationService;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class PreferencesHelper {
	private Context mContext = null;
	private SharedPreferences.Editor mEditor = null;
	private SharedPreferences mPreferences = null;

	public PreferencesHelper(Context context) {
		mContext = context;
		mPreferences = mContext.getSharedPreferences("private_preferences", Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	/**
	 * Gets message text from Preferences.
	 *
	 * @return a String or null if there is no entry for the key
	 */
	public String getText() {
		String defaultValue = mContext.getString(R.string.default_message);
		if (mPreferences == null) {
			return defaultValue;
		}

		return mPreferences.getString("message_text", defaultValue);
	}

	/**
	 * Save a the desired message text on Preferences.
	 *
	 * @param text the text for the notification messages
	 */
	public void setText(String text) {
		if (mEditor == null) {
			return;
		}

		mEditor.putString("message_text", text);
		mEditor.commit();
	}
}
