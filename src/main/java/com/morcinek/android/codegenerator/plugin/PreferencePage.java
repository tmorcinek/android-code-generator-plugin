package com.morcinek.android.codegenerator.plugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;


/**
 * Preference for program used by ContextMenuPlugin
 */
public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String P_DIRECTORY = "directoryPreference";
	public static final String P_PACKAGE = "packagePreference";
	public static final String P_SHORT_MODE = "shortModePreference";

	public PreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Set up your file context menu application.");
//		initializeDefaults();
	}

//	/**
//	 * Sets the default values of the preferences.
//	 */
//	private void initializeDefaults() {
//		// find ShellContextMenu.exe on file system
//		// try
//		// {
//		// }
//		// catch (Exception e)
//		// {
//		// MessageDialog.openInformation(new Shell(), "ContextMenuPlugin",
//		// "Unable to find ShellContextMenu.exe:\n" +
//		// "Please enter manually in Window->Preferences->Context Menu");
//		// }
//	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {

		addField(new StringFieldEditor(P_DIRECTORY, "&Directory:", getFieldEditorParent()));
		addField(new StringFieldEditor(P_PACKAGE, "&Package:", getFieldEditorParent()));
		addField(new StringFieldEditor(P_SHORT_MODE, "&Short Mode:", getFieldEditorParent()));

	}

	public void init(IWorkbench arg0) {
		// TODO Auto-generated method stub

	}
}