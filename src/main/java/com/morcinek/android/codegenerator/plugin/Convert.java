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

import com.morcinek.android.codegenerator.logic.XmlLayoutParser;

@SuppressWarnings("restriction")
public class Convert extends AbstractHandler {

	@SuppressWarnings({ "unchecked" })
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		String directoryName = preferenceStore.getString(PreferencePage.P_DIRECTORY);
		String packageName = preferenceStore.getString(PreferencePage.P_PACKAGE);
		boolean shortMode = preferenceStore.getBoolean(PreferencePage.P_SHORT_MODE);
		XmlLayoutParser layoutParser = new XmlLayoutParser(shortMode, packageName);

		EvaluationContext evaluationContext = (EvaluationContext) arg0.getApplicationContext();
		List<Object> objects = (List<Object>) evaluationContext.getDefaultVariable();
		for (Object object : objects) {
			File file = (File) object;
			try {
				InputStream contents = file.getContents();
				IFile iFile = file.getProject().getFile(
						directoryName + packageName.replace(".", "/") + layoutParser.getFileName(file.getName()));
				InputStream generatedCode = layoutParser.generateCode(file.getName(), contents);
				iFile.create(generatedCode, false, null);
			} catch (ResourceException e) {
				showErrorMessage(e);
			} catch (CoreException e) {
				showErrorMessage(e);
			} catch (Exception e) {
				showErrorMessage(e);
			}
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
