package com.morcinek.android.codegenerator.logic;

public class WidgetResource {
	
	protected String id;

	public WidgetResource(String pId) {
		id = pId;
	}
	
	private String getIdNameFromId(String pId) {
		if (pId.startsWith("@+id/")) {
			return pId.substring(5);
		} else if(pId.startsWith("@id/")){
			return pId.substring(4);
		}
		throw new IllegalArgumentException("Wrong id structure: " + pId);
	}

	public String getVariableName() {
		String idName = getIdNameFromId(id);
		StringBuilder stringBuilder = new StringBuilder();
		String[] words = idName.split("_");
		stringBuilder.append(words[0]);
		for (int i = 1; i < words.length; i++) {
			String word = words[i];
			stringBuilder.append(Character.toUpperCase(word.charAt(0)) + word.substring(1));
		}
		return stringBuilder.toString();
	}

	public String getReference() {
		String idName = getIdNameFromId(id);

		return "R.id." + idName;
	}
	

}
