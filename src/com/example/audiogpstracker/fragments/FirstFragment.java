package com.example.audiogpstracker.fragments;

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

import com.example.audiogpstracker.Constants;
import com.example.audiogpstracker.R;
import com.example.audiogpstracker.data.Accelerometer;
import com.example.audiogpstracker.datainterfaces.MessageListener;
import com.example.audiogpstracker.utils.DmafManager;
import com.example.audiogpstracker.utils.MessageManager;
import com.example.audiogpstracker.utils.PreferencesManager;

public class FirstFragment extends Fragment implements OnCheckedChangeListener, MessageListener, Constants {
	
	private View rootView;

	public TextView speedField;
	public TextView speedSecondField;
	public TextView acceleration;
	public TextView direction;
	public RelativeLayout firstFragm;
	public ToggleButton toogleButton;
	public Button clearAccBtn;
	public TextView satValue;
	public TextView accuracyValue;

	public TextView mDebugTextView;
	private Handler handler;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		this.speedField = (TextView) rootView.findViewById(R.id.speed);
		this.speedSecondField = (TextView) rootView
				.findViewById(R.id.speed2_value);
		this.acceleration = (TextView) rootView.findViewById(R.id.acceleration);
		this.direction = (TextView) rootView.findViewById(R.id.direction);
		this.toogleButton = (ToggleButton) rootView.findViewById(R.id.dmafSnd);
		this.clearAccBtn = (Button) rootView.findViewById(R.id.clearAcc);

		this.mDebugTextView = (TextView) rootView.findViewById(R.id.debug_tv);

		this.firstFragm = (RelativeLayout) rootView
				.findViewById(R.id.firstFragm);

		this.satValue = (TextView) rootView.findViewById(R.id.sat_value);
		this.accuracyValue = (TextView) rootView
				.findViewById(R.id.accuracy_value);

		this.toogleButton.setOnCheckedChangeListener(this);
		this.toogleButton.setChecked(true);

		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearAcc_OnClick();
			}
		};

		this.clearAccBtn.setOnClickListener(listener);
		
		handler = new Handler();

		// Add this fragment as a leek listener so
		// it can update its view accordingly
		MessageManager.get().addListener(this);
		
		return rootView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		MessageManager.get().removeListener(this);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			DmafManager.getInstance(getActivity()).setPlaySound(true);
			if(!DmafManager.getInstance(getActivity()).isInitialized()) {
				DmafManager.getInstance(getActivity()).init();
			}
			Toast.makeText(getActivity(), "Souds are on", Toast.LENGTH_SHORT).show();
		} else {
			DmafManager.getInstance(getActivity()).stopPlaying();
			DmafManager.getInstance(getActivity()).setPlaySound(false);
			Toast.makeText(getActivity(), "Souds are off", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void clearAcc_OnClick() {
		
		if(acceleration != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					Accelerometer.getInstance().resetAcceleration();
					if(isAdded()){
						acceleration.setText(getResources().getString(
							R.string.acc_speed)
							+ " " + "x=0 | y=0");
					}
				}
			});
		}

	}

	private String getUnitString() {
		String unit_string = getResources().getString(R.string.title_kmph);
		switch (PreferencesManager.get().getUnitType()) {
		case R.id.kmph:
			unit_string = getResources().getString(R.string.title_kmph);
			break;
		case R.id.mph:
			unit_string = getResources().getString(R.string.title_mph);
			break;
		case R.id.knots:
			unit_string = getResources().getString(R.string.title_knots);
			break;
		}
		
		return unit_string;
	}
	
	@Override
	public void onDisplayDebug(final String msg) {
		// TODO Auto-generated method stub
		if (mDebugTextView != null) {
					
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (ISDEBUG)
					{
						mDebugTextView.setText(msg);
					}
					else {
						mDebugTextView.setText("");
					}
				}
			});
		}
	}

	@Override
	public void onDisplaySpeedMessage(final String msg) {
		// TODO Auto-generated method stub
		
		if (speedField != null) {
			
			handler.post(new Runnable() {

				@Override
				public void run() {
					if(isAdded()){
						// TODO Auto-generated method stub
						speedField.setText(getResources().getString(
							R.string.gps_speed)
							+ " " + msg + " " + getUnitString());
					}
				}
				
			});
			
		}
	}

	@Override
	public void onDisplayDirectionMessage(final String msg) {
		// TODO Auto-generated method stub
		if (direction != null) {
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					if(isAdded()){
						direction.setText(getResources()
								.getString(R.string.direction)
								+ " "
								+ msg);
					}
				}
			});
		}
	}

}
