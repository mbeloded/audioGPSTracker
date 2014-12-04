package com.example.audiogpstracker.data;

import java.util.Timer;
import java.util.TimerTask;

import com.example.audiogpstracker.Constants;
import com.example.audiogpstracker.MainActivity;
import com.example.audiogpstracker.utils.DmafManager;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class Direction implements SensorEventListener, Constants {

	private static final String LOG = "Direction";
	
	private static Direction instance;
	
	private Timer timer;
	
	private float lastDegree = 0.0f;
	
	private float currentDegree = 0.0f;
	private float agoDegree = 0.0f;
	private float averageDegree = 0.0f;
	
	private  Direction() {
		timer = new Timer();
		
		timer.schedule(new LastDegreeTimer(), 0, 250);
		timer.schedule(new CurrentDegreeTimer(), 0, 1);
	}
	
	public Direction getInstance() {
		if (instance == null)
			instance = new Direction();
		return instance;
	}
	
	public void getCompassChange() {
		averageDegree = currentDegree - agoDegree;
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// get the angle around the z-axis rotated
		if(event.sensor.getType()==directionSensor) {
			float degree = Math.round(event.values[0]);
			displayText(Float.toString(degree) + " degrees \nlast: " +
						Float.toString(lastDegree) + " \nCompass change: " +
						Float.toString(degree - lastDegree));
			
			if (degree == 360 && DmafManager.getInstance(MainActivity.getInsatnce()).isNeedToPlaySound())
				DmafManager.getInstance(MainActivity.getInsatnce()).playAudio();
			
			lastDegree = degree;
		}

	}
	
	public void displayText(final String direction) {
    	Log.i(LOG, direction);
    }
	
	
	private class CurrentDegreeTimer extends TimerTask implements SensorEventListener
	{
		private float returnedValue = 0.0f;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType()==directionSensor) {
				returnedValue = Math.round(event.values[0]);
			}		
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
		}

		@Override
		public void run() {
			currentDegree = returnedValue;
		}
		
	}
	
	private class LastDegreeTimer extends TimerTask implements SensorEventListener
	{
		private float returnedValue = 0.0f;

		@Override
		public void onSensorChanged(SensorEvent event) {
			if(event.sensor.getType()==directionSensor) {
				returnedValue = Math.round(event.values[0]);
			}						
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			
			
		}

		@Override
		public void run() {
			agoDegree = returnedValue;
		}		
	}
}
