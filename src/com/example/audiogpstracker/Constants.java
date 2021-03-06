package com.example.audiogpstracker;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;

public interface Constants {
	public static String provider = LocationManager.GPS_PROVIDER;
	public static int timeInterval = 500;// 3000;//in ms
	public static int minDistance = 0;// interval for notifications in meters
	public static int padding = 3;
	public static int accSensor = Sensor.TYPE_ACCELEROMETER;
	public static int directionSensor = Sensor.TYPE_MAGNETIC_FIELD;//Sensor.TYPE_ORIENTATION;
	public static int accSensorRate = SensorManager.SENSOR_DELAY_GAME;
	public static int directionRate = SensorManager.SENSOR_DELAY_GAME;//SensorManager.SENSOR_DELAY_UI;

	public static int splash_time = 3000;
	public static String mint_key = "f72bfede";
	
	public static final boolean ISDEBUG = true;
}
