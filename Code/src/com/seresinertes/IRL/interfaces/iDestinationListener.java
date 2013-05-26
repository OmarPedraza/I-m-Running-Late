//
//  iDestinationListener.java
//  I'm Running Late
//
//  Created by Omar Pedraza on 5/26/13.
//  Copyright (c) 2013 Seres Inertes. All rights reserved.
//

package com.seresinertes.IRL.interfaces;

import com.google.android.gms.maps.model.LatLng;

public interface iDestinationListener {
	public void destinationChanged(LatLng latLng);
	public void destinationNotFound();
}
