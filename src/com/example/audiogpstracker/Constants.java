package com.example.audiogpstracker;

import android.location.LocationManager;

public interface Constants {
	public static String provider = LocationManager.GPS_PROVIDER;
	public static int timeInterval = 3000;//in ms
	public static int minDistance = 3;//interval for notifications in meters
}
