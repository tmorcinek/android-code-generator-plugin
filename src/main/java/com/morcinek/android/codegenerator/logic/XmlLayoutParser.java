package com.morcinek.android.codegenerator.logic;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.morcinek.android.codegenerator.logic.resources.ActivityResource;
import com.morcinek.android.codegenerator.logic.resources.WidgetResource;


public class XmlLayoutParser {

	private final String packageName;

	private final boolean pShortMode;

	public XmlLayoutParser(boolean pShortMode, String packageName) {
		this.pShortMode = pShortMode;
		this.packageName = packageName;
	}

	/**
	 * You can retrieve widget name from Node by calling
	 * <code>getNodeName()</code> on it. You can retrieve any attribute by
	 * calling <code>getAttributes().getNamedItem("attr_name")</code>. You need
	 * to pass full attribute name (with namespace). For attribute
	 * <i>android:id</i> <blockquote>
	 * <code>getAttributes().getNamedItem("android:id")</code> </blockquote>
	 * 
	 * @param pShortMode
	 * @param packageName
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 */
	public InputStream generateCode(String filePath, InputStream inputStream) throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		String layoutName = getFileNameFromPath(filePath);

		NodeList nodeList = getNodesWithId(inputStream);
		ActivityObject activityObject = new ActivityObject(pShortMode);
		activityObject.setActivityResource(new ActivityResource(layoutName));
		activityObject.setPackageName(packageName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String id = node.getAttributes().getNamedItem("android:id").getNodeValue();
			String typeName = node.getNodeName();
			WidgetResource widget = new WidgetResource(id);
			activityObject.addWidget(widget, typeName);
		}
		return new ByteArrayInputStream(activityObject.generate().getBytes());
	}

	private NodeList getNodesWithId(InputStream inputStream) throws ParserConfigurationException, SAXException,
			IOException, XPathExpressionException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(inputStream);
		XPathFactory pathFactory = XPathFactory.newInstance();
		XPath xPath = pathFactory.newXPath();
		XPathExpression expression = xPath.compile("//*[@id]");
		return (NodeList) expression.evaluate(doc, XPathConstants.NODESET);
	}

	public String getFileName(String filePath) {
		return "/" + new ActivityResource(getFileNameFromPath(filePath)).getVariableName() + ".java";
	}
	
	public static String getFileNameFromPath(String filePath) {
		// Pattern pattern = Pattern.compile("[^\\/]+.xml$");
		// Matcher matcher = pattern.matcher(filePath);
		// matcher.find();
		// return matcher.group().replaceFirst(".xml", "");
		return new File(filePath).getName().replaceFirst(".xml", "");
	}

	// Pattern pattern = Pattern.compile("_\\w");
	// Matcher matcher = pattern.matcher(pLayoutName);
	// while(matcher.find()){
	// int start = matcher.start();
	// int end = matcher.end();
	// }
	public static String getActivityName(String pLayoutName) {
		String[] words = pLayoutName.split("_");
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			stringBuilder.append(Character.toUpperCase(word.charAt(0)) + word.substring(1));
		}
		return stringBuilder.toString() + "Activity";
	}

	private static String getIdNameFromId(String pId) {
		if (pId.startsWith("@+id/")) {
			return pId.substring(5);
		}
		throw new IllegalArgumentException("");
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
		String idName = getIdNameFromId(pId);

		return "R.id." + idName;
	}

	public static String getFirstLowerCase(String word) {
		return Character.toLowerCase(word.charAt(0)) + word.substring(1);
	}
}
