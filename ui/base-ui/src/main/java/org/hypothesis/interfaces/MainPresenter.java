/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.interfaces;

import org.hypothesis.ui.MainScreen;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
public interface MainPresenter extends ComponentPresenter {

	public Component buildTopPanel();

	public Component buildMainPane();

	public Component buildBottomPanel();

	public ComponentContainer getContent();

	public MainScreen createScreen();

}