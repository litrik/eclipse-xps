/*
 * (C) Copyright Johannes Brodwall <johannes@brodwall.com>, 2006
 *
 * Version 1.0.1
 *
 * History:
 * * 2006/02/20: Version 1.0: Colors and brightnesses with range checking.
 * * 05/11/2006: Tweaked by David Pritchard to add touchpad configuration.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "LEDs.h"

#include <windows.h>
#include <stdio.h>

#define BUFF_SIZE 0x2c

JNIEXPORT jint JNICALL Java_com_litrik_eclipse_xps_core_LEDs_setXpsColors
  (JNIEnv *env, jclass class, jchar side_color, jchar front_color, jchar top_color, jchar brightness, jchar touchpad_light)
{
  HANDLE hHandle = CreateFileA("\\\\.\\APPDRV", 
    GENERIC_READ|GENERIC_WRITE, 0, NULL,
    OPEN_EXISTING, 0x80, NULL);
  if (hHandle == INVALID_HANDLE_VALUE) {
    printf("CreateFileA(\\\\.\\APPDRV) failed: 0x%x\n", (int)GetLastError());
    return FALSE;
  }
  
  char buffer[BUFF_SIZE];
  memset(buffer, 0, BUFF_SIZE);
  buffer[0] = 0x04;
  buffer[1] = 0x00;
  buffer[2] = 0x06;
  buffer[3] = 0x00;
  
  buffer[4] = side_color;
  buffer[5] = front_color;
  buffer[6] = top_color;
  buffer[7] = brightness;
  buffer[8] = 0x1;

  buffer[12] = touchpad_light;

  DWORD bytesReturned;
  BOOL result = DeviceIoControl(hHandle, 0x22209C, 
    buffer, BUFF_SIZE, buffer, BUFF_SIZE,
    &bytesReturned, NULL);
  if (!result) {
    printf("DeviceIoControl failed: 0x%x\n", (int)GetLastError());
  }
  

  CloseHandle(hHandle);

  return result;  
}

