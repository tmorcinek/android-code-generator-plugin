package com.morcinek.android.codegenerator.logic.util;

public class ActivityResource extends WidgetResource {

	public ActivityResource(String pId) {
		super("@+id/" + pId);
	}

	@Override
	public String getVariableName() {
		return StringUtils.getActivityNameFromVariable(super.getVariableName());
	}

	@Override
	public String getReference() {
		return super.getReference().replaceFirst("id", "layout");
	}
}
