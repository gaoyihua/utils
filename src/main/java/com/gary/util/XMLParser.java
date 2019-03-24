package com.gary.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class XMLParser {
	private static final DocumentBuilderFactory dbf; 

	static {
		dbf = DocumentBuilderFactory.newInstance();
	}
	
	public XMLParser() {
	}
	
	public abstract void dealElement(Element element, int index);

	private void traversal(NodeList elements) {
		for (int index = 0; index < elements.getLength(); index++) {
			Element ele = (Element) elements.item(index);
			// 对于element的处理应该是由XMLParser的使用者来决定的！
			dealElement(ele, index);
		}
	}

	public void dealElementInTag(Element element, String tagName) {
		NodeList elements = element.getElementsByTagName(tagName);
		traversal(elements);
	}
	
	public void dealElementInTag(Document document, String tagName) {
		NodeList elements = document.getElementsByTagName(tagName);
		traversal(elements);
	}
	
	public static  Document getDocument(String xmlPath) {
		Document document = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputStream is = XMLParser.class.getResourceAsStream(xmlPath);
			document = db.parse(is);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return document;
	}
	
}
