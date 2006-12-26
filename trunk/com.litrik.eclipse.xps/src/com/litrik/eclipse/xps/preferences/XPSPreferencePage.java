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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.litrik.eclipse.xps.Activator;

public class XPSPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{

	public XPSPreferencePage()
	{
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Shows JUnit results using the Dell XPS' built-in LEDs.");
	}

	protected Control createContents(Composite parent)
	{
		Control fieldEditorComposite = super.createContents(parent);

		Link homepageLink = new Link(parent, SWT.NONE);
		homepageLink.setText("Visit the <a>project homepage</a>.");
		homepageLink.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				Program.launch("http://code.google.com/p/eclipse-xps/");
			}
		});
		return fieldEditorComposite;
	}

	public void createFieldEditors()
	{
		// Composite composite = new Composite(getFieldEditorParent(),
		// SWT.NONE);
		// GridLayout compositeLayout = new GridLayout();
		// compositeLayout.numColumns = 1 ;
		// composite.setLayout(compositeLayout);
		// composite.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL |
		// GridData.GRAB_HORIZONTAL));

		addField(new BooleanFieldEditor(XPSPreferenceConstants.P_JUNIT_ENABLED, "&Listen to test runs", getFieldEditorParent()));
		addField(new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_START, "Start test run:", getFieldEditorParent()));
		addField(new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_SUCCESS, "Test success:", getFieldEditorParent()));
		addField(new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE, "Test failure:", getFieldEditorParent()));
	}

	private class LEDColorFieldEditor extends FieldEditor
	{
		private Combo colorField;

		public LEDColorFieldEditor(String name, String labelText, Composite parent)
		{
			super(name, labelText, parent);
		}

		protected void adjustForNumColumns(int numColumns)
		{

		}

		protected void doFillIntoGrid(Composite parent, int numColumns)
		{
			getLabelControl(parent);
			colorField = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.DROP_DOWN | SWT.READ_ONLY);
			GridLayout colorFieldLayout = new GridLayout();
			colorField.setLayout(colorFieldLayout);
			colorField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			colorField.setItems(new String[]
			{ "Off", "Ruby", "Citrine", "Amber", "Peridot", "Emerald", "Jade", "Topaz", "Tanzanite", "Aquamarine", "Sapphire",
					"Iolite", "Amythest", "Kunzite", "Rhodolite", "Coral", "Diamond" });
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

	}

	public void init(IWorkbench workbench)
	{
	// TODO Auto-generated method stub
	}

}