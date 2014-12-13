package com.example.audiogpstracker.utils;

import com.example.audiogpstracker.R;

public class PreferencesManager {

	private static PreferencesManager instance = new PreferencesManager();
	public static PreferencesManager get() {return instance;}
	
	public int getUnitType() {
		return R.id.kmph;
	}
}
