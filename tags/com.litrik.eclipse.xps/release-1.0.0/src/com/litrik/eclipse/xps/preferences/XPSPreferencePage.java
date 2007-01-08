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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
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
	private static String DESCRIPTION = "Shows JUnit results using the Dell XPS' built-in LEDs.";
	
	private static String HOMEPAGE_TEXT = "Visit the <a>project homepage</a>.";
	private static String HOMEPAGE_URL = "http://code.google.com/p/eclipse-xps/";
	
	private static String JUNIT_ENABLED = "&Listen to test runs";
	private static String JUNIT_COLOR_START = "Start test run:";
	private static String JUNIT_COLOR_SUCCESS = "Test success:";
	private static String JUNIT_COLOR_FAILURE = "Test failure:";
	
	public XPSPreferencePage()
	{
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(DESCRIPTION);
	}

	protected Control createContents(Composite parent)
	{
		Control fieldEditorComposite = super.createContents(parent);

		Link homepageLink = new Link(parent, SWT.NONE);
		homepageLink.setText(HOMEPAGE_TEXT);
		homepageLink.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event)
			{
				Program.launch(HOMEPAGE_URL);
			}
		});
		return fieldEditorComposite;
	}

	public void createFieldEditors()
	{
		addField(new BooleanFieldEditor(XPSPreferenceConstants.P_JUNIT_ENABLED, JUNIT_ENABLED, getFieldEditorParent()));
		addField(new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_START, JUNIT_COLOR_START, getFieldEditorParent()));
		addField(new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_SUCCESS, JUNIT_COLOR_SUCCESS, getFieldEditorParent()));
		addField(new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE, JUNIT_COLOR_FAILURE, getFieldEditorParent()));
	}

	public void init(IWorkbench workbench)
	{
	// TODO Auto-generated method stub
	}

}