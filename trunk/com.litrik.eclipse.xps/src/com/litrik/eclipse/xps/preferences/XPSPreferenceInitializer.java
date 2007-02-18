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

package com.litrik.eclipse.xps.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.litrik.eclipse.xps.Activator;

/**
 * Class used to initialize default preference values.
 */
public class XPSPreferenceInitializer extends AbstractPreferenceInitializer
{

	public void initializeDefaultPreferences()
	{
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(XPSPreferenceConstants.P_JUNIT_ENABLED, true);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_COLOR_START, 5);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE, 1);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_COLOR_SUCCESS, 5);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_LOCATION_FANS, true);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_LOCATION_SPEAKERS, true);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_LOCATION_PANEL, true);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_BRIGHTNESS, 7);
		store.setDefault(XPSPreferenceConstants.P_JUNIT_PULSATE, true);
	}

}
