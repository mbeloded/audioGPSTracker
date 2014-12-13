package com.example.audiogpstracker.utils;

import java.util.LinkedList;
import java.util.List;

import com.example.audiogpstracker.datainterfaces.MessageListener;

public class MessageManager {

	private static MessageManager instance = new MessageManager();
	public static MessageManager get() { return instance;}
	
	private List<MessageListener> listeners;
	
	public MessageManager() {
		listeners = new LinkedList<MessageListener>();
	}
	
	public void displaySpeedMessage(String msg){
		for (MessageListener listener : listeners){
			listener.onDisplaySpeedMessage(msg);
		}
	}
	
	public void displayDirectionMessage(String msg) {
		for (MessageListener listener: listeners) {
			listener.onDisplayDirectionMessage(msg);
		}
	}
	
	public void displayDebugMessage(String debug) {
		for (MessageListener listener : listeners){
			listener.onDisplayDebug(debug);
		}
	}
	
	public void addListener(MessageListener listener){
		listeners.add(listener);
	}
	
	public void removeListener(MessageListener listener){
		listeners.remove(listener);
	}
}
