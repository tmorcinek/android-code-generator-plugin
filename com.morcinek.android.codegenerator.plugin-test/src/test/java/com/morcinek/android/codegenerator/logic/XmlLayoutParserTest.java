package com.morcinek.android.codegenerator.logic;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.morcinek.android.codegenerator.serialization.Types;

public class XmlLayoutParserTest {

	private static XmlLayoutParser layoutParser;
	private static String mainActivityString;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Types types = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Types.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			types = (Types) unmarshaller.unmarshal(TypesAdapterTest.class.getResourceAsStream("/types.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		// testing on long form
		InputStream javaInput = XmlLayoutParserTest.class.getResourceAsStream("/MainActivity.java");
		mainActivityString = IOUtils.toString(javaInput);
		TypesAdapter typesAdapter = new TypesAdapter(types);
		typesAdapter.setAutoTypeRecognition(true);
		layoutParser = new XmlLayoutParser(false, "com.morcinek.activities", typesAdapter);
	}

	@Test
	public void testXmlLayoutParserMain() throws XPathExpressionException, ParserConfigurationException, SAXException,
			IOException {
		InputStream inputStream = getClass().getResourceAsStream("/main.xml");

		InputStream generateCode = layoutParser.generateCode("/main", inputStream);
		assertNotNull(generateCode);
		assertEquals(mainActivityString, IOUtils.toString(generateCode));
	}

	@Test
	public void testXmlLayoutParser() throws XPathExpressionException, ParserConfigurationException, SAXException,
			IOException {
		InputStream inputStream = getClass().getResourceAsStream("/main_header_1.xml");

		InputStream generateCode = layoutParser.generateCode("/main", inputStream);
		assertEquals(mainActivityString, IOUtils.toString(generateCode));
	}

	@Test
	public void testXmlLayoutParserHeader2() throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		InputStream inputStream = getClass().getResourceAsStream("/main_header_2.xml");
		InputStream generateCode = layoutParser.generateCode("/main", inputStream);
		assertEquals(mainActivityString, IOUtils.toString(generateCode));
	}

	@Test
	public void testXmlLayoutParserNoHeader() throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		InputStream inputStream = getClass().getResourceAsStream("/main_no_header.xml");
		InputStream generateCode = layoutParser.generateCode("/main", inputStream);
		assertEquals(mainActivityString, IOUtils.toString(generateCode));
	}

	@Test
	public void testXmlLayoutParserLayout() throws XPathExpressionException, ParserConfigurationException,
			SAXException, IOException {
		InputStream inputStream = getClass().getResourceAsStream("/layout.xml");
		InputStream generateCode = layoutParser.generateCode("/layout", inputStream);
		String layoutActivityString = IOUtils.toString(XmlLayoutParserTest.class
				.getResourceAsStream("/LayoutActivity.java"));
		assertEquals(layoutActivityString, IOUtils.toString(generateCode));
	}

	@Test
	public void testGetFileNameFromPath() {
		assertEquals("/MainNoHeaderActivity.java", layoutParser.getFileName("main_no_header.xml"));
	}

	@Test
	public void testSpecific() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		InputStream inputStream = getClass().getResourceAsStream("/specific.xml");
		InputStream generateCode = layoutParser.generateCode("/specific", inputStream);
		String layoutActivityString = IOUtils.toString(XmlLayoutParserTest.class
				.getResourceAsStream("/SpecificActivity.java"));
		assertEquals(layoutActivityString, IOUtils.toString(generateCode));
	}
}
