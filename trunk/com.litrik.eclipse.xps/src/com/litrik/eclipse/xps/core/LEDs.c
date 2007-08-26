/**
 Copyright 2007 Litrik De Roy

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

#include "LEDs.h"

#include <windows.h>
#include <stdio.h>

#include <LEDDecl.h>

#define BUFF_SIZE 0x2c

JNIEXPORT jint JNICALL Java_com_litrik_eclipse_xps_core_LEDs_setXpsColors
  (JNIEnv *env, jclass clazz, jchar side_color, jchar front_color, jchar top_color, jchar brightness, jchar touchpad_light)
{
	LED_CAPS l_LEDCaps = NONE;
	GetCapabilities(&l_LEDCaps);
	
	NBK_LED_USER_SETTINGS l_Settings = {0};
	l_Settings.ClrZone1 = side_color;
	l_Settings.ClrZone2 = front_color;
	l_Settings.ClrZone3 = top_color;
	l_Settings.ClrZone4 = OFF;
	l_Settings.IntLevel = brightness;
	return SetLEDSettings(&l_Settings, l_LEDCaps);
}

