package com.morcinek.android.codegenerator.logic;

import static org.junit.Assert.assertEquals;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.morcinek.android.codegenerator.serialization.Type;
import com.morcinek.android.codegenerator.serialization.Types;

public class TypesAdapterTest {

	private static TypesAdapter typesAdapter;
	private static Types types;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Types.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			types = (Types) unmarshaller.unmarshal(TypesAdapterTest.class.getResourceAsStream("/types_easy.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() {
		typesAdapter = new TypesAdapter(types);
		typesAdapter.setAutoTypeRecognition(true);
	}

	@Test
	public void test() {
		Type type = typesAdapter.getType("Button");
		assertEquals("android.widget", type.getPackage());
	}

	@Test
	public void test2() {
		Type type = typesAdapter.getType("AceButton");
		assertEquals("android.widget", type.getPackage());
	}

	@Test
	public void test3() {
		Type type = typesAdapter.getType("android.widget.AceButton");
		assertEquals("android.widget", type.getPackage());
	}

	@Test
	public void test4() {
		Type type = typesAdapter.getType("com.morcinek.android.widget.v4.AceButton");
		assertEquals("com.morcinek.android.widget.v4", type.getPackage());
	}

	@Test
	public void test5() {
		Type type = typesAdapter.getType("aceButton");
		assertEquals("android.widget", type.getPackage());
	}

	@Test
	public void testComplicated() {
		Type type = typesAdapter.getType("com.morcinek.widget.Button");
		assertEquals("com.morcinek.widget", type.getPackage());
		type = typesAdapter.getType("Button");
		assertEquals("android.widget", type.getPackage());
	}

	@Test
	public void testParsing() {
		Types types = null;
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Types.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			types = (Types) unmarshaller.unmarshal(TypesAdapterTest.class
					.getResourceAsStream("/types_easy_no_package.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		TypesAdapter typesAdapter = new TypesAdapter(types);
		typesAdapter.setAutoTypeRecognition(true);
		Type type = typesAdapter.getType("Button");
		assertEquals("morcinek.widget", type.getPackage());
		type = typesAdapter.getType("com.morcinek.widget.Button");
		assertEquals("com.morcinek.widget", type.getPackage());
	}

}
