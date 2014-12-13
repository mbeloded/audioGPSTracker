package com.example.audiogpstracker.datainterfaces;

public interface MessageListener {
	public void onDisplaySpeedMessage(String msg);
	public void onDisplayDirectionMessage(String msg);
	public void onDisplayDebug(String msg);
}
