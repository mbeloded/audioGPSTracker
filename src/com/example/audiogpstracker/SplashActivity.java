package com.example.audiogpstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.splunk.mint.Mint;

public class SplashActivity extends Activity {
	
	private static Activity instance = null;
	
	private final String STATE_ACTIVITY = "activity_instance";

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		Mint.initAndStartSession(SplashActivity.this, "f72bfede");
		
		setContentView(R.layout.activity_splash);
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				SplashActivity.this.finish();
				
			}
		}, 3000);
		
	}
	
	
}
