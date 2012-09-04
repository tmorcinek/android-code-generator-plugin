package com.morcinek.android.codegenerator.logic.util;

import java.io.File;

public class StringUtils {

	public static String getFileNameFromPath(String filePath) {
		return new File(filePath).getName().replaceFirst(".xml", "");
	}

	public static String getActivityNameFromId(String pLayoutName) {
		String[] words = pLayoutName.split("_");
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			stringBuilder.append(Character.toUpperCase(word.charAt(0)) + word.substring(1));
		}
		return stringBuilder.toString() + "Activity";
	}

	public static String getActivityNameFromVariable(String variableName) {
		return Character.toUpperCase(variableName.charAt(0)) + variableName.substring(1) + "Activity";
	}

	public static String getIdNameFromId(String pId) {
		if (pId.startsWith("@+id/")) {
			return pId.substring(5);
		} else if (pId.startsWith("@id/")) {
			return pId.substring(4);
		}
		throw new IllegalArgumentException("Wrong id structure: " + pId);
	}

	public static String getVariableNameFromId(String pId) {
		String idName = getIdNameFromId(pId);
		StringBuilder stringBuilder = new StringBuilder();
		String[] words = idName.split("_");
		stringBuilder.append(words[0]);
		for (int i = 1; i < words.length; i++) {
			String word = words[i];
			stringBuilder.append(Character.toUpperCase(word.charAt(0)) + word.substring(1));
		}
		return stringBuilder.toString();
	}

	public static String getReferenceFromID(String pId) {
		return "R.id." + getIdNameFromId(pId);
	}

	public static String getFirstLowerCase(String word) {
		return Character.toLowerCase(word.charAt(0)) + word.substring(1);
	}
}
