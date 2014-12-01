package com.dinahmoe.dmaf;

public interface IDmafListener 
{
	public void onDmafCallback (String trigger, float time, long params, long args);
}
