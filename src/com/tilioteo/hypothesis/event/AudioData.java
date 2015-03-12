/**
 * 
 */
package com.tilioteo.hypothesis.event;

import org.dom4j.Element;

import com.tilioteo.hypothesis.core.SlideFactory;
import com.tilioteo.hypothesis.core.SlideManager;
import com.tilioteo.hypothesis.ui.Audio;

/**
 * @author kamil
 *
 */
public class AudioData extends AbstractComponentData<Audio> {

	private double time = 0.0;

	public AudioData(Audio sender, SlideManager slideManager) {
		super(sender, slideManager);
	}

	public final double getTime() {
		return time;
	}
	
	public final void setTime(double time) {
		this.time = time;
	}

	@Override
	public void writeDataToElement(Element element) {
		SlideFactory.writeAudioData(element, this);
	}

}
