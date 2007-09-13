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

package com.litrik.eclipse.xps.preferences;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

class LEDColorFieldEditor extends FieldEditor
{
	private static String[] COLOR_NAMES = new String[]
	{ "Off", "Ruby", "Citrine", "Amber", "Peridot", "Emerald", "Jade", "Topaz", "Tanzanite", "Aquamarine", "Sapphire", "Iolite",
			"Amythest", "Kunzite", "Rhodolite", "Coral", "Diamond" };

	private Combo colorField;

	public LEDColorFieldEditor(String name, String labelText, Composite parent)
	{
		super(name, labelText, parent);
	}

	protected void adjustForNumColumns(int numColumns)
	{
	// Do nothing
	}

	protected void doFillIntoGrid(Composite parent, int numColumns)
	{
		getLabelControl(parent);
		colorField = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
		GridLayout colorFieldLayout = new GridLayout();
		colorField.setLayout(colorFieldLayout);
		colorField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		colorField.setItems(COLOR_NAMES);
	}

	protected void doLoad()
	{
		colorField.select(getPreferenceStore().getInt(getPreferenceName()));
	}

	protected void doLoadDefault()
	{
		colorField.select(getPreferenceStore().getDefaultInt(getPreferenceName()));
	}

	protected void doStore()
	{
		getPreferenceStore().setValue(getPreferenceName(), colorField.getSelectionIndex());
	}

	public int getNumberOfControls()
	{
		return 1;
	}

	public int getCurrentValue()
	{
		return colorField.getSelectionIndex();
	}
}
