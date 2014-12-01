/**
 * 
 * For information on usage and redistribution, and for a DISCLAIMER OF ALL
 * WARRANTIES, see the file, "LICENSE.txt," in this distribution.
 * 
 */

package org.puredata.android.io;

import java.io.IOException;
import java.util.Arrays;

import com.dinahmoe.dmaf.Dmaf;

//import org.puredata.core.PdBase;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * 
 * PdAudio manages an instance of {@link AudioWrapper} that uses Pure Data for audio processing.
 * 
 * @author Peter Brinkmann (peter.brinkmann@gmail.com) 
 *
 */
public class PdAudio {

	private static AudioWrapper audioWrapper = null;
	private static final Dmaf dmaf = new Dmaf();
	private static int numOutChannels;
	
	public static Dmaf getDmaf()
	{
		return dmaf;
	}

	private PdAudio() {
		// Do nothing; we just don't want instances of this class.
	}

	/**
	 * Initializes Pure Data as well as audio components.
	 * 
	 * @param sampleRate
	 * @param inChannels      number of input channels
	 * @param outChannels     number of output channels
	 * @param ticksPerBuffer  number of Pure Data ticks (i.e., blocks of 64 samples) per buffer; choose 1 for minimal latency,
	 *                            or more if performance is a concern; for Java audio only (Android 2.2 or earlier),
	 *                            ignored by OpenSL components
	 * @param restart         flag indicating whether the audio thread should be stopped if it is currently running
	 * @throws IOException    if the audio parameters are not supported by the device
	 */
	public synchronized static void initAudio(int sampleRate, int inChannels, int outChannels, final int ticksPerBuffer, boolean restart, AssetManager manager)
			throws IOException 
	{
		if (isRunning() && !restart) return;
		stopAudio();
		numOutChannels = outChannels;
		if (!AudioParameters.checkParameters(sampleRate, inChannels, outChannels) || ticksPerBuffer <= 0) {
			throw new IOException("bad Java audio parameters: " + sampleRate + ", " + inChannels + ", " + outChannels + ", " + ticksPerBuffer);
		}
		dmaf.setAssetManager(manager);
		if(!dmaf.initialize())
			throw new IOException("Dmaf could not initialize");
		int bufferSizePerChannel = ticksPerBuffer * 64;//PdBase.blockSize();
		audioWrapper = new AudioWrapper(sampleRate, inChannels, outChannels, bufferSizePerChannel) 
		{
			@Override
			protected int process(short[] inBuffer, short[] outBuffer) 
			{
				if(!dmaf.isInitialized())
					return 1;
				Arrays.fill(outBuffer, (short) 0);
				dmaf.process(outBuffer.length / numOutChannels, numOutChannels, outBuffer);
				return 0;
			}
		};
	}

	/**
	 * Starts the audio components.
	 * 
	 * @param context  current application context
	 */
	public synchronized static void startAudio(Context context) 
	{
		//PdBase.computeAudio(true);
		if (audioWrapper == null) 
		{
			throw new IllegalStateException("audio not initialized");
		}
		audioWrapper.start(context);
	}

	/**
	 * Stops the audio components.
	 */
	public synchronized static void stopAudio() 
	{
		if (!isRunning()) return;
		audioWrapper.stop();
	}

	/**
	 * @return true if and only if the audio wrapper is running
	 */
	public synchronized static boolean isRunning() 
	{
		return audioWrapper != null && audioWrapper.isRunning();
	}

	/**
	 * Releases resources held by the audio components.
	 */
	public synchronized static void release() 
	{
		stopAudio();
		if (audioWrapper == null) return;
		audioWrapper.release();
		audioWrapper = null;
	}
}
