package com.example.audiogpstracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.audiogpstracker.data.Accelerometer;
import com.example.audiogpstracker.data.Direction;
import com.example.audiogpstracker.data.Speed;
import com.example.audiogpstracker.fragments.FirstFragment;
import com.example.audiogpstracker.utils.DmafManager;

public class MainActivity extends FragmentActivity implements Constants {
	
	private final String LOG = "MainActivity";
	private static final String TAG_FIRST_FRAGMENT = "task_fragment";
	
	private static MainActivity instance = null;
	
	private FirstFragment first;
	
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
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
		
		init();
		
		instance = this;
		
	}
	
	private void init() {
		
        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		//init fragments
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		first = (FirstFragment) fragmentManager.findFragmentByTag(TAG_FIRST_FRAGMENT);

	    // If the Fragment is non-null, then it is being retained
	    // over a configuration change.
	    if (first == null) {
	    	first = new FirstFragment();
			fragmentTransaction.add(R.id.container, first, TAG_FIRST_FRAGMENT).commit();
	    }
		
		handler = new Handler();
		
		// use the LocationManager class to obtain GPS locations
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
        locationListener = new Speed(){
        	@Override
        	public void displayText(final String speed){
//        		Log.i(LOG, speed);
        		handler.post(new Runnable(){
        			public void run(){
        				if(first.speedField!=null){
        					first.speedField.setText(getResources().getString(R.string.gps_speed) +
	        						" " + speed);
        				}
        			}
        		});
        		
        	}
        };
        
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(accSensor) != null) {

        	accelerometer = sensorManager.getDefaultSensor(accSensor);
        	
	        accListener = new Accelerometer(){
	        	@Override
	        	public void displayText(final String acc){
//	        		Log.i(LOG, acc);
	        		handler.post(new Runnable(){
	        			public void run(){
	        				if(first.acceleration!=null) {
	        					first.acceleration.setText(getResources().getString(R.string.acc_speed) +
	        						" " + acc);
	        				}
	        			}
	        		});
	        	}
	        };
	        
	        sensorManager.registerListener(accListener, accelerometer, accSensorRate);
        }
        else {
        	handler.post(new Runnable(){
    			public void run(){
    				if(first.acceleration!=null){
    					first.acceleration.setText(R.string.no_accelerometer);
    				}
    			}
        	});
        }
        
        if (sensorManager.getDefaultSensor(directionSensor) != null) {
        	
        	direction = sensorManager.getDefaultSensor(directionSensor);
        	
	        directionListener = new Direction(){
	        	@Override
	        	public void displayText(final String direction) {
//	        		Log.i(LOG, direction);
	        		handler.post(new Runnable(){
	        			public void run(){
	        				if(first.direction!=null){
	        					first.direction.setText(getResources().getString(R.string.direction) +
	        						" " + direction);
	        				}
	        			}
	        		});
	        	}
	        };
        } else {
        	handler.post(new Runnable(){
    			public void run(){
    				if(first.direction!=null){
    					first.direction.setText(R.string.no_direction);
    				}
    			}
        	});
        }
        
        DmafManager.getInstance(this).init();
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
		// TODO Auto-generated method stub
		super.onPause();
		
		unregisterListeners();
	}
	
	@Override
    public void onResume() {
        super.onResume();
        registerListeners();
    }
	
	private void registerListeners() {
		locationManager.requestLocationUpdates(provider, timeInterval, minDistance, locationListener);
        sensorManager.registerListener(accListener, accelerometer, accSensorRate);
     // for the system's orientation sensor registered listeners
        sensorManager.registerListener(directionListener, direction, directionRate);

	}
	
	private void unregisterListeners() {
		locationManager.removeUpdates(locationListener);
		sensorManager.unregisterListener(accListener);
		// to stop the listener and save battery
		sensorManager.unregisterListener(directionListener);
	}
	
	public static MainActivity getInstnce() {
		if (instance != null) 
			return instance;
		return null;
	}

//	@Override
//	public void onDataPass(String data) {
//	    Log.d("LOG","hello " + data);
//	    first.speedField.setText(data);
//	}

}
