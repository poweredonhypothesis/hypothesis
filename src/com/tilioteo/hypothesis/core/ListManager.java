/**
 * 
 */
package com.tilioteo.hypothesis.core;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.tilioteo.hypothesis.entity.HasList;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
@SuppressWarnings("serial")
public class ListManager<T extends HasList<E>, E> implements Serializable {

	private LinkedList<E> list = new LinkedList<E>();
	private int index = -1;

	private E element = null;
	
	private Random randomGenerator;
	
	private E getByInternalIndex() {
		if (index >= 0 && index < list.size()) {
			element = list.get(index);
		} else {
			element = null;
		}
		return element;
	}

	public E current() {
		return getByInternalIndex();
	}
	
	public E get(int index) {
		if (index >= 0 && index < list.size()) {
			this.index = index;
		} else {
			this.index = -1;
		}
		return getByInternalIndex();
	}

	public E find(E item) {
		index = list.indexOf(item);
		return getByInternalIndex();
	}

	public E next() {
		if (index < list.size()) {
			++index;
		}
		return getByInternalIndex();
	}
	
	public E prior() {
		if (index >= 0) {
			--index;
		}
		return getByInternalIndex();
	}

	/*
	 * set current element - for test purpose only
	 * 
	 * @param element
	 */
	/*public void setCurrent(E element) {
		this.element = element;
	}*/

	public void setListFromParent(T parent) {
		list.clear();
		index = -1;
		randomGenerator = new Random();
		if (parent != null) {
			for (E item : parent.getList()) {
				if (item != null) {
					list.add(item);
				}
			}
			
			if (list.size() > 0) {
				index = 0;
				getByInternalIndex();
			}
		}
	}
	
	public List<Integer> createRandomOrder() {
		LinkedList<Integer> order = new LinkedList<Integer>();
		
		while (order.size() < list.size()) {
			Integer random = randomGenerator.nextInt(list.size());
			if (!order.contains(random)) {
				order.add(random);
			}
		}
		
		return order;
	}
	
	public void setOrder(List<Integer> order) {
		if (order.size() > 0 && list.size() > 0) {
			LinkedList<E> tempList = new LinkedList<>();
			
			int size = Math.min(order.size(), list.size());
			for (int i = 0; i < size; ++i) {
				tempList.add(list.get(order.get(i)));
			}
			
			for (int i = size; i < list.size(); ++i) {
				tempList.add(list.get(i));
			}
			
			this.list = tempList;
		}
		index = 0;
	}
	
}
