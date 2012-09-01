package com.morcinek.android.codegenerator.logic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.morcinek.android.codegenerator.serialization.Method;
import com.morcinek.android.codegenerator.serialization.Type;
import com.morcinek.android.codegenerator.serialization.Types;
import com.morcinek.android.codegenerator.serialization.Type.Require.Listener;

public class TypesAdapter {

	private Types types;

	public TypesAdapter(Types types) {
		super();
		this.types = types;
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
		for (Type type : types.getType()) {
			if (type.getName().equals(typeName)) {
				return type;
			}
		}
		if (Character.isUpperCase(typeName.charAt(0))){
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

	public List<Method> getTypeMethods(String typeName){
		Type type = getType(typeName);		
		if(type != null && type.getImplements() != null){
			return type.getImplements().getMethod();
		}
		return null;
	}
	
	public Set<Type> getTypesFromListener(String interfaceName){
		Set<Type> connectedTypes = new HashSet<Type>();
		for (Type type : types.getType()) {
			if(type.getRequire() != null){
				for (Listener listener : type.getRequire().getListener()) {
					if(interfaceName.equals(listener.getType())){
						connectedTypes.add(type);
					}
				}
			}
		}
		return connectedTypes;
	}
}
