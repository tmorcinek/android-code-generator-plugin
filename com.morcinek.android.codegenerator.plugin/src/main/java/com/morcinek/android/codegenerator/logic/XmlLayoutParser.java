package com.morcinek.android.codegenerator.logic;

import java.io.ByteArrayInputStream;
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

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.morcinek.android.codegenerator.logic.util.ActivityResource;
import com.morcinek.android.codegenerator.logic.util.StringUtils;
import com.morcinek.android.codegenerator.logic.util.WidgetResource;
import com.morcinek.android.codegenerator.plugin.Activator;

public class XmlLayoutParser {

	private final String packageName;

	private final boolean pShortMode;

	private TypesAdapter typesAdapter;

	private ILog log;

	public XmlLayoutParser(boolean pShortMode, String packageName, TypesAdapter typesAdapter) {
		this.pShortMode = pShortMode;
		this.packageName = packageName;
		this.typesAdapter = typesAdapter;
	}

	public ILog getLog() {
		return log;
	}

	public void setLog(ILog log) {
		this.log = log;
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
		String layoutName = StringUtils.getFileNameFromPath(filePath);

		NodeList nodeList = getNodesWithId(inputStream);
		ActivityObject activityObject = new ActivityObject(pShortMode);
		activityObject.setTypesAdapter(typesAdapter);
		activityObject.setActivityResource(new ActivityResource(layoutName));
		activityObject.setPackageName(packageName);
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			String id = node.getAttributes().getNamedItem("android:id").getNodeValue();
			String typeName = node.getNodeName();
			WidgetResource widget = new WidgetResource(id);
			try {
				activityObject.addWidget(widget, typeName);
				log.log(new Status(Status.INFO, Activator.PLUGIN_ID, "Type: " + typeName + " added."));
			} catch (IllegalArgumentException e) {
				log.log(new Status(Status.ERROR, Activator.PLUGIN_ID, "Type: " + typeName + " was not recognized."));
			}
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
		return "/" + StringUtils.getActivityNameFromId(StringUtils.getFileNameFromPath(filePath)) + ".java";
	}

}
