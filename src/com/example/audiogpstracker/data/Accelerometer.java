package com.example.audiogpstracker.data;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Accelerometer implements SensorEventListener {

	private float lastX, lastY, lastZ;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    
	private final String LOG = "Accelerometer";
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		
		lastX = se.values[SensorManager.DATA_X];
	    lastY = se.values[SensorManager.DATA_Y];
	    lastZ = se.values[SensorManager.DATA_Z];
	    
		// clean current values
		displayCleanValues();
		// display the current x,y,z accelerometer values
		displayCurrentValues();
		
//	    long now = System.currentTimeMillis();
//	    interval = now - lastEvetn;
//	    lastEvetn = now;
//	    out.write(Float.toString(lastX) + ";" + 
//	                    Float.toString(lastY) + ";" + 
//	                    Float.toString(lastZ) + ";" + 
//	                    Long.toString(interval) + "\n");
	    
	 // get the change of the x,y,z values of the accelerometer
		deltaX = Math.abs(lastX - se.values[SensorManager.DATA_X]);
		deltaY = Math.abs(lastY - se.values[SensorManager.DATA_Y]);
		deltaZ = Math.abs(lastZ - se.values[SensorManager.DATA_Z]);
	
		// if the change is below 2, it is just plain noise
		if (deltaX < 2)
			deltaX = 0;
		if (deltaY < 2)
			deltaY = 0;
	}
	
	public void displayText(final String acceleration){
		Log.i(LOG, acceleration);
	}
	
	public void displayCleanValues() {
		displayText("0.0");
	}

	// display the current x,y,z accelerometer values
	public void displayCurrentValues() {
		
		String value_string = 	Float.toString(deltaX) + "|" +
								Float.toString(deltaY) + "|" +
								Float.toString(deltaZ);
		displayText(value_string);
		
	}

}
