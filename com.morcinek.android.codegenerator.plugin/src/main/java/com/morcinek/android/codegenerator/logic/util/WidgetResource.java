package com.morcinek.android.codegenerator.logic.util;

public class WidgetResource {

	protected String id;

	public WidgetResource(String pId) {
		id = pId;
	}

	public String getVariableName() {
		return StringUtils.getVariableNameFromId(id);
	}

	public String getReference() {
		return StringUtils.getReferenceFromID(id);
	}

}
