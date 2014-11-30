package com.example.audiogpstracker;

import com.example.audiogpstracker.fragments.FirstFragment;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements Constants {
	
	private final String LOG = "MainActivity";
	private FirstFragment first;
	
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;
	
	private LocationManager lm;
	private LocationListener locationListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		init();
		
		if (savedInstanceState == null) {
			fragmentTransaction.add(R.id.container, first).commit();
		}
		
	}
	
	private void init() {
		//init fragments
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		first = new FirstFragment();
		
		// use the LocationManager class to obtain GPS locations
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    
        locationListener = new Speed(){
        	@Override
        	public void displayText(int speed){
        		Log.i(LOG, "speed: "+speed);
        		first.speedField.setText("speed:"+speed);
        	}
        };
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
		
		lm.removeUpdates(locationListener);
	}
	
	@Override
    public void onResume() {
        super.onResume();
        lm.requestLocationUpdates(provider, timeInterval, minDistance, locationListener);
    }

//	@Override
//	public void onDataPass(String data) {
//	    Log.d("LOG","hello " + data);
//	    first.speedField.setText(data);
//	}

}
