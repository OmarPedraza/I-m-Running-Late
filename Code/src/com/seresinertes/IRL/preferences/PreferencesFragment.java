//
//  PreferencesFragment.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/24/2013.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.preferences;

import android.os.Bundle;
import android.preference.*;
import com.seresinertes.IRL.R;
import com.seresinertes.IRL.activities.PreferencesActivity;

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

	    final PreferencesHelper preferencesHelper = new PreferencesHelper(getActivity());

	    final EditTextPreference messageText = (EditTextPreference) findPreference("change_text");
	    messageText.setText(preferencesHelper.getText());
		messageText.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				String newText = newValue.toString();
				if (!newText.equalsIgnoreCase(getString(R.string.default_message))) {
					preferencesHelper.setText(newText);
					messageText.setText(preferencesHelper.getText());
				}

				return false;
			}
		});
    }
}
