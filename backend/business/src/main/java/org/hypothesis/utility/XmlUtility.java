/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.utility;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
@SuppressWarnings("serial")
public class XmlUtility implements Serializable {

	private static final String ENCODING_UTF8 = "utf-8";
	public static final String DESCENDANT_FMT = "descendant::%s";

	/**
	 * Remove all children
	 * 
	 * @param parent
	 */
	@SuppressWarnings("unchecked")
	public static void clearAllChilds(Node parent) {
		if (parent != null) {
			((List<Node>) parent.selectNodes("")).forEach(e -> e.detach());
		}
	}

	/*
	 * public static boolean writeFile(final Document doc, final File file) { if
	 * (doc != null && file.getName().length() > 0) { try { OutputFormat
	 * outputFormat = OutputFormat.createPrettyPrint();
	 * outputFormat.setEncoding(doc.getXMLEncoding());
	 * 
	 * XMLWriter writer = new XMLWriter(new FileWriter(file), outputFormat);
	 * writer.write(doc); writer.close(); return true; } catch (Throwable e) { }
	 * }
	 * 
	 * return false; }
	 */

	/**
	 * Create new document with UTF8 encoding
	 * 
	 * @return
	 */
	public static Document createDocument() {
		Document doc = DocumentFactory.getInstance().createDocument();
		doc.setXMLEncoding(ENCODING_UTF8);

		return doc;
	}

	/**
	 * Find attribude by name
	 * 
	 * @param node
	 * @param name
	 * @return
	 */
	public static Attribute findAttributeByName(Node node, String name) {
		if (node != null && name.length() > 0) {
			if (node instanceof Element) {
				Element el = (Element) node;
				return el.attribute(name);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static Element findElementByNameAndValue(boolean descendant, Element element, String name, String prefix,
			String uri, final String attributeName, final String attributeValue) {
		Element result = null;
		if (element != null) {
			Map<String, String> namespaces = new HashMap<>();
			if (StringUtils.isNotEmpty(prefix) && StringUtils.isNotEmpty(uri)) {
				name = String.format("%s:%s", prefix, name);
				namespaces.put(prefix, uri);
			}

			XPath path = element.createXPath(descendant ? String.format(XmlUtility.DESCENDANT_FMT, name) : name);
			if (namespaces.size() > 0) {
				path.setNamespaceURIs(namespaces);
			}

			Stream<Element> stream = path.selectNodes(element).stream().filter(f -> f instanceof Element)
					.map(m -> m);

			result = attributeName != null ? stream.filter(f -> {
				Attribute attr = f.attribute(attributeName);
				return attr != null && attr.getValue().equals(attributeValue);
			}).findFirst().orElse(null) : stream.findFirst().orElse(null);

			/*
			 * for (Iterator<Node> i = nodes.iterator(); i.hasNext();) { Node
			 * node = i.next();
			 * 
			 * if (node instanceof Element) { Element el = (Element) node;
			 * 
			 * if (attributeName != null) { Attribute attr =
			 * el.attribute(attributeName); if (attr != null &&
			 * attr.getValue().equals(attributeValue)) { result = el; break; } }
			 * else { result = el; break; } } }
			 */
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static Node findFirstNodeByName(Node parent, String name) {
		if (parent != null && StringUtils.isNotBlank(name)) {
			return ((List<Node>) parent.selectNodes(String.format(XmlUtility.DESCENDANT_FMT, name))).stream()
					.filter(f -> f.getName().equals(name)).findFirst().orElse(null);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static List<Node> findNodesByNameStarting(Node parent, String startName) {
		if (parent != null && startName.length() > 0) {
			return parent.selectNodes("./*[starts-with(name(), '" + startName + "')]");
		}

		return null;
	}

	public static Document readFile(File file) {
		if (file.exists()) {
			try {
				SAXReader reader = new SAXReader();
				Document doc = reader.read(file);
				return doc;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static Document readString(String xmlString) {
		if (xmlString != null && xmlString.length() > 0) {
			try {
				StringReader stringReader = new StringReader(xmlString);
				SAXReader reader = new SAXReader();
				// reader.setFeature("http://xml.org/sax/features/namespaces",
				// false);
				Document doc = reader.read(stringReader);
				return doc;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public static String writeString(Document doc) {
		String string = null;
		if (doc != null) {
			try {
				StringWriter stringWriter = new StringWriter();
				XMLWriter writer = new XMLWriter(stringWriter);
				writer.write(doc);
				writer.close();
				string = stringWriter.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return string;
	}
}
