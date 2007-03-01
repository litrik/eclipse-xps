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
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.ScaleFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.litrik.eclipse.xps.Activator;
import com.litrik.eclipse.xps.core.LEDs;

public class XPSPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
	private static String DESCRIPTION = "Shows JUnit results using the Dell XPS' built-in LEDs.";

	private static String HOMEPAGE_TEXT = "Visit the <a>project homepage</a>.";
	private static String HOMEPAGE_URL = "http://code.google.com/p/eclipse-xps/";

	private static String JUNIT_ENABLED = "&Listen to test runs";
	private static String JUNIT_COLOR = "Colors";
	private static String JUNIT_COLOR_START = "Start test run:";
	private static String JUNIT_COLOR_SUCCESS = "Test success:";
	private static String JUNIT_COLOR_FAILURE = "Test failure:";
	private static String JUNIT_LOCATION = "LEDs";
	private static String JUNIT_LOCATION_FANS = "Fans";
	private static String JUNIT_LOCATION_SPEAKERS = "Speakers";
	private static String JUNIT_LOCATION_PANEL = "Panel back";
	private static String JUNIT_BRIGHTNESS = "Brightness:";
	private static String JUNIT_PULSE = "Pulsate";
	private static String JUNIT_TIMEOUT = "Automatically turn LEDs off";
	private static String JUNIT_TIMEOUT_SUCCESS = "Time-out after success:";
	private static String JUNIT_TIMEOUT_FAILURE = "Time-out after failure:";
	private static String JUNIT_TIMEOUT_SECONDS = "seconds";

	public XPSPreferencePage()
	{
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription(DESCRIPTION);
	}

	protected Control createContents(Composite parent)
	{
		Control fieldEditorComposite = super.createContents(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, "com.litrik.eclipse.xps.pref");

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

		// A group for the color preferences
		Group colorGroup = new Group(getFieldEditorParent(), SWT.NONE);
		colorGroup.setText(JUNIT_COLOR);

		// The color when a test run starts
		final LEDColorFieldEditor startLEDColorFieldEditor = new LEDColorFieldEditor(XPSPreferenceConstants.P_JUNIT_COLOR_START,
				JUNIT_COLOR_START, colorGroup);
		addField(startLEDColorFieldEditor);
		// A preview button
		final Button previewStartButton = new Button(colorGroup, SWT.NONE);
		previewStartButton.setText("Preview");

		// The color when a test run was successful
		final LEDColorFieldEditor successLEDColorFieldEditor = new LEDColorFieldEditor(
				XPSPreferenceConstants.P_JUNIT_COLOR_SUCCESS, JUNIT_COLOR_SUCCESS, colorGroup);
		addField(successLEDColorFieldEditor);
		// A preview button
		final Button previewSuccessButton = new Button(colorGroup, SWT.NONE);
		previewSuccessButton.setText("Preview");

		// The color when a test failed
		final LEDColorFieldEditor failureLEDColorFieldEditor = new LEDColorFieldEditor(
				XPSPreferenceConstants.P_JUNIT_COLOR_FAILURE, JUNIT_COLOR_FAILURE, colorGroup);
		addField(failureLEDColorFieldEditor);
		// A preview button
		final Button previewFailureButton = new Button(colorGroup, SWT.NONE);
		previewFailureButton.setText("Preview");

		// The brightness
		final ScaleFieldEditor brightnessScaleFieldEditor = new ScaleFieldEditor(XPSPreferenceConstants.P_JUNIT_BRIGHTNESS,
				JUNIT_BRIGHTNESS, colorGroup, 1, 7, 1, 1);
		addField(brightnessScaleFieldEditor);

		// Pulsate
		addField(new BooleanFieldEditor(XPSPreferenceConstants.P_JUNIT_PULSATE, JUNIT_PULSE, colorGroup));

		// Layout the group
		GridLayout colorGroupLayout = new GridLayout();
		colorGroupLayout.numColumns = 3;
		colorGroup.setLayout(colorGroupLayout);
		GridData colorGroupGridData = new GridData(GridData.FILL_HORIZONTAL);
		colorGroupGridData.horizontalSpan = 2;
		colorGroup.setLayoutData(colorGroupGridData);

		// A group for the time-out preferences
		Group timeOutGroup = new Group(getFieldEditorParent(), SWT.NONE);
		timeOutGroup.setText(JUNIT_TIMEOUT);

		// The time-out after success
		addField(new IntegerFieldEditor(XPSPreferenceConstants.P_JUNIT_TIMEOUT_SUCCESS, JUNIT_TIMEOUT_SUCCESS, timeOutGroup, 3));
		final Label secondsSuccessLabel = new Label(timeOutGroup, SWT.NONE);
		secondsSuccessLabel.setText(JUNIT_TIMEOUT_SECONDS);

		// The time-out after failure
		addField(new IntegerFieldEditor(XPSPreferenceConstants.P_JUNIT_TIMEOUT_FAILURE, JUNIT_TIMEOUT_FAILURE, timeOutGroup, 3));
		final Label secondsFailureLabel = new Label(timeOutGroup, SWT.NONE);
		secondsFailureLabel.setText(JUNIT_TIMEOUT_SECONDS);

		// Layout the group
		GridLayout timeOutGroupLayout = new GridLayout();
		timeOutGroupLayout.numColumns = 3;
		timeOutGroup.setLayout(timeOutGroupLayout);
		GridData timeOutGroupGridData = new GridData(GridData.FILL_HORIZONTAL);
		timeOutGroupGridData.horizontalSpan = 2;
		timeOutGroup.setLayoutData(timeOutGroupGridData);

		// A group for the LED location preferences
		Group locationGroup = new Group(getFieldEditorParent(), SWT.NONE);
		locationGroup.setText(JUNIT_LOCATION);

		// Fan LEDs
		final BooleanFieldEditor fansBooleanFieldEditor = new BooleanFieldEditor(XPSPreferenceConstants.P_JUNIT_LOCATION_FANS,
				JUNIT_LOCATION_FANS, locationGroup);
		addField(fansBooleanFieldEditor);

		// Speaker LEDs
		final BooleanFieldEditor speakersBooleanFieldEditor = new BooleanFieldEditor(
				XPSPreferenceConstants.P_JUNIT_LOCATION_SPEAKERS, JUNIT_LOCATION_SPEAKERS, locationGroup);
		addField(speakersBooleanFieldEditor);

		// Panel back LEDs
		final BooleanFieldEditor panelBooleanFieldEditor = new BooleanFieldEditor(XPSPreferenceConstants.P_JUNIT_LOCATION_PANEL,
				JUNIT_LOCATION_PANEL, locationGroup);
		addField(panelBooleanFieldEditor);

		// Layout the group
		GridLayout locationGroupLayout = new GridLayout();
		locationGroup.setLayout(locationGroupLayout);
		GridData locationGroupGridData = new GridData(GridData.FILL_HORIZONTAL);
		locationGroupGridData.horizontalSpan = 2;
		locationGroup.setLayoutData(locationGroupGridData);

		// Let the "Preview" buttons do something
		previewStartButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				LEDs.setLeds(startLEDColorFieldEditor.getCurrentValue(), fansBooleanFieldEditor.getBooleanValue(),
						speakersBooleanFieldEditor.getBooleanValue(), panelBooleanFieldEditor.getBooleanValue(),
						brightnessScaleFieldEditor.getScaleControl().getSelection());
			}
		});
		previewSuccessButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				LEDs.setLeds(successLEDColorFieldEditor.getCurrentValue(), fansBooleanFieldEditor.getBooleanValue(),
						speakersBooleanFieldEditor.getBooleanValue(), panelBooleanFieldEditor.getBooleanValue(),
						brightnessScaleFieldEditor.getScaleControl().getSelection());
			}
		});
		previewFailureButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				LEDs.setLeds(failureLEDColorFieldEditor.getCurrentValue(), fansBooleanFieldEditor.getBooleanValue(),
						speakersBooleanFieldEditor.getBooleanValue(), panelBooleanFieldEditor.getBooleanValue(),
						brightnessScaleFieldEditor.getScaleControl().getSelection());
			}
		});

	}

	public void init(IWorkbench workbench)
	{
	// TODO Auto-generated method stub
	}

}