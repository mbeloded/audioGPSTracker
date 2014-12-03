package com.example.audiogpstracker.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;

import com.example.audiogpstracker.MainActivity;
import com.example.audiogpstracker.R;

public class GPSStateManager {
	private final String LOG_TAG = GPSStateManager.class.getSimpleName();
	
	private Activity mActivity;
	private Handler mHandler;
	private LocationManager mLocationManager;
	
	private static GPSStateManager instance = null;
	
	private GPSStateManager(Activity activity) {
		mActivity = activity;
		mHandler = new Handler();
		mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public static GPSStateManager getInstance(Activity activity) {
		if (instance == null)
			instance = new GPSStateManager(activity);
		return instance;
	}
	
	public boolean checkGPSEnable() {
		if (mLocationManager == null) return false;
		
		if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			return false;
		return true;
	}
	
	public boolean isNetworkEnable() {
		if (mLocationManager == null) return false;
		
		if (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			return false;
		return true;
	}
	
	public void buildGPSEnableDialog() {
		String dialogMessage = mActivity.getResources().getString(R.string.gps_dialog_message);
		String positive_button_text = mActivity.getResources().getString(R.string.gps_dialog_positive_button);
		String negative_button_text = mActivity.getResources().getString(R.string.gps_dialog_negative_button);
		
		
		AlertDialog.Builder gpsDialog = new AlertDialog.Builder(mActivity);
		gpsDialog.setMessage(dialogMessage);
		gpsDialog.setPositiveButton(positive_button_text, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent gpsSettingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				mActivity.startActivityForResult(gpsSettingsIntent, 911);
				
			}
		});
		gpsDialog.setNegativeButton(negative_button_text, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				System.exit(0);				
			}
		});
		
		final AlertDialog alert = gpsDialog.create();
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				alert.show();				
			}
		});		
	}
}
