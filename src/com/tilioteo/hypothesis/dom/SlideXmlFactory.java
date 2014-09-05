/**
 * 
 */
package com.tilioteo.hypothesis.dom;

import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.tilioteo.hypothesis.common.Strings;
import com.tilioteo.hypothesis.entity.Slide;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
public class SlideXmlFactory {

	private static Logger log = Logger.getLogger(SlideXmlFactory.class);

	@SuppressWarnings("unchecked")
	private static Document buildSlideXml(Document slideTemplate,
			Document slideContent) {
		log.debug(String.format("buildSlideXml(...): template UID = %s", getTemplateUID(slideTemplate)));
		Document doc = XmlUtility.createDocument();
		Element root = doc.addElement(SlideXmlConstants.SLIDE);

		// copy template
		List<Element> templateNodes = slideTemplate.getRootElement().elements();
		for (Element node : templateNodes) {
			root.add((Node) node.clone());
		}

		// bind component content
		List<Node> contentNodes = slideContent.getRootElement().selectNodes(
				String.format("%s//%s", SlideXmlConstants.BINDINGS,
						SlideXmlConstants.BIND));
		for (Node node : contentNodes) {
			if (node instanceof Element) {
				Element element = (Element) ((Element) node).elements().get(0);
				if (element != null) {
					String name = element.getName();
					String prefix = element.getNamespacePrefix();
					String uri = element.getNamespaceURI();
					
					String id = SlideXmlUtility.getId(element);
					if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(id)) {
						Element origElement = XmlUtility.findElementByNameAndValue(true, root, name, prefix, uri, SlideXmlConstants.ID, id);
						if (origElement != null) {
							mergeElementAttributes(origElement, element);
							List<Element> bindNodes = element.elements();

							for (Element bindNode : bindNodes) {
								mergeBindingNodes(doc, origElement, bindNode);
							}
						}
					}
				}
			}
		}

		// TODO
		// override evaluator
		/*
		 * Node slideNode = root.selectSingleNode(SlideXmlConstants.EVALUATION);
		 * if (slideNode == null) { slideNode =
		 * root.addElement(SlideXmlConstants.EVALUATION); }
		 * 
		 * for (Iterator<Node> i = slideContent.getRootElement().selectNodes(
		 * String.format("%s/%s", SlideXmlConstants.EVALUATION,
		 * SlideXmlConstants.EVALUATE)).iterator(); i.hasNext();) { Node
		 * evalNode = i.next();
		 * 
		 * ((Element)slideNode).add((Node)evalNode.clone()); }
		 */

		//String xmlString = XmlUtility.writeString(doc);
		return doc;
	}
	
	/**
	 * for logging purpose only
	 * @return
	 */
	private static String getTemplateUID(Document slideTemplate) {
		return slideTemplate.getRootElement().attributeValue(SlideXmlConstants.UID);
	}

	public static Document buildSlideXml(Slide slide) {
		if (null == slide) {
			log.warn("getSlideXml(null)");
			return null;
		}
		
		log.debug(String.format("getSlideXml(slide:%d)", slide.getId()));

		Document slideTemplate = slide.getContent().getTemplateDocument();
		Document slideContent = slide.getContent().getDocument();

		if (slideTemplate == null)
			log.warn(String.format("Template document is NULL (slide:%d)", slide.getId()));
		if (slideContent == null)
			log.warn(String.format("Content document is NULL (slide:%d)", slide.getId()));

		try {
			if (slideTemplate != null && slideContent != null) {
				return buildSlideXml(slideTemplate, slideContent);
			} else {
				return null;
			}
		} catch (Throwable t) {
			log.error(String.format("getSlideXml(slide:%d)",
					slide != null ? slide.getId() : -1), t);
			return null;
		}
	}

	public static Document createEventDataXml() {
		Document eventData = XmlUtility.createDocument();
		eventData.addElement(SlideXmlConstants.EVENT_DATA);
		return eventData;
	}

	public static Document createSlideDataXml() {
		Document slideData = XmlUtility.createDocument();
		slideData.addElement(SlideXmlConstants.SLIDE_DATA);
		return slideData;
	}

	public static Document createSlideOutputXml() {
		Document slideOutput = XmlUtility.createDocument();
		slideOutput.addElement(SlideXmlConstants.SLIDE_OUTPUT);
		return slideOutput;
	}

	private static void mergeBindingNodes(Document doc,	Element destinationElement, Element sourceSubElement) {
		log.debug("mergeBindingNodes(...)");
		
		String name = sourceSubElement.getName();
		String prefix = sourceSubElement.getNamespacePrefix();
		String uri = sourceSubElement.getNamespaceURI();
		
		String id = SlideXmlUtility.getId(sourceSubElement);
		
		Element destinationSubElement = null;
		if (!Strings.isNullOrEmpty(id)) {
			destinationSubElement = XmlUtility.findElementByNameAndValue(false, destinationElement, name, prefix, uri, SlideXmlConstants.ID, id);
		} else {
			destinationSubElement = XmlUtility.findElementByNameAndValue(false, destinationElement, name, prefix, uri, null, null);
		}
		if (destinationSubElement == null) {
			destinationSubElement = destinationElement.addElement(name);
		}

		mergeElements(destinationSubElement, sourceSubElement);
	}

	@SuppressWarnings("unchecked")
	private static void mergeElementAttributes(Element destination,
			Element source) {
		log.debug("mergeElementAttributes(...)");
		
		List<Attribute> sourceAttributes = source.attributes();
		for (Attribute sourceAttribute : sourceAttributes) {
			destination.addAttribute(sourceAttribute.getName(),
					sourceAttribute.getValue());
		}
	}

	@SuppressWarnings("unchecked")
	private static void mergeElements(Element destination, Element source) {
		mergeElementAttributes(destination, source);

		List<Node> sourceNodes = source.selectNodes("*");
		for (Node sourceNode : sourceNodes) {
			if (sourceNode instanceof Element) {
				Element sourceSubElement = (Element)sourceNode;
				String name = sourceSubElement.getName();
				String prefix = sourceSubElement.getNamespacePrefix();
				String uri = sourceSubElement.getNamespaceURI();
				
				String id = SlideXmlUtility.getId(sourceSubElement);
				
				Element destinationSubElement = null;
				if (!Strings.isNullOrEmpty(id)) {
					destinationSubElement = XmlUtility.findElementByNameAndValue(false, destination, name, prefix, uri, SlideXmlConstants.ID, id);
				} else {
					destinationSubElement = XmlUtility.findElementByNameAndValue(false, destination, name, prefix, uri, null, null);
				}
				if (destinationSubElement == null) {
					destinationSubElement = destination.addElement(name);
				}
					//destination.add((Node) sourceNode.clone());
				//} else {
					mergeElements(destinationSubElement, sourceSubElement);
			}
		}
	}

}
