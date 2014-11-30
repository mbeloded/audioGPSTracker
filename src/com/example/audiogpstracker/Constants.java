package com.example.audiogpstracker;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;

public interface Constants {
	public static String provider 	= LocationManager.GPS_PROVIDER;
	public static int timeInterval 	= 0;//3000;//in ms
	public static int minDistance 	= 0;//interval for notifications in meters
	public static int padding 		= 3;
	public static int accSensor 	= Sensor.TYPE_ACCELEROMETER;
	public static int accSensorRate = SensorManager.SENSOR_DELAY_NORMAL;
}
