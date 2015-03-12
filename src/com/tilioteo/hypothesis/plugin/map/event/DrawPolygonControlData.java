/**
 * 
 */
package com.tilioteo.hypothesis.plugin.map.event;

import org.dom4j.Element;
import org.vaadin.maps.ui.feature.VectorFeature;

import com.tilioteo.hypothesis.core.SlideManager;
import com.tilioteo.hypothesis.event.AbstractComponentData;
import com.tilioteo.hypothesis.plugin.map.SlideFactory;
import com.tilioteo.hypothesis.plugin.map.ui.DrawPolygonControl;

/**
 * @author kamil
 *
 */
public class DrawPolygonControlData extends AbstractComponentData<DrawPolygonControl> {

	private VectorFeature feature = null;

	public DrawPolygonControlData(DrawPolygonControl sender, SlideManager slideManager) {
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
		SlideFactory.writeDrawPolygonControlData(element, this);
	}

}
