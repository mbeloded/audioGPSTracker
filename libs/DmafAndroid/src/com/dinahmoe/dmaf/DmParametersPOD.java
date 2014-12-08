package com.dinahmoe.dmaf;

public class DmParametersPOD 
{
	static 
	{
		System.loadLibrary("DmafAndroid");
	}
	
	private long intPointer;
	
	private static native long DmCreateParameters(int size_);
	private static native int DmGetNumParameters(long ptr_);
	private static native void DmSetStringParameter(long ptr_, int index_, String value_);
	private static native void DmSetIntParameter(long ptr_, int index_, int value_);
	private static native void DmSetEnumParameter(long ptr_, int index_, int value_);
	private static native void DmSetFloatParameter(long ptr_, int index_, float value_);
	private static native String DmGetStringParameter(long ptr_, int index_);
	private static native int DmGetIntParameter(long ptr_, int index_);
	private static native int DmGetEnumParameter(long ptr_, int index_);
	private static native float DmGetFloatParameter(long ptr_, int index_);
	
	public static DmParametersPOD createParameters(int size)
	{
		DmParametersPOD pod = new DmParametersPOD();
		pod.intPointer = DmCreateParameters(size);
		return pod;
	}
	
	public static int getNumParameters(DmParametersPOD pod)
	{
		return DmGetNumParameters(pod.intPointer);
	}
	
	public static void setStringParameter(DmParametersPOD pod, int index, String value)
	{
		DmSetStringParameter(pod.intPointer, index, value);
	}
	
	public static void setIntParameter(DmParametersPOD pod, int index, int value)
	{
		DmSetIntParameter(pod.intPointer, index, value);
	}
	
	public static void setEnumParameter(DmParametersPOD pod, int index, int value)
	{
		DmSetEnumParameter(pod.intPointer, index, value);
	}
	
	public static void setFloatParameter(DmParametersPOD pod, int index, float value)
	{
		DmSetFloatParameter(pod.intPointer, index, value);
	}
	
	public static String getStringParameter(DmParametersPOD pod, int index)
	{
		return DmGetStringParameter(pod.intPointer, index);
	}
	
	public static int getIntParameter(DmParametersPOD pod, int index)
	{
		return DmGetIntParameter(pod.intPointer, index);
	}
	
	public static int getEnumParameter(DmParametersPOD pod, int index)
	{
		return DmGetEnumParameter(pod.intPointer, index);
	}
	
	public static float getFloatParameter(DmParametersPOD pod, int index)
	{
		return DmGetFloatParameter(pod.intPointer, index);
	}
	
	public static long getParamPointer(DmParametersPOD pod)
	{
		return pod.intPointer;
	}
}
