package com.example.audiogpstracker.fragments;

import com.example.audiogpstracker.OnDataPass;
import com.example.audiogpstracker.R;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FirstFragment extends Fragment {
	
	public TextView speedField;
	
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
		
		speedField = (TextView) rootView.findViewById(R.id.speed);
		
		return rootView;
	}
	
}
