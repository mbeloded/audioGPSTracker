package com.example.audiogpstracker.fragments;

import com.example.audiogpstracker.R;
import com.example.audiogpstracker.data.Accelerometer;
import com.example.audiogpstracker.data.OnDataPass;
import com.example.audiogpstracker.utils.DmafManager;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
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

public class FirstFragment extends Fragment implements OnCheckedChangeListener {
	
	private Handler mHandler = null;
	
	public TextView 		speedField;
	public TextView 		acceleration;
	public TextView			direction;
	public RelativeLayout 	firstFragm;
	public ToggleButton 	toogleButton;
	public Button			clearAccBtn;
	
	public FirstFragment() {
		
	}
	
	OnDataPass dataPasser;

	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
	    mHandler = new Handler();
//	    dataPasser = (OnDataPass) a;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		speedField 		= (TextView) rootView.findViewById(R.id.speed);
		acceleration 	= (TextView) rootView.findViewById(R.id.acceleration);
		direction 		= (TextView) rootView.findViewById(R.id.direction);
		toogleButton	= (ToggleButton) rootView.findViewById(R.id.dmafSnd);
		clearAccBtn		= (Button)	rootView.findViewById(R.id.clearAcc);
		
		firstFragm = (RelativeLayout) rootView.findViewById(R.id.firstFragm);
		
		toogleButton.setOnCheckedChangeListener(this);
		toogleButton.setChecked(true);
		
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				clearAcc_OnClick();
				
			}
		};
		
		clearAccBtn.setOnClickListener(listener);
		
		return rootView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
//			acceleration.setText("x=0 | y=0");
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
		mHandler.post(new Runnable() {			
			@Override
			public void run() {
				Accelerometer.getInstance().resetAcceleration();
				acceleration.setText(getResources().getString(R.string.acc_speed) +
						" " + "x=0 | y=0");				
			}
		});
		
	}
	
}
