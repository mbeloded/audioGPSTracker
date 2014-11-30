package com.example.audiogpstracker.fragments;

import com.example.audiogpstracker.R;
import com.example.audiogpstracker.data.OnDataPass;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FirstFragment extends Fragment {
	
	public TextView 		speedField;
	public TextView 		acceleration;
	public TextView			direction;
	public RelativeLayout 	firstFragm;
	
	public FirstFragment() {
	}
	
	OnDataPass dataPasser;

	@Override
	public void onAttach(Activity a) {
	    super.onAttach(a);
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
		
		firstFragm = (RelativeLayout) rootView.findViewById(R.id.firstFragm);
		
		return rootView;
	}
	
}
