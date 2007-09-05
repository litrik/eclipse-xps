/**
 Copyright 2006-2007 Litrik De Roy

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

package com.litrik.eclipse.xps.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.litrik.eclipse.xps.Activator;

public class LEDs
{
	private static String ERROR_MESSAGE = "An unexpected error occurred when setting the XPS LEDs. This message will not be logged again.";

	private static boolean firstError = true;

	/**
	 * Set the XPS' LEDs.
	 * 
	 * @param color Color of the LEDs, where color is in the range 0-16
	 * @param fans Indicates whether fan LEDs should be used
	 * @param speakers Indicates whether speaker LEDs should be used
	 * @param panel Indicates whether panel LEDs should be used
	 */
	public static void setLeds(int color, boolean fans, boolean speakers, boolean panel, int brightness)
	{
		synchronized (LEDs.class)
		{
			if (setXpsColors((char) (fans ? color : 0), (char) (speakers ? color : 0), (char) (panel ? color : 0),
					(char) brightness, (char) 0x00) == 0)
			{
				if (firstError)
				{
					Activator.getDefault().getLog().log(
							new Status(IStatus.ERROR, Activator.PLUGIN_ID, Status.OK, ERROR_MESSAGE, null));
					firstError = false;
				}
			}
		}
	}

	/**
	 * Native C function to set the XPS' LEDs.
	 * 
	 * @param side_color Color of the side LEDs, where color is in the range
	 *            0-16
	 * @param front_color Color of the front LEDs, where color is in the range
	 *            0-16
	 * @param top_color Color of the top LEDs, where color is in the range 0-16
	 * @param brightness Brightness of the LEDs, where brightness is in the
	 *            range 0-7
	 * @param touchpad_light Enable touchpad LED (as 0 or 1)
	 * @return 0 when a problem occurred, 1 otherwise
	 */
	private static native int setXpsColors(char side_color, char front_color, char top_color, char brightness, char touchpad_light);
}
