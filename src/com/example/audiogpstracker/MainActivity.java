package com.example.audiogpstracker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.audiogpstracker.data.Direction;
import com.example.audiogpstracker.data.Speed;
import com.example.audiogpstracker.fragments.FirstFragment;
import com.example.audiogpstracker.utils.DmafManager;
import com.example.audiogpstracker.utils.GPSStateManager;

public class MainActivity extends FragmentActivity implements Constants {

	private static final String TAG_FIRST_FRAGMENT = "task_fragment";

	private FirstFragment first;

	private LocationManager locationManager;
	private LocationListener locationListener;

	private SensorManager sensorManager;
	private SensorEventListener accListener;
	private SensorEventListener directionListener;
	
	private Sensor accelerometer;
	private Sensor direction;

	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		if (savedInstanceState != null) { // saved instance state, fragment may exist
           // look up the instance that already exists by tag
			first = (FirstFragment)  
              getSupportFragmentManager().findFragmentByTag(TAG_FIRST_FRAGMENT);
        } else if (first == null) { 
           // only create fragment if they haven't been instantiated already
        	first = new FirstFragment();
        }
		
		init();
		
		if (!first.isInLayout()) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, first, TAG_FIRST_FRAGMENT)
                .commit();
        }
	}

	public void init() {

		// keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		if (handler == null ) handler = new Handler();

		// use the LocationManager class to obtain GPS locations

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		locationListener = new Speed(this);

		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		
		if (sensorManager.getDefaultSensor(accSensor) != null) {

			accelerometer = sensorManager.getDefaultSensor(accSensor);

		}

		if (sensorManager.getDefaultSensor(directionSensor) != null) {

			direction = sensorManager.getDefaultSensor(directionSensor);

			directionListener = new Direction(this);
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		DmafManager.getInstance(this).stopPlaying();
		
		unregisterListeners();
		
	}

	@Override
	public void onResume() {
		super.onResume();
		DmafManager.getInstance(this).init();
		registerListeners();
	}

	private void registerListeners() {
		if (GPSStateManager.getInstance(this).checkGPSEnable())
			locationManager.requestLocationUpdates(provider, timeInterval,
					minDistance, locationListener);
		else {
			GPSStateManager.getInstance(this).buildGPSEnableDialog();
		}

		sensorManager.registerListener(directionListener, accelerometer,
				accSensorRate);
		sensorManager.registerListener(directionListener, direction,
				directionRate);
	}

	private void unregisterListeners() {
		// to stop listeners and save battery

		if (GPSStateManager.getInstance(this).checkGPSEnable())
			locationManager.removeUpdates(locationListener);

		sensorManager.unregisterListener(accListener);
		sensorManager.unregisterListener(directionListener);
	}

	public LocationManager getLocationManager() {
		return locationManager;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		DmafManager.getInstance(this).stopPlaying();
		unregisterListeners();
		locationManager = null;
		sensorManager   = null;
		accListener = null;
		
		((Direction)directionListener).destructor();
		((Speed)locationListener).destructor();
		
		directionListener = null;
		locationListener = null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != 0)
			return;

		switch (requestCode) {
		case 911:
			if (GPSStateManager.getInstance(this).checkGPSEnable()) {
				init();
			} else {
				GPSStateManager.getInstance(this).buildGPSEnableDialog();
			}
			break;

		default:
			break;
		}
	}
}
