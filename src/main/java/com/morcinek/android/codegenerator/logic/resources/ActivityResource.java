package com.morcinek.android.codegenerator.logic.resources;

public class ActivityResource extends WidgetResource {

	public ActivityResource(String pId) {
		super("@+id/" + pId);
	}

	@Override
	public String getVariableName() {
		String activityName = super.getVariableName();
		return Character.toUpperCase(activityName.charAt(0)) + activityName.substring(1) + "Activity";
	}

	@Override
	public String getReference() {
		return super.getReference().replaceFirst("id", "layout");
	}
}
