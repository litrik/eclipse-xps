/**
 Copyright 2006 Litrik De Roy

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

import org.eclipse.jdt.junit.ITestRunListener;
import org.eclipse.jface.preference.IPreferenceStore;

import com.litrik.eclipse.xps.Activator;
import com.litrik.eclipse.xps.core.LEDs;
import com.litrik.eclipse.xps.preferences.XPSPreferenceConstants;

/**
 * Listener for JUnit test runs that sets the XPS' LEDs.
 */
public class XPSTestRunListener implements ITestRunListener, Runnable
{
	/**
	 * Indicates whether a failure occured during the test run.
	 */
	private boolean hasFailures;

	/**
	 * The preference store
	 */
	private IPreferenceStore store;

	/**
	 * The thread changing the LEDs
	 */
	private Thread ledThread;

	/**
	 * The name of the thread changing the LEDs
	 */
	private static String ledThreadName = "XPS LEDs";

	/**
	 * The current color
	 */
	private int color = 0;

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
			// Tell the thread to stop
			ledThread = null;
		}
	}

	public void testRunTerminated()
	{
	// Not interested in this event
	}

	public void testStarted(String testId, String testName)
	{
	// Not interested in this event
	}

	public void run()
	{
		Thread thisThread = Thread.currentThread();
		// The color to get started
		color = store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_START);
		// The maximum pulse/brigthness
		int maxPulse = store.getInt(XPSPreferenceConstants.P_JUNIT_BRIGHTNESS);
		// The current pulse/brigthness
		int pulse = store.getBoolean(XPSPreferenceConstants.P_JUNIT_PULSATE) ? 0 : maxPulse;
		try
		{
			while (ledThread == thisThread)
			{
				LEDs.setLeds(color, store.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_FANS), store
						.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_SPEAKERS), store
						.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_PANEL), Math.abs(pulse));
				if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_PULSATE))
				{
					// Calculate new pulse/brightness
					pulse = (pulse == -maxPulse) ? (maxPulse - 1) : (pulse - 1) % (maxPulse + 1);
				}
				Thread.sleep(75);
			}
			// We'ves stopped. Set brightness to preferred value.
			LEDs.setLeds(color, store.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_FANS), store
					.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_SPEAKERS), store
					.getBoolean(XPSPreferenceConstants.P_JUNIT_LOCATION_PANEL), store
					.getInt(XPSPreferenceConstants.P_JUNIT_BRIGHTNESS));
		}
		catch (InterruptedException e)
		{
			// Do nothing
		}
	}
}
