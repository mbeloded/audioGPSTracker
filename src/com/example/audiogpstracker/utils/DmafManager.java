package com.example.audiogpstracker.utils;

import java.io.IOException;

import org.puredata.android.io.PdAudio;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.dinahmoe.dmaf.DmParametersPOD;
import com.dinahmoe.dmaf.Dmaf;
import com.dinahmoe.dmaf.IDmafListener;

public class DmafManager implements IDmafListener {
	
	private static final String LOG_TAG = DmafManager.class.getSimpleName();
	
	private static DmafManager instance = null;
	
	private Activity mActivity = null;
	
	private Dmaf mDmaf;
	private DmParametersPOD mPod;
	
	private boolean mIsNeedToPlaySound = false;
	
	private DmafManager(Activity activity) {
		mActivity = activity;
		
	}
	
	public static DmafManager getInstance(Activity activity) {
		if (instance == null)
			instance = new DmafManager(activity);
		return instance;
	}

	@Override
	public void onDmafCallback(String trigger, float time, long params,
			long args) {
		mActivity.runOnUiThread(new Runnable() 
		{
			@Override
			public void run() 
			{
				Toast.makeText(mActivity, "dmaf_end received", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	public void init() {
		try 
		{
			PdAudio.initAudio(44100, 0, 2, 1, true, mActivity.getAssets());
			mDmaf = PdAudio.getDmaf();
			if(mDmaf.isInitialized()) {
				Toast.makeText(mActivity, "Audio initialized", Toast.LENGTH_SHORT).show();
				PdAudio.startAudio(mActivity);
			}
			else
				Log.e(LOG_TAG, "Could not initialize");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void playAudio()
	{
		mDmaf.tell("play_sound");
	}
	
	public void setPlaySound(boolean value) {
		mIsNeedToPlaySound = value;
	}
	
	public boolean isNeedToPlaySound() {
		return mIsNeedToPlaySound;
	}

}
