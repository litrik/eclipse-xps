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
public class XPSTestRunListener implements ITestRunListener
{
	/**
	 * Indicates whether a failure occured during the test run.
	 */
	private boolean hasFailures;

	private IPreferenceStore store;

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
			LEDs.setLeds(store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE));
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
				LEDs.setLeds(store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_SUCCESS));
			}
		}
	}

	public void testRunStarted(int testCount)
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			hasFailures = false;
			LEDs.setLeds(store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_START));
		}
	}

	public void testRunStopped(long elapsedTime)
	{
		if (store.getBoolean(XPSPreferenceConstants.P_JUNIT_ENABLED))
		{
			LEDs.setLeds(store.getInt(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE));
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

}
