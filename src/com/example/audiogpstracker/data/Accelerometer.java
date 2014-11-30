package com.example.audiogpstracker.data;

import com.example.audiogpstracker.Constants;
import com.example.audiogpstracker.R;
import com.example.audiogpstracker.utils.MathUtils;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class Accelerometer implements SensorEventListener, Constants {

	private float lastX, lastY, lastZ;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float deltaXMax = 0;
	private float deltaYMax = 0;
	private float deltaZMax = 0;
    
    private long interval = 0L;
    private long lastEvent = 0L;
    
    private float gravity[];
    private float linear_acceleration[];
    
	private final String LOG = "Accelerometer";
	
	public Accelerometer(){
		gravity = new float[3];
		linear_acceleration = new float[3];
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent se) {
		
		// check sensor type
		if(se.sensor.getType()==accSensor){
			lastX = se.values[SensorManager.DATA_X];
		    lastY = se.values[SensorManager.DATA_Y];
		    lastZ = se.values[SensorManager.DATA_Z];
		    
			// clean current values
			displayCleanValues();
			// display the current x,y,z accelerometer values
			displayCurrentValues();
			// display the max x,y,z accelerometer values
			displayMaxValues();
			
		    long now = System.currentTimeMillis();
		    interval = now - lastEvent;
		    lastEvent = now;
	//	    out.write(Float.toString(lastX) + ";" + 
	//	                    Float.toString(lastY) + ";" + 
	//	                    Float.toString(lastZ) + ";" + 
	//	                    Long.toString(interval) + "\n");
		    
		 // get the change of the x,y,z values of the accelerometer
	//		deltaX = lastX;//Math.abs(lastX - se.values[SensorManager.DATA_X]);
	//		deltaY = lastY;//Math.abs(lastY - se.values[SensorManager.DATA_Y]);
	//		deltaZ = lastZ;//Math.abs(lastZ - se.values[SensorManager.DATA_Z]);
	//	
	//		// if the change is below 2, it is just plain noise
	//		if (deltaX < 2)
	//			deltaX = 0;
	//		if (deltaY < 2)
	//			deltaY = 0;
			
			// alpha is calculated as t / (t + dT)
	        // with t, the low-pass filter's time-constant
	        // and dT, the event delivery rate
	
	        final float alpha = 0.8f;
	
	        gravity[SensorManager.DATA_X] = alpha * gravity[SensorManager.DATA_X] + (1 - alpha) * lastX;
	        gravity[SensorManager.DATA_Y] = alpha * gravity[SensorManager.DATA_Y] + (1 - alpha) * lastY;
	        gravity[SensorManager.DATA_Z] = alpha * gravity[SensorManager.DATA_Z] + (1 - alpha) * lastZ;
	
	        linear_acceleration[SensorManager.DATA_X] = lastX - gravity[SensorManager.DATA_X];
	        linear_acceleration[SensorManager.DATA_Y] = lastY - gravity[SensorManager.DATA_Y];
	        linear_acceleration[SensorManager.DATA_Z] = lastZ - gravity[SensorManager.DATA_Z];
	        
			// if the change is below 2, it is just plain noise
			if (linear_acceleration[SensorManager.DATA_X] < 2)
				linear_acceleration[SensorManager.DATA_X] = 0;
			if (linear_acceleration[SensorManager.DATA_Y] < 2)
				linear_acceleration[SensorManager.DATA_Y] = 0;
		}
	}
	
	public void displayText(final String acceleration){
		Log.i(LOG, acceleration);
	}
	
	public void displayCleanValues() {
		displayText("x:0 | y:0 | z:0");
	}

	// display the current x,y,z accelerometer values
	public void displayCurrentValues() {
		
		float[] accel = MathUtils.acceleration(linear_acceleration[0], linear_acceleration[1], linear_acceleration[2], interval);
	
		String value_string = 	"x="+Float.toString(accel[0]) + " | " +
								"y="+Float.toString(accel[1])/* + " | " +
								"z:"+Float.toString(accel[2])*/;
						
		displayText(value_string);
		
	}
	
	// display the max x,y,z accelerometer values
	public void displayMaxValues() {
		if (linear_acceleration[0] > deltaXMax) {
			deltaXMax = linear_acceleration[0];
//			maxX.setText(Float.toString(deltaXMax));
		}
		if (linear_acceleration[1] > deltaYMax) {
			deltaYMax = linear_acceleration[1];
//			maxY.setText(Float.toString(deltaYMax));
		}
		
		displayText("x="+Float.toString(deltaXMax)+" | " +
					"y="+Float.toString(deltaYMax));
//		if (deltaZ > deltaZMax) {
//			deltaZMax = deltaZ;
//			maxZ.setText(Float.toString(deltaZMax));
//		}
	}
}
