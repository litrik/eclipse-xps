/**
 Copyright 2008 Litrik De Roy

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.litrik.eclipse.xps.ant;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

import com.litrik.eclipse.xps.core.LEDException;
import com.litrik.eclipse.xps.core.LEDs;

/**
 * Listener for Ant builds that sets the XPS' LEDs.
 */
public class XPSAntBuildListener implements BuildListener, Runnable
{
	/**
	 * The default value of some preferences
	 */
	boolean fEnabled = true;
	int fBrightness = 16;
	boolean fPulsate = true;
	boolean fLocationFans = true;
	boolean fLocationSpeakers = true;
	boolean fLocationPanel = true;
	int fTimeoutFailure = 3;
	int fTimeoutSuccess = 3;
	int fColorStart = 5;
	int fColorFailure = 1;
	int fColorSuccess = 5;

	/**
	 * The thread changing the LEDs
	 */
	private volatile Thread ledThread;

	/**
	 * The name of the thread changing the LEDs
	 */
	private static String ledThreadName = "XPS Ant LEDs";

	/**
	 * The current color
	 */
	private volatile int color = 0;

	/**
	 * Indicates whether a failure occurred during the test run.
	 */
	private volatile boolean hasFailed;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#buildFinished(org.apache.tools.ant.BuildEvent)
	 */
	public void buildFinished(BuildEvent event)
	{
		if (fEnabled)
		{
			hasFailed = (event.getException() != null);
			color = hasFailed ? fColorFailure : fColorSuccess;
			// Tell the thread to stop
			ledThread = null;
			try
			{
				Thread.sleep(((hasFailed ? fTimeoutFailure : fTimeoutSuccess) + 1) * 1000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#buildStarted(org.apache.tools.ant.BuildEvent)
	 */
	public void buildStarted(BuildEvent event)
	{
		LEDs.loadLibraries();
		// helper method
		if (fEnabled)
		{
			color = fColorStart;
			hasFailed = false;
			// Start the thread
			ledThread = new Thread(this);
			ledThread.setName(ledThreadName);
			ledThread.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#messageLogged(org.apache.tools.ant.BuildEvent)
	 */
	public void messageLogged(BuildEvent event)
	{
	// Not interested in this event
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#targetFinished(org.apache.tools.ant.BuildEvent)
	 */
	public void targetFinished(BuildEvent event)
	{
	// Not interested in this event
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#targetStarted(org.apache.tools.ant.BuildEvent)
	 */
	public void targetStarted(BuildEvent event)
	{
	// Not interested in this event
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#taskFinished(org.apache.tools.ant.BuildEvent)
	 */
	public void taskFinished(BuildEvent event)
	{
	// Not interested in this event
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.BuildListener#taskStarted(org.apache.tools.ant.BuildEvent)
	 */
	public void taskStarted(BuildEvent event)
	{
	// Not interested in this event
	}

	public void run()
	{
		Thread thisThread = Thread.currentThread();
		// The maximum pulse/brightness
		int maxPulse = fBrightness - 1;
		// The current pulse/brightness
		int pulse = fPulsate ? 0 : maxPulse;
		try
		{
			try
			{
				while (ledThread == thisThread) // http://java.sun.com/j2se/1.4.2/docs/guide/misc/threadPrimitiveDeprecation.html
				{
					LEDs.setLeds(color, fLocationFans, fLocationSpeakers, fLocationPanel, Math.abs(pulse) + 1);
					if (fPulsate)
					{
						// Calculate new pulse/brightness
						pulse = (pulse == -maxPulse) ? (maxPulse - 1) : (pulse - 1) % (maxPulse + 1);
					}
					Thread.sleep(50);
				}
				// We have stopped. Set brightness to preferred value.
				LEDs.setLeds(color, fLocationFans, fLocationSpeakers, fLocationPanel, fBrightness);
				// Should we time-out and turn the LEDs off?
				if (hasFailed && fTimeoutFailure > 0)
				{
					Thread.sleep(fTimeoutFailure * 1000);
					LEDs.setLeds(0, true, true, true, 0);
				}
				else if (!hasFailed && fTimeoutSuccess > 0)
				{
					Thread.sleep(fTimeoutSuccess * 1000);
					LEDs.setLeds(0, true, true, true, 0);
				}
			}
			catch (InterruptedException e)
			{
				LEDs.setLeds(0, true, true, true, 0);
			}
		}
		catch (LEDException e)
		{
			e.printStackTrace();
		}
	}
}
