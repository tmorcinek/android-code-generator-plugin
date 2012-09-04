package com.morcinek.android.codegenerator.plugin;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.morcinek.android.codegenerator.logic.TypesAdapter;
import com.morcinek.android.codegenerator.serialization.Types;


/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.morcinek.android.codegenerator"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private TypesAdapter typesAdapter;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(PreferencePage.P_DIRECTORY, "src/");
		store.setDefault(PreferencePage.P_PACKAGE, "com.mobica.activities");
		store.setDefault(PreferencePage.P_SHORT_MODE, false);
		store.setDefault(PreferencePage.P_AUTO_TYPE_RECOGNITION, false);
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Types.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			typesAdapter = new TypesAdapter(
					(Types) unmarshaller.unmarshal(getClass().getResourceAsStream("types.xml")));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public TypesAdapter getTypesAdapter() {
		typesAdapter.setAutoTypeRecognition(getPreferenceStore().getBoolean(PreferencePage.P_AUTO_TYPE_RECOGNITION));
		return typesAdapter;
	}
	
}
