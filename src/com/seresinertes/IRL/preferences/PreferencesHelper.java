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
import android.location.LocationManager;
import org.json.JSONException;
import org.json.JSONObject;

public class PreferencesHelper {
	private SharedPreferences.Editor mEditor = null;
	private SharedPreferences mPreferences = null;

	public PreferencesHelper(Context context) {
		mPreferences = context.getSharedPreferences("private_preferences", Context.MODE_PRIVATE);
		mEditor = mPreferences.edit();
	}

	/**
	 * Gets location data from Preferences.
	 *
	 * @return a {@link Address} object or null if there is no entry for the key
	 */
	public Location getAddress() {
		try {
			String json = mPreferences.getString("location", null);

			if (json != null) {
				JSONObject jsonObject = new JSONObject(json);

				Location location = new Location(LocationManager.GPS_PROVIDER);
				location.setLatitude(jsonObject.getInt("com.seresinertes.IRL.latitude"));
				location.setLongitude(jsonObject.getInt("com.seresinertes.IRL.longitude"));

				return location;
			}
		} catch (JSONException e) {}

		return null;
	}

	/**
	 * Save an address on Preferences.
	 *
	 * @param address the location for the key
	 *
	 * @return true if saved successfully false otherwise
	 */
	public boolean setAddress(Address address) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(mLatitude, location.getLatitude());
			jsonObject.put(mLongitude, location.getLongitude());

			mEditor.putString("location", jsonObject.toString());
			mEditor.commit();
		} catch (JSONException e) {
			return false;
		}

		return true;
	}

	public String getText() {
		String defaultValue = "";
		if (mPreferences == null) {
			return defaultValue;
		}

		return mPreferences.getString("advice_text", defaultValue);
	}

	public void setText(String text) {
		if (mEditor == null) {
			return;
		}

		mEditor.putString("advice_text", text);
		mEditor.commit();
	}

	public Integer getUnits() {
		if (mPreferences == null) {
			return 0;
		}

		return mPreferences.getInt("units", 0);
	}

	public void setUnits(Integer units) {
		if (mEditor == null) {
			return;
		}

		mEditor.putInt("units", units);
		mEditor.commit();
	}
}
