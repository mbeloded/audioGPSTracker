package com.example.audiogpstracker.data;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.example.audiogpstracker.Constants;
import com.example.audiogpstracker.MainActivity;
import com.example.audiogpstracker.R;
import com.example.audiogpstracker.utils.DmafManager;
import com.example.audiogpstracker.utils.MathUtils;

public class Speed implements LocationListener, Constants {

	private static final String LOG = "Speed";
	private Integer data_points = 2; // how many data points to calculate for
	private Double[][] positions;
	private Long[] times;
	private Integer units; // Preference integers
	private Integer counter = 0;
	private String unit_string;
	private String logStr = null;

	public Speed() {
		// init vars here
		// two arrays for position and time.
		positions = new Double[data_points][2];
		times = new Long[data_points];

		units = R.id.kmph;// by default
		unit_string = MainActivity.getInsatnce().getResources()
				.getString(R.string.title_kmph);
		logStr = "";
	}

	public String getUnitStr() {
		return unit_string;
	}

	public void onLocationChanged(Location loc) {   
		if (loc != null) {
			String speed_string = null;
			Double d1;
			Long t1;
			Double speed = 0.0;
			Double speed2 = 0.0;
			d1 = 0.0;
			t1 = 0l;

			positions[counter][0] = loc.getLatitude();
			positions[counter][1] = loc.getLongitude();
			times[counter] = loc.getTime();

			if (loc.hasSpeed()) {
				speed = loc.getSpeed() * 1.0; // need to * 1.0 to get into a
												// double for some reason...
				logStr = "Method: getSpeed()";
//				displayDebugInfo("Method: getSpeed()");
			} 
//			else {
				try {
					logStr = "Method: formula()";
//					displayDebugInfo("Method: calculate by formula()");
					// get the distance and time between the current position,
					// and the previous position.
					// using (counter - 1) % data_points doesn't wrap properly
					d1 = MathUtils.distance(positions[counter][0], positions[counter][1],
							positions[(counter + (data_points - 1))
									% data_points][0],
							positions[(counter + (data_points - 1))
									% data_points][1]);
					t1 = times[counter]
							- times[(counter + (data_points - 1)) % data_points];
				} catch (NullPointerException e) {
					// all good, just not enough data yet.
					speed_string = "no speed data\nException:"+e.getLocalizedMessage();
				}
				speed2 = d1 / t1; // m/s
				speed2 *= 1000;//WTF?
//			}
			
			counter = (counter + 1) % data_points;

			Log.i(LOG, "speed: "+speed);
			
			// convert from m/s to specified units
			switch (units) {
			case R.id.kmph:
				speed = speed * 3.6d;
				unit_string = MainActivity.getInsatnce().getResources().getString(R.string.title_kmph);
				break;
			case R.id.mph:
				speed = speed * 2.23693629d;
				unit_string = MainActivity.getInsatnce().getResources().getString(R.string.title_mph);
				break;
			case R.id.knots:
				speed = speed * 1.94384449d;
				unit_string = MainActivity.getInsatnce().getResources().getString(R.string.title_knots);
				break;
			}
			
//			String format_string = "%0" + padding + "d";
			
			String value_string = String.format("%.3f", speed.doubleValue());
			String spped2_value = String.format("%.3f", speed2.doubleValue());
			
//	    	String value_string = String.format(format_string, speed.intValue());
			
			speed_string = value_string;
			
			displayText(speed_string);
			displayTextSpeed(spped2_value);
			
			if(DmafManager.getInstance(MainActivity.getInsatnce()).isNeedToPlaySound()) {
				DmafManager.getInstance(MainActivity.getInsatnce()).setSpeed(speed.floatValue());
				logStr += "\nsetSpeed("+speed.floatValue()+")";
			}
			
			displayDebugInfo(logStr);
		} else {
			logStr = "location is null";
	        
	        displayDebugInfo(logStr);
		}
    }

	public void displayText(final String speed) {
		Log.i(LOG, speed);
	}
	
	public void displayTextSpeed(final String speed) {
		Log.i(LOG, speed);
	}

	public void displayDebugInfo(String info) {
		if (ISDEBUG)
			Log.i(LOG, info);
		else
			return;
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.i(LOG, "provider disabled : " + provider);
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.i(LOG, "provider enabled : " + provider);
	}

	private String getStatus(int st) {
		String status = null;
		switch (st) {
		case 0:
			status = "OUT_OF_SERVICE";
			break;
		case 1:
			status = "TEMPORARILY_UNAVAILABLE";
			break;
		case 2:
			status = "AVAILABLE";
			break;
		}
		return status;
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.i(LOG, "status changed : " + extras.toString());
		logStr += "\nstatus changed : " + getStatus(status);

		displayDebugInfo(logStr);
	}

}
