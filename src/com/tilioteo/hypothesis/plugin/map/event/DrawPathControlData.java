/**
 * 
 */
package com.tilioteo.hypothesis.plugin.map.event;

import org.dom4j.Element;
import org.vaadin.maps.ui.feature.VectorFeature;

import com.tilioteo.hypothesis.core.SlideManager;
import com.tilioteo.hypothesis.event.AbstractComponentData;
import com.tilioteo.hypothesis.plugin.map.SlideFactory;
import com.tilioteo.hypothesis.plugin.map.ui.DrawPathControl;

/**
 * @author kamil
 *
 */
public class DrawPathControlData extends AbstractComponentData<DrawPathControl>  {
	
	private VectorFeature feature = null;

	public DrawPathControlData(DrawPathControl sender, SlideManager slideManager) {
		super(sender, slideManager);
	}

	public VectorFeature getFeature() {
		return feature;
	}

	public void setFeature(VectorFeature feature) {
		this.feature = feature;
	}

	@Override
	public void writeDataToElement(Element element) {
		SlideFactory.writeDrawPathControlData(element, this);
	}

}
