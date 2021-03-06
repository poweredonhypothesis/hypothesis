/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.event.model;

import com.vaadin.ui.Notification;
import org.hypothesis.event.interfaces.ProcessViewEvent;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
@SuppressWarnings("serial")
public abstract class AbstractNotificationEvent implements ProcessViewEvent {

	protected final String caption;
	protected final String description;

	protected AbstractNotificationEvent(String caption) {
		this(caption, null);
	}

	protected AbstractNotificationEvent(String caption, String description) {
		this.caption = caption;
		this.description = description;
	}

	public String getCaption() {
		return caption;
	}

	public String getDescription() {
		return description;
	}

	public abstract Notification getNotification();
}
