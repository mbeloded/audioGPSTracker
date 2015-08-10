package com.dinahmoe.dmaf;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.res.AssetManager;
import android.os.Environment;

public class Dmaf 
{
	private static HashMap<String, IDmafListener> callbacks;
	
	private long dmafPointer; 
	private AssetManager currManager;
	private float[] floatData;
	private int i;
	private boolean initialized;
	
	public boolean isInitialized()
	{
		return initialized;
	}
	
	public void setAssetManager(AssetManager manager)
	{
		this.currManager = manager;
	}
	
	static 
	{
		System.loadLibrary("DmafAndroid");
	}
	
	// Native methods
	
	private native long DmAlloc();
	private native boolean DmInitialize(long dmafPtr_, String settingsFilepath_);
	private native void DmDeinitialize(long dmafPtr_);
	private native float DmGetCurrentTime(long dmafPtr_);
	private native void DmTell(long dmafPtr_, String trigger, long parameters_);
	private native void DmTellTime(long dmafPtr_, String trigger, float time_, long parameters_);
	private native void DmLog(long dmafPtr_, String file_, int line_, String message_);
	private native void DmProcess(long dmafPtr_, float numSamples_, int channels_, float[] data_);
	private native void DmAddListener(long dmafPtr_, String trigger_, String methodName_, long args_);
	
	// end Native methods
	
	// constructors
	
	public Dmaf()
	{
		
	}
	
	//end constructors
	
	// generic callback
	public void onCallback(String trigger, float time, long params, long args)
	{
		if(callbacks.containsKey(trigger));
			callbacks.get(trigger).onDmafCallback(trigger, time, params, args);
	}
	
	// public methods
	
	public boolean initialize()
	{
		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		try 
		{
			ZipInputStream zis = new ZipInputStream(currManager.open("demo.zip"));
			ZipEntry entry = null;
			byte data[] = null;
			FileOutputStream fos = null;
			BufferedOutputStream dest = null;
			File file = null;
			int count = 0;
			while((entry = zis.getNextEntry()) != null)
			{
				if(entry.getName().contains(".DS_Store")) continue;
				file = new File(path, entry.getName());
				if(entry.isDirectory())
				{
					if(!file.exists())
						file.mkdir();
				}
				else
				{
					if(!file.exists())
						file.createNewFile();
					data = new byte[2048];
					fos = new FileOutputStream(file);
					dest = new BufferedOutputStream(fos, 2048);
					while((count = zis.read(data, 0, 2048)) != -1)
					{
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
				}
			}
			zis.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dmafPointer = DmAlloc();
		callbacks = new HashMap<String, IDmafListener>();
		String relPath = "/demo/xml/settings.xml";
		if(path.endsWith("/"))
			path += relPath.substring(1);
		else
			path += relPath;
		initialized = DmInitialize(dmafPointer, path);
		return initialized;
	}
	
	public void deinitialize()
	{
		DmDeinitialize(dmafPointer);
		callbacks.clear();
		initialized = false;
	}
	
	public float getCurrentTime()
	{
		return DmGetCurrentTime(dmafPointer);
	}
	
	public void tell(String trigger, long parameters)
	{
		DmTell(dmafPointer, trigger, parameters);
	}
	
	public void tell(String trigger)
	{
		DmTell(dmafPointer, trigger, 0);
	}
	
	public void tell(String trigger, float time, long parameters)
	{
		DmTellTime(dmafPointer, trigger, time, parameters);
	}
	
	public void tell(String trigger, float time)
	{
		DmTellTime(dmafPointer, trigger, time, 0);
	}
	
	public void Log(String file, int line, String message)
	{
		DmLog(dmafPointer, file, line, message);
	}
	
	public void process(float numSamples, int channels, float[] data)
	{
		DmProcess(dmafPointer, numSamples, channels, data);
	}
	
	public void process(float numSamples, int channels, short[] data)
	{
		if(floatData == null)
			floatData = new float[data.length];
		
		for(i = 0; i < data.length; i++)
			floatData[i] = data[i];
		
		DmProcess(dmafPointer, numSamples, channels, floatData);
		
		for(i = 0; i < data.length; i++)
		{
			floatData[i] *= 32768; 
			if(floatData[i] > 32767) floatData[i] = 32767;
			else if(floatData[i] < -32768) floatData[i] = -32768;
			data[i] = (short)floatData[i];
		}
		
		floatData = null;
	}
	
	public void processMix(float numSamples, int channels, float[] data)
	{
		if(floatData == null)
			floatData = new float[data.length];
		
		DmProcess(dmafPointer, numSamples, channels, floatData);
		
		for(i = 0; i < data.length; i++)
		{
			data[i] += floatData[i]; 
			if(data[i] > 1) data[i] = 1;
			else if(data[i] < -1) data[i] = -1;
		}
	}
	
	public void processMix(float numSamples, int channels, short[] data)
	{
		if(floatData == null)
			floatData = new float[data.length];
		
		for(i = 0; i < data.length; i++)
			floatData[i] = data[i];
		
		DmProcess(dmafPointer, numSamples, channels, floatData);
		
		for(i = 0; i < data.length; i++)
		{
			floatData[i] *= 32768; 
			data[i] += (short)floatData[i];
			if(data[i] > 32767) data[i] = 32767;
			else if(data[i] < -32768) data[i] = -32768;
		}
	}
	
	public void addListener(String trigger, IDmafListener listener, long args)
	{
		DmAddListener(dmafPointer, trigger, "onCallback", args);
		callbacks.put(trigger, listener);
	}
	
	public void addListener(String trigger, IDmafListener listener)
	{
		DmAddListener(dmafPointer, trigger, "onCallback", 0);
		callbacks.put(trigger, listener);
	}
	
	// end public methods
	
	// start private methods
	
	private float[] convertShortArray(short[] src)
	{
		float[] dest = new float[src.length];
		
		for(int i = 0; i < src.length; i++)
			dest[i] = src[i];
		
		return dest;
	}
	
	private void convertFloatArray(float[] src, short[] dest)
	{
		for(int i = 0; i < src.length; i++)
			dest[i] = (short)src[i];
	}
	
	// end provate methods
}
