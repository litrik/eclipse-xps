/**
 Copyright 2006-2008 Litrik De Roy

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

package com.litrik.eclipse.xps.junit;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jface.preference.IPreferenceStore;

import com.litrik.eclipse.xps.Activator;
import com.litrik.eclipse.xps.core.LEDException;
import com.litrik.eclipse.xps.core.LEDs;
import com.litrik.eclipse.xps.preferences.XPSPreferenceConstants;

/**
 * Listener for JUnit test runs that sets the XPS' LEDs.
 */
public class XPSTestRunListener implements ITestRunListener, Runnable
{
	/**
	 * Indicates whether a failure occurred during the test run.
	 */
	private volatile boolean hasFailures;

	/**
	 * The preference store
	 */
	private IPreferenceStore store;

	/**
	 * The thread changing the LEDs
	 */
	private volatile Thread ledThread;

	/**
	 * The name of the thread changing the LEDs
	 */
	private static String ledThreadName = "XPS JUnit LEDs";

	/**
	 * The current color
	 */
	private volatile int color = 0;

	/**
	 * Constructor.
	 */
	public XPSTestRunListener()
	{
		super();
		store = Activator.getDefault().getPreferenceStore();
	}

	public void testEnded(String testId, String testName)
	{
	// Not interested in this event
	}

	public void testFailed(int status, String testId, String testName, String trace)
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			hasFailures = true;
			color = store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE);
		}
	}

	public void testReran(String testId, String testClass, String testName, int status, String trace)
	{
	// Not interested in this event
	}

	public void testRunEnded(long elapsedTime)
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			if (!hasFailures)
			{
				color = store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_SUCCESS);
			}
			// Tell the thread to stop
			ledThread = null;
		}
	}

	public void testRunStarted(int testCount)
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			color = store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_START);
			hasFailures = false;
			// Start the thread
			ledThread = new Thread(this);
			ledThread.setName(ledThreadName);
			ledThread.start();
		}
	}

	public void testRunStopped(long elapsedTime)
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			color = store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE);
			hasFailures = true;
			// Tell the thread to stop
			ledThread = null;
		}
	}

	public void testRunTerminated()
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			color = store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE);
			hasFailures = true;
			// Tell the thread to stop
			ledThread = null;
		}
	}

	public void testStarted(String testId, String testName)
	{
	// Not interested in this event
	}

	public void run()
	{
		Thread thisThread = Thread.currentThread();
		// The maximum pulse/brightness
		int maxPulse = store.getInt(XPSPreferenceConstants.P_JUNIT_BRIGHTNESS) - 1;
		// The current pulse/brightness
		int pulse = store.getBoolean(XPSPreferenceConstants.P_JUNIT_PULSATE) ? 0 : maxPulse;
		try
		{
			try
			{
				while (ledThread == thisThread) // http://java.sun.com/j2se/1.4.2/docs/guide/misc/threadPrimitiveDeprecation.html
				{
					LEDs.setLeds(color, store.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_FANS), store
							.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_SPEAKERS), store
							.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_PANEL), Math.abs(pulse) + 1);
					if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_PULSATE))
					{
						// Calculate new pulse/brightness
						pulse = (pulse == -maxPulse) ? (maxPulse - 1) : (pulse - 1) % (maxPulse + 1);
					}
					Thread.sleep(50);
				}
				// We have stopped. Set brightness to preferred value.
				LEDs.setLeds(color, store.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_FANS), store
						.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_SPEAKERS), store
						.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_PANEL), store
						.getInt(XPSPreferenceConstants.P_JUNIT_BRIGHTNESS));
				// Should we time-out and turn the LEDs off?
				if (hasFailures && store.getInt(XPSPreferenceConstants.P_JUNIT_TIMEOUT_FAILURE) > 0)
				{
					Thread.sleep(store.getInt(XPSPreferenceConstants.P_JUNIT_TIMEOUT_FAILURE) * 1000);
					LEDs.setLeds(0, true, true, true, 0);
				}
				else if (!hasFailures && store.getInt(XPSPreferenceConstants.P_JUNIT_TIMEOUT_SUCCESS) > 0)
				{
					Thread.sleep(store.getInt(XPSPreferenceConstants.P_JUNIT_TIMEOUT_SUCCESS) * 1000);
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
			Activator.getDefault().getLog().log(
					new Status(IStatus.ERROR, Activator.PLUGIN_ID, Status.OK, LEDException.MESSAGE, e));
		}
	}
}
