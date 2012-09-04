package com.morcinek.android.codegenerator.plugin;

import java.io.InputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xml.sax.SAXException;

import com.morcinek.android.codegenerator.logic.XmlLayoutParser;

@SuppressWarnings("restriction")
public class Convert extends AbstractHandler {

	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		ILog iLog = Activator.getDefault().getLog();
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String directoryName = preferenceStore.getString(PreferencePage.P_DIRECTORY);
		String packageName = preferenceStore.getString(PreferencePage.P_PACKAGE);
		boolean shortMode = preferenceStore.getBoolean(PreferencePage.P_SHORT_MODE);

		try {
			XmlLayoutParser layoutParser = new XmlLayoutParser(shortMode, packageName, Activator.getDefault()
					.getTypesAdapter());
			layoutParser.setLog(iLog);

			ISelection sel = HandlerUtil.getActiveMenuSelection(arg0);
			IStructuredSelection selection = (IStructuredSelection) sel;

			for (Object object : selection.toList()) {
				File file = (File) object;
				iLog.log(new Status(Status.INFO, Activator.PLUGIN_ID, "Generation for resource: "
						+ file.getName()));
				try {
					InputStream contents = file.getContents();
					IFile iFile = file.getProject().getFile(
							directoryName + packageName.replace(".", "/") + layoutParser.getFileName(file.getName()));
					InputStream generatedCode = layoutParser.generateCode(file.getName(), contents);
					iLog.log(new Status(Status.INFO, Activator.PLUGIN_ID, "Activity class "
							+ layoutParser.getFileName(file.getName()) + " successfully created"));
					iFile.create(generatedCode, false, null);
					iLog.log(new Status(Status.INFO, Activator.PLUGIN_ID, "File " + iFile.getFullPath().toString()
							+ " successfully created"));
				} catch (ResourceException e) {
					showErrorMessage(e);
				} catch (CoreException e) {
					showErrorMessage(e);
				} catch (SAXException e) {
					showErrorMessage("Parsing file: " + file.getName() + " error");
				} catch (Exception e) {
					showErrorMessage(e);
				}
			}
		} catch (Exception e) {
			showErrorMessage("File 'types.xml' is not valid.");
		}
		return null;
	}

	private void showErrorMessage(Exception e) {
		showErrorMessage(e.getMessage());
	}

	private void showErrorMessage(String message) {
		MessageDialog.openError(new Shell(), "Android Code Generator Plugin", message);
	}

}
