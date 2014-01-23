/**
 * 
 */
package com.tilioteo.hypothesis.event;

import com.vaadin.ui.Notification;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
@SuppressWarnings("serial")
public abstract class AbstractNotificationEvent extends AbstractProcessEvent {

	protected String caption;
	protected String description;

	protected AbstractNotificationEvent(String caption) {
		this(caption, null);
	}

	protected AbstractNotificationEvent(String caption, String description) {
		super(new Object());
		this.caption = caption;
		this.description = description;
	}

	public abstract Notification getNotification();
}