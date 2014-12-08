package com.example.audiogpstracker.utils;

import java.util.ArrayList;
import java.util.Iterator;

import android.util.Log;

public class MathUtils {

	private static final float ALPHA = 0.25f;

	public static double distance(double lat1, double lon1, double lat2,
			double lon2) {
		// haversine great circle distance approximation, returns meters
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
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

	public static float[] acceleration(float dX, float dY, float dZ,
			long interval) {
		float acceleration[] = new float[3];

		// a=delta(velocity)/interval;
		acceleration[0] = dX / interval;
		acceleration[1] = dY / interval;
		acceleration[2] = dZ / interval;
		return acceleration;
	}

	public static float[] lowPass(float[] input, float[] output) {
		if (output == null)
			return input;
		for (int i = 0; i < input.length; i++) {
			output[i] = output[i] + ALPHA * (input[i] - output[i]);
		}
		return output;
	}

	public static float meanValue(ArrayList<Float> inputArr) {

		int sum = 0;
		int length = 0;
		float smoothVal = 0.0f;

		synchronized (inputArr) {
			if (inputArr == null)
				return 0.0f;

			// Get an iterator.
			Iterator<Float> ite = inputArr.iterator();

			while (ite.hasNext()) {
				Float f = ite.next();
				if (f != null && !f.isInfinite() && !f.isNaN()) {
					sum += f;
					length++;
				}
			}

			smoothVal = sum / length;

			if (smoothVal > 360)
				smoothVal = smoothVal - 360.0f;
			if (smoothVal < 0)
				smoothVal = smoothVal + 360.0f;
		}

		return smoothVal;
	}

	public static float meanValue(ArrayList<Float> inputArr, float lastMeanValue) {

		int sum = 0;
		int length = 0;
		float smoothVal = 0.0f;

		synchronized (inputArr) {
			if (inputArr == null)
				return 0.0f;

			// Get an iterator.
			Iterator<Float> ite = inputArr.iterator();

			if (lastMeanValue >= 300.0f && lastMeanValue <= 360.0f) {
				for (int i = 0; i < inputArr.size(); i++) {
					if (inputArr.get(i) >= 0.0f && inputArr.get(i) <= 60.0f) {
						inputArr.set(i, inputArr.get(i) + 360.0f);
//						Log.i("LOG", "need to do +360");
					}						
				}				
			}
			
			if (lastMeanValue >= 0.0f && lastMeanValue <= 60.0f) {
				for (int i = 0; i < inputArr.size(); i++) {
					if (inputArr.get(i) >= 300.0f && inputArr.get(i) <= 360.0f) {
						inputArr.set(i, inputArr.get(i) - 360.0f);
//						Log.i("LOG", "need to do -360");
					}
						
				}
				
			}

			while (ite.hasNext()) {
				Float f = ite.next();
				if (f != null && !f.isInfinite() && !f.isNaN()) {
					sum += f;
					length++;
				}
			}

			smoothVal = sum / length;
			Log.i("LOG", "newMeanValue = " + smoothVal);

			if (smoothVal > 360)
				smoothVal = smoothVal - 360.0f;
			if (smoothVal < 0)
				smoothVal = smoothVal + 360.0f;
		}

		return smoothVal;
	}

}
