package com.example.audiogpstracker.utils;

public class MathUtils {

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
	    // haversine great circle distance approximation, returns meters
	    double theta = lon1 - lon2;
	    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	    dist = Math.acos(dist);
	    dist = rad2deg(dist);
	    dist = dist * 60; // 60 nautical miles per degree of seperation
	    dist = dist * 1852; // 1852 meters per nautical mile  
	    return (dist);
    }

    private static double deg2rad(double deg) {
    	return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
    	return (rad * 180.0 / Math.PI);
    }
    
    public static float[] acceleration(float dX, float dY, float dZ, long interval){
    	float acceleration[] = new float[3];
    	
    	//a=delta(velocity)/interval;
    	acceleration[0] = dX/interval;
    	acceleration[1] = dY/interval;
    	acceleration[2] = dZ/interval;
    	return acceleration;
    }
    
}
