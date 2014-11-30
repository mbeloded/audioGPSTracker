package com.example.audiogpstracker;

import com.example.audiogpstracker.fragments.FirstFragment;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class Speed implements LocationListener {
	
	private static final String LOG = "Speed:";
	private Integer data_points = 2; // how many data points to calculate for
    private Double[][] positions;
    private Long[] times;
    private Integer units; // Preference integers
    private Integer counter = 0;
    
	public Speed() {
		//init vars here
	}
    
    public void onLocationChanged(Location loc) {   
		if (loc != null) {
			String speed_string;
			Double d1;
			Long t1;
			Double speed = 0.0;
			d1 = 0.0;
			t1 = 0l;

			positions[counter][0] = loc.getLatitude();
			positions[counter][1] = loc.getLongitude();
			times[counter] = loc.getTime();

			if (loc.hasSpeed()) {
				speed = loc.getSpeed() * 1.0; // need to * 1.0 to get into a
												// double for some reason...
			} else {
				try {
					// get the distance and time between the current position,
					// and the previous position.
					// using (counter - 1) % data_points doesn't wrap properly
					d1 = distance(positions[counter][0], positions[counter][1],
							positions[(counter + (data_points - 1))
									% data_points][0],
							positions[(counter + (data_points - 1))
									% data_points][1]);
					t1 = times[counter]
							- times[(counter + (data_points - 1)) % data_points];
				} catch (NullPointerException e) {
					// all good, just not enough data yet.
				}
				speed = d1 / t1; // m/s
			}
			counter = (counter + 1) % data_points;

			// convert from m/s to specified units
			switch (units) {
			case R.id.kmph:
				speed = speed * 3.6d;
				break;
			case R.id.mph:
				speed = speed * 2.23693629d;
				break;
			case R.id.knots:
				speed = speed * 1.94384449d;
				break;
			}
			displayText(speed.intValue());
		}
    }

    public void displayText(int speed) {
    	Log.i(LOG, "speed: "+speed);
    }
    
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.i(LOG, "provider disabled : " + provider);
    }


    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.i(LOG, "provider enabled : " + provider);
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Log.i(LOG, "status changed : " + extras.toString());
    }
    
    // private functions       
    private double distance(double lat1, double lon1, double lat2, double lon2) {
	    // haversine great circle distance approximation, returns meters
	    double theta = lon1 - lon2;
	    double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	    dist = Math.acos(dist);
	    dist = rad2deg(dist);
	    dist = dist * 60; // 60 nautical miles per degree of seperation
	    dist = dist * 1852; // 1852 meters per nautical mile  
	    return (dist);
    }

    private double deg2rad(double deg) {
    	return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
    	return (rad * 180.0 / Math.PI);
    }               
}
