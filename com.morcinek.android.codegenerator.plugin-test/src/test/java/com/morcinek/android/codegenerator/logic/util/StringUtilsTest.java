package com.morcinek.android.codegenerator.logic.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testGetFileNameFromPath() {
		assertEquals("file_name", StringUtils.getFileNameFromPath("com/morcine/file_name.xml"));
	}

	@Test
	public void testGetActivityName() {
		assertEquals("FileNameOfLayoutActivity", StringUtils.getActivityNameFromId("file_name_of_layout"));
	}
	
	@Test
	public void testGetActivityName2() {
		assertEquals("FileNameOfLayoutActivity", StringUtils.getActivityNameFromVariable("fileNameOfLayout"));
	}

	@Test
	public void testGetIdNameFromId() {
		assertEquals("file_name_of_layout", StringUtils.getIdNameFromId("@+id/file_name_of_layout"));
		assertEquals("file_name_of_layout", StringUtils.getIdNameFromId("@id/file_name_of_layout"));
		assertEquals("file_name_of", StringUtils.getIdNameFromId("@id/file_name_of"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetIdNameFromIdException() {
		StringUtils.getIdNameFromId("@d/file_name_of_layout");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetIdNameFromIdException2() {
		StringUtils.getIdNameFromId("+id/file_name_of_layout");
	}

	@Test
	public void testGetVariableNameFromId() {
		assertEquals("fileNameOfLayout", StringUtils.getVariableNameFromId("@+id/file_name_of_layout"));
	}

	@Test
	public void testGetReferenceFromID() {
		assertEquals("R.id.file_name_of_layout", StringUtils.getReferenceFromID("@+id/file_name_of_layout"));
	}

	@Test
	public void testGetFirstLowerCase() {
		assertEquals("file_name_of_layout", StringUtils.getFirstLowerCase("File_name_of_layout"));
		assertEquals("name_of_layout", StringUtils.getFirstLowerCase("Name_of_layout"));
		assertEquals("of_layout", StringUtils.getFirstLowerCase("Of_layout"));
		assertEquals("of_layout", StringUtils.getFirstLowerCase("of_layout"));
		assertEquals("layout", StringUtils.getFirstLowerCase("Layout"));
	}

}
