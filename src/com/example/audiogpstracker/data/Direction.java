package com.example.audiogpstracker.data;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.audiogpstracker.Constants;
import com.example.audiogpstracker.MainActivity;
import com.example.audiogpstracker.datainterfaces.CompassChangeListener;
import com.example.audiogpstracker.utils.DmafManager;
import com.example.audiogpstracker.utils.MathUtils;
import com.example.audiogpstracker.utils.MessageManager;

public class Direction implements SensorEventListener, CompassChangeListener,
		Constants {

	private static final String LOG = "Direction";

	long currentTime = 0;

	private Timer timer;
	private Timer timer2;

	private float currentDegree = 0.0f;

	private float changeDegree = 0.0f;
	private float lastMeanValue = 0.0f;

	private ArrayList<Float> buffer;

	private MainActivity activity;

	private float[] mGravity = new float[3];
	private float[] mGeomagnetic = new float[3];
	private float azimuth = 0f;

	public Direction(MainActivity _activity) {
		activity = _activity;

		timer = new Timer();
		timer2 = new Timer();

		buffer = new ArrayList<Float>();

		timer.schedule(new LastDegreeTimer(), 0, 1000);
		timer2.schedule(new CompassChangeScedule(this), 0, 250);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		final float alpha = 0.97f;

		synchronized (this) {

			if (event.sensor.getType() == accSensor) {

				mGravity[0] = alpha * mGravity[0] + (1 - alpha)
						* event.values[0];
				mGravity[1] = alpha * mGravity[1] + (1 - alpha)
						* event.values[1];
				mGravity[2] = alpha * mGravity[2] + (1 - alpha)
						* event.values[2];
			}

			if (event.sensor.getType() == directionSensor) {

				mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
						* event.values[0];
				mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
						* event.values[1];
				mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
						* event.values[2];

			}

			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				// Log.d(TAG, "azimuth (rad): " + azimuth);
				azimuth = (float) Math.toDegrees(orientation[0]); // orientation
				azimuth = (azimuth + 360) % 360;
				// Log.d(TAG, "azimuth (deg): " + azimuth);

				synchronized (buffer) {
					buffer.add(azimuth);
				}

			}
		}
	}

	public void displayText(final String direction) {
//		Log.i(LOG, direction);
		MessageManager.get().displayDirectionMessage(direction);
	}

	private class LastDegreeTimer extends TimerTask {
		@Override
		public void run() {
			// Log.i(LOG, "buffer_size_for_1_sec = " + buffer.size());
			synchronized (buffer) {
				buffer.clear();
			}

			/*
			 * If the earlier mean value is between 330-360 or 0-30 then any
			 * value on the other side of that line must have either 360
			 * added/subtracted before calculating the change.
			 */

			lastMeanValue = currentDegree;

		}
	}

	private class CompassChangeScedule extends TimerTask {
		private CompassChangeListener compassListener;

		public CompassChangeScedule(CompassChangeListener compassListener) {
			this.compassListener = compassListener;
		}

		@Override
		public void run() {

			int size = buffer.size();
			if (size != 0) {

				currentDegree = MathUtils.meanValue(buffer, lastMeanValue);

				/*
				 * if(currentDegree>360) { currentDegree -= 360; } else
				 * if(currentDegree<0) { currentDegree += 360; }
				 */

//				Log.i(LOG, "buffer size(" + size + ") -> currentDegree="
//						+ currentDegree);
				
				changeDegree = currentDegree - lastMeanValue;
				
				if (changeDegree >= 180.0f)
					changeDegree -= 360.0f;
				if (changeDegree <= -180)
					changeDegree += 360;

				String value_string = String.format("%.0f", currentDegree)
						+ " degrees\nCompass change: "
						+ String.format("%.0f", changeDegree);

				displayText(value_string);

				this.compassListener.deltaDegreeValue(changeDegree);
			}
		}
	}

	@Override
	public void deltaDegreeValue(float deltaDegree) {
		if (DmafManager.getInstance(activity).isNeedToPlaySound()
				&& (deltaDegree > 1 || deltaDegree < -2)) {
			// Log.i(LOG, "value = " + deltaDegree);
			DmafManager.getInstance(activity).onCompassChangePlay(deltaDegree);
		}

	}
	
	public void destructor() {
		if(timer != null)
			timer.cancel();
		
		if(timer2 != null) 
			timer2.cancel();
		
		if(buffer != null) {
			buffer.clear();
		}
		
		if(mGravity != null)
			mGravity = null;
		
		if(mGeomagnetic != null)
			mGeomagnetic = null;
		
		activity = null;
	}
}
