/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.servlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.hypothesis.ui.MainUI;

import com.vaadin.annotations.VaadinServletConfiguration;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
@SuppressWarnings("serial")
@WebServlet(value = "/*", asyncSupported = true, name = "main-servlet", initParams = {
		@WebInitParam(name = "UIProvider", value = "org.hypothesis.provider.MainUIProvider") })
@VaadinServletConfiguration(productionMode = false, ui = MainUI.class, widgetset = "org.hypothesis.WidgetSet")
public class MainServlet extends HibernateVaadinServlet {

}