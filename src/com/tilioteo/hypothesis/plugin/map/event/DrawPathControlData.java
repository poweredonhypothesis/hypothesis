/**
 * 
 */
package com.tilioteo.hypothesis.plugin.map.event;

import org.dom4j.Element;
import org.vaadin.maps.ui.feature.VectorFeature;

import com.tilioteo.hypothesis.event.AbstractComponentData;
import com.tilioteo.hypothesis.interfaces.SlideFascia;
import com.tilioteo.hypothesis.plugin.map.SlideFactory;
import com.tilioteo.hypothesis.plugin.map.ui.DrawPathControl;

/**
 * @author kamil
 *
 */
@SuppressWarnings("serial")
public class DrawPathControlData extends AbstractComponentData<DrawPathControl>  {
	
	private VectorFeature feature = null;

	public DrawPathControlData(DrawPathControl sender, SlideFascia slideFascia) {
		super(sender, slideFascia);
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
