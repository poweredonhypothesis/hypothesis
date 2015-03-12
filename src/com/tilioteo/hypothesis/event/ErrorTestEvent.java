/**
 * 
 */
package com.tilioteo.hypothesis.event;

import com.tilioteo.hypothesis.entity.SimpleTest;
import com.tilioteo.hypothesis.entity.Status;
import com.vaadin.server.ErrorHandler;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
@SuppressWarnings("serial")
public class ErrorTestEvent extends AbstractTestEvent {

	// TODO add reason
	// private String reason;

	public ErrorTestEvent(SimpleTest test) {
		this(test, null);
	}

	public ErrorTestEvent(SimpleTest test, ErrorHandler errorHandler) {
		super(test, errorHandler);
	}

	@Override
	public String getName() {
		return ProcessEventTypes.TestError;
	}

	@Override
	public Status getStatus() {
		return Status.BROKEN_BY_ERROR;
	}

}
