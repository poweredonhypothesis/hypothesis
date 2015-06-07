/**
 * 
 */
package com.tilioteo.hypothesis.core;

import java.io.Serializable;

/**
 * @author kamil
 *
 */
@SuppressWarnings("serial")
public class TaskNavigator implements Serializable {
	
	private Node node;
	
	public TaskNavigator(Node node) {
		this.node = node;
	}

	public void nextIndex(int index) {
		node.setNextIndex(index);
	}
	
	public void next() {
		node.setNextIndex(0);
	}
	
	public void nextTask() {
		node.setNextIndex(-1);
	}
	
}
