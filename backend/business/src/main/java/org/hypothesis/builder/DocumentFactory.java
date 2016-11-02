/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.builder;

import org.apache.commons.lang3.StringUtils;
import org.hypothesis.common.utility.DocumentUtility;
import org.hypothesis.interfaces.Document;
import org.hypothesis.interfaces.DocumentConstants;
import org.hypothesis.interfaces.Element;

import java.util.*;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
public final class DocumentFactory {

	private DocumentFactory() {
	}

	public static Document mergeSlideDocument(Document template, Document content) {
		DocumentImpl document = new DocumentImpl();
		Element root = document.createRoot(DocumentConstants.SLIDE);

		// copy template
		template.root().children().forEach(root::createChild);

		// bind slide content
		Element element = content.root().selectElement(DocumentConstants.BINDINGS);
		element.selectElements(DocumentConstants.BIND).stream().map(m -> m.firstChild()).filter(Objects::nonNull)
				.forEach(e -> {
					String name = e.getName();
					String id = e.getAttribute(DocumentConstants.ID);

					if (StringUtils.isNotEmpty(name)) {
						Map<String, String> attributes = null;
						boolean searchDescendants = false;

						if (StringUtils.isNotEmpty(id)) {
							attributes = new HashMap<>();
						attributes.put(DocumentConstants.ID, id);
							searchDescendants = true;
						}

						Element origElement = DocumentUtility.findElementByNameAndValue(root, name, attributes,
								searchDescendants);
						if (origElement != null) {
							mergeElementAttributes(origElement, e);
							e.children().forEach(i -> mergeBindingNodes(origElement, i));
							}
						}
				});

		return document;
	}

	private static void mergeElementAttributes(Element destination, Element source) {
		source.attributes().entrySet().forEach(e -> destination.setAttribute(e.getKey(), e.getValue()));
		}

	private static void mergeBindingNodes(Element destinationElement, Element sourceSubElement) {
		String name = sourceSubElement.getName();
		String id = sourceSubElement.getAttribute(DocumentConstants.ID);

		Element destinationSubElement;
		if (StringUtils.isNotEmpty(id)) {
			Map<String, String> attributes = new HashMap<>();
			attributes.put(DocumentConstants.ID, id);
			destinationSubElement = DocumentUtility.findElementByNameAndValue(destinationElement, name, attributes,
					false);
		} else {
			destinationSubElement = DocumentUtility.findElementByNameAndValue(destinationElement, name, null, false);
		}
		if (destinationSubElement == null) {
			destinationSubElement = destinationElement.createChild(name);
		}

		mergeElements(destinationSubElement, sourceSubElement);
	}

	private static void mergeElements(Element destination, Element source) {
		mergeElementAttributes(destination, source);

		destination.setText(source.getText());

		List<Element> destSubElements = new ArrayList<>();

		boolean destSubEmpty = destSubElements.isEmpty();

		source.children().forEach(e -> {
			String name = e.getName();
			String id = e.getAttribute(DocumentConstants.ID);

			Element destinationSubElement = null;

			if (!destSubEmpty) {
				Map<String, String> attributes = null;
				if (StringUtils.isNotEmpty(id)) {
					attributes = new HashMap<>();
					attributes.put(DocumentConstants.ID, id);
				}

				destinationSubElement = DocumentUtility.findElementByNameAndValue(destination, name, attributes, false);

				if (StringUtils.isEmpty(id) && destSubElements.contains(destinationSubElement)) {
					// if previously created element found then skip to avoid
					// rewrite
						destinationSubElement = null;
					}
				}

			if (destinationSubElement == null) {
				destinationSubElement = destination.createChild(name);
			}

			mergeElements(destinationSubElement, e);
		});
		}

	public static Document createEventDataDocument() {
		Document document = new DocumentImpl();
		document.createRoot(DocumentConstants.EVENT_DATA);

		return document;
	}

}
