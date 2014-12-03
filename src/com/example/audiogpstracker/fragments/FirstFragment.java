package com.example.audiogpstracker.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.audiogpstracker.R;
import com.example.audiogpstracker.data.Accelerometer;
import com.example.audiogpstracker.utils.DmafManager;

public class FirstFragment extends Fragment implements OnCheckedChangeListener {
	
	private Handler mHandler = null;
	
	public TextView 		speedField;
	public TextView 		acceleration;
	public TextView			direction;
	public RelativeLayout 	firstFragm;
	public ToggleButton 	toogleButton;
	public Button			clearAccBtn;
	
	public TextView 		mDebugTextView;

	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
	    if(this.mHandler == null)
	    	this.mHandler = new Handler();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setRetainInstance(true);
		
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		this.speedField 		= (TextView) rootView.findViewById(R.id.speed);
		this.acceleration 	= (TextView) rootView.findViewById(R.id.acceleration);
		this.direction 		= (TextView) rootView.findViewById(R.id.direction);
		this.toogleButton	= (ToggleButton) rootView.findViewById(R.id.dmafSnd);
		this.clearAccBtn		= (Button)	rootView.findViewById(R.id.clearAcc);
		
		mDebugTextView = (TextView) rootView.findViewById(R.id.debug_tv);
		
		this.firstFragm = (RelativeLayout) rootView.findViewById(R.id.firstFragm);
		
		this.toogleButton.setOnCheckedChangeListener(this);
		this.toogleButton.setChecked(true);
		
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearAcc_OnClick();	
			}
		};
		
		this.clearAccBtn.setOnClickListener(listener);
		
		return rootView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			DmafManager.getInstance(getActivity()).setPlaySound(true);
			DmafManager.getInstance(getActivity()).init();
			Toast.makeText(getActivity(), "Sound will start play on 360 degree value", Toast.LENGTH_SHORT).show();
		} else {
			DmafManager.getInstance(getActivity()).stopPlaying();
			DmafManager.getInstance(getActivity()).setPlaySound(false);
			Toast.makeText(getActivity(), "Souds are off", Toast.LENGTH_SHORT).show();
		}
		
	}

	public void clearAcc_OnClick() {
		this.mHandler.post(new Runnable() {			
			@Override
			public void run() {
				Accelerometer.getInstance().resetAcceleration();
				acceleration.setText(getResources().getString(R.string.acc_speed) +
						" " + "x=0 | y=0");				
			}
		});
		
	}
	
}
