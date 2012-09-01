package com.morcinek.android.codegenerator.plugin;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.EvaluationContext;
import org.eclipse.core.internal.resources.File;
import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;

import com.morcinek.android.codegenerator.logic.LayoutParser;

public class Convert extends AbstractHandler {

	private InputStream contents;

	@SuppressWarnings({ "unchecked", "restriction" })
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		EvaluationContext evaluationContext = (EvaluationContext) arg0.getApplicationContext();
		List<Object> objects = (List<Object>) evaluationContext.getDefaultVariable();
		if (!objects.isEmpty() && objects.get(0) instanceof File) {
			File file = (File) objects.get(0);
			if (file.getName().endsWith(".xml")) {
				IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
				String directoryName = preferenceStore.getString(PreferencePage.P_DIRECTORY);
				String packageName = preferenceStore.getString(PreferencePage.P_PACKAGE);
				boolean shortMode = Boolean.parseBoolean(preferenceStore.getString(PreferencePage.P_SHORT_MODE));
				try {
					contents = file.getContents();
					LayoutParser layoutParser = new LayoutParser(file.getName(), contents);
					IFile iFile = file.getProject().getFile(directoryName + packageName.replace(".", "/")+ layoutParser.getFileName());
					iFile.create(layoutParser.generateCode(shortMode, packageName), false, null);
				} catch (ResourceException e) {
					showErrorMessage(e);
				} catch (CoreException e) {
					showErrorMessage(e);
				} catch (Exception e) {
					showErrorMessage(e);
				}
			} else {
				showErrorMessage("This resource is not xml File.");
			}
		} else {
			showErrorMessage("This resource is not File type.");
		}
		return null;
	}

	private void showErrorMessage(Exception e) {
		showErrorMessage(e.getMessage());
	}

	private void showErrorMessage(String message) {
		MessageDialog.openInformation(new Shell(), "Android Code Generator Plugin", message);
	}

}
