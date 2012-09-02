package com.morcinek.android.codegenerator.logic;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.BeforeClass;
import org.junit.Test;

public class XmlLayoutParserTest {

	private static XmlLayoutParser layoutParser;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		layoutParser = new XmlLayoutParser(false, "");
	}

	@Test
	public void testXmlLayoutParser() {
		InputStream inputStream = getClass().getResourceAsStream("main_header_1.xml");
		assertNotNull(layoutParser.generateCode("main_header", inputStream));
	}

	@Test
	public void testXmlLayoutParserHeader2() {
		InputStream inputStream = getClass().getResourceAsStream("main_header_2.xml");
		assertNotNull(layoutParser.generateCode("main_header", inputStream));
	}

	@Test
	public void testXmlLayoutParserNoHeader() {
		InputStream inputStream = getClass().getResourceAsStream("main_no_header.xml");
		assertNotNull(layoutParser.generateCode("main_header", inputStream));
	}

	@Test
	public void testXmlLayoutParserStringInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerateCode() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFileName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLayoutName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFileNameFromPath() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetActivityName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIdNameFromId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetVariableNameFromId() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReferenceFromID() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFirstLowerCase() {
		fail("Not yet implemented");
	}

}
