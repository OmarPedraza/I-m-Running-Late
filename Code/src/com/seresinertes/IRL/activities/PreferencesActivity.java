//
//  PreferencesActivity.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/24/2013.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.activities;

import android.app.Activity;
import android.os.Bundle;
import com.seresinertes.IRL.preferences.PreferencesFragment;

public class PreferencesActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
	}
}
