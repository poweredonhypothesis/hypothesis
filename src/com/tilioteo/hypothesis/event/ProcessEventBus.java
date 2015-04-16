/**
 * 
 */
package com.tilioteo.hypothesis.event;

/**
 * @author kamil
 *
 */
public class ProcessEventBus extends HypothesisEventBus {

	private static final ProcessEventBus instance = new ProcessEventBus();
	
	public static final ProcessEventBus get() {
		return instance;
	}
}
