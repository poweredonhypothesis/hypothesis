/**
 * 
 */
package com.tilioteo.hypothesis.plugin.processing;

import org.dom4j.Element;

import com.tilioteo.hypothesis.plugin.processing.event.ProcessingData;

/**
 * @author kamil
 *
 */
public class SlideFactory {

	public static void writeProcessingData(Element sourceElement, ProcessingData data) {
		String id = data.getComponentId();
		sourceElement.addAttribute(SlideXmlConstants.TYPE, SlideXmlConstants.CALLBACK);
		if (id != null)
			sourceElement.addAttribute(SlideXmlConstants.ID, id);
		
		writeName(sourceElement, data.getName());
	}
	
	private static void writeName(Element sourceElement, String name) {
		Element subElement = sourceElement.addElement(SlideXmlConstants.NAME);
		subElement.addText(name);
	}

}
