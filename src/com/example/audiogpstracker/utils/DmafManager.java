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
	private DmParametersPOD mPod;
	private Dmaf mDmaf;

	private boolean mIsNeedToPlaySound = true;

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
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mActivity, "dmaf_end received",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void init() {
		try {
			PdAudio.initAudio(44100, 0, 2, 1, true, mActivity.getAssets());
			mDmaf = PdAudio.getDmaf();
			if (mDmaf.isInitialized()) {
				Toast.makeText(mActivity, "Audio initialized",
						Toast.LENGTH_SHORT).show();
				PdAudio.startAudio(mActivity);
			} else
				Log.e(LOG_TAG, "Could not initialize");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void onCompassChangePlay(float compassValue) {
		if (mDmaf != null && mDmaf.isInitialized()) {
			mPod = DmParametersPOD.createParameters(1);

			DmParametersPOD.setFloatParameter(mPod, 0, compassValue);

			mDmaf.tell("compass_change", DmParametersPOD.getParamPointer(mPod));
		}
	}

	public void setSpeed(float speed) {
		if (mDmaf != null && mDmaf.isInitialized()) {
			mPod = DmParametersPOD.createParameters(1);

			DmParametersPOD.setFloatParameter(mPod, 0, speed);

			mDmaf.tell("speed_gps", DmParametersPOD.getParamPointer(mPod));
		}
	}

	public void startAudio() {
		PdAudio.startAudio(mActivity);
	}

	public void stopPlaying() {
		PdAudio.stopAudio();
		PdAudio.release();
	}
	
	public boolean isInitialized() {
		if(mDmaf == null)
			return false;
		
		return mDmaf.isInitialized();
	}

	public void setPlaySound(boolean value) {
		mIsNeedToPlaySound = value;
	}

	public boolean isNeedToPlaySound() {
		return mIsNeedToPlaySound;
	}
}
