package com.morcinek.android.codegenerator.logic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.jface.preference.IPreferenceStore;

import com.morcinek.android.codegenerator.plugin.Activator;
import com.morcinek.android.codegenerator.plugin.PreferencePage;
import com.morcinek.android.codegenerator.serialization.Method;
import com.morcinek.android.codegenerator.serialization.Type;
import com.morcinek.android.codegenerator.serialization.Type.Require.Listener;
import com.morcinek.android.codegenerator.serialization.Types;

public class TypesAdapter {

	private final Types types;

	private final boolean autoTypeRecognition;

	public TypesAdapter(Types types) {
		super();
		this.types = types;
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		this.autoTypeRecognition = preferenceStore.getBoolean(PreferencePage.P_AUTO_TYPE_RECOGNITION);
	}

	//
	// public Types getTypes() {
	// return types;
	// }
	//
	// public List<Type> getTypeList() {
	// return types.getType();
	// }

	public Type getType(String typeName) {
		if (autoTypeRecognition && Pattern.matches("([a-zA-Z]+.)+[a-zA-Z]+", typeName)) {
			int lastDot = typeName.lastIndexOf(".");
			Type type = new Type();
			type.setName(typeName.substring(lastDot+1));
			type.setPackage(typeName.substring(0, lastDot));
			return type;
		}
		for (Type type : types.getType()) {
			if (type.getName().equals(typeName)) {
				return type;
			}
		}
		if (autoTypeRecognition && Character.isUpperCase(typeName.charAt(0))) {
			Type type = new Type();
			type.setName(typeName);
			type.setPackage("android.widget");
			return type;
		}
		return null;
	}

	public String getTypeFullName(String typeName) {
		Type type = getType(typeName);
		if (type != null) {
			return type.getPackage() + "." + typeName;
		}
		return null;
	}

	public List<Method> getTypeMethods(String typeName) {
		Type type = getType(typeName);
		if (type != null && type.getImplements() != null) {
			return type.getImplements().getMethod();
		}
		return null;
	}

	public Set<Type> getTypesFromListener(String interfaceName) {
		Set<Type> connectedTypes = new HashSet<Type>();
		for (Type type : types.getType()) {
			if (type.getRequire() != null) {
				for (Listener listener : type.getRequire().getListener()) {
					if (interfaceName.equals(listener.getType())) {
						connectedTypes.add(type);
					}
				}
			}
		}
		return connectedTypes;
	}
}
