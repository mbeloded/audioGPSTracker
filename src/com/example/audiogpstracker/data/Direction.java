package com.example.audiogpstracker.data;

import com.example.audiogpstracker.Constants;
import com.example.audiogpstracker.MainActivity;
import com.example.audiogpstracker.utils.DmafManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Direction implements SensorEventListener, Constants {

	private static final String LOG = "Direction";
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// get the angle around the z-axis rotated
		if(event.sensor.getType()==directionSensor){
			float degree = Math.round(event.values[0]);
			displayText(Float.toString(degree) + " degrees");
			
			if (Math.round(event.values[0]) == 360 && DmafManager.getInstance(MainActivity.getInstnce()).isNeedToPlaySound())
				DmafManager.getInstance(MainActivity.getInstnce()).playAudio();
		}

	}
	
	public void displayText(final String direction) {
    	Log.i(LOG, direction);
    }
	
}
