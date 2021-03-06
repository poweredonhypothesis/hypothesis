/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.ui.view;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import org.hypothesis.annotations.RolesAllowed;
import org.hypothesis.annotations.Title;
import org.hypothesis.interfaces.RoleType;
import org.hypothesis.interfaces.SlideManagementPresenter;
import org.hypothesis.ui.MainUI;
import org.vaadin.aceeditor.AceEditor;
import org.vaadin.aceeditor.AceMode;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
@SuppressWarnings("serial")
@CDIView(value = "/slides", uis = { MainUI.class })
@Title(value = "Caption.View.Slides", icon = FontAwesome.FILE_CODE_O, index = 7)
@RolesAllowed(value = { RoleType.MANAGER, RoleType.SUPERUSER })
public class SlideManagementView extends HorizontalLayout implements View {

	@Inject
	private SlideManagementPresenter presenter;

	private AceEditor editor1;
	private AceEditor editor2;

	public SlideManagementView() {
		setSizeFull();
	}

	private void buildContent() {
		removeAllComponents();

		Panel contentPanel = buildContentPanel();
		addComponent(contentPanel);
		setExpandRatio(contentPanel, 1.0f);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		presenter.enter(event);
	}

	private Panel buildContentPanel() {
		Panel panel = new Panel();
		panel.setSizeFull();

		VerticalLayout mainLayout = new VerticalLayout();
		mainLayout.setSizeFull();
		panel.setContent(mainLayout);

		Panel controlPanel = new Panel();
		controlPanel.setHeight("50px");
		controlPanel.setWidth(100, Unit.PERCENTAGE);
		mainLayout.addComponent(controlPanel);

		Button showButton = new Button("Show");
		showButton.addClickListener(e -> showSlide());

		controlPanel.setContent(showButton);

		// VerticalSplitPanel splitPanel = new VerticalSplitPanel();
		// splitPanel.setSizeFull();
		// mainLayout.addComponent(splitPanel);
		// mainLayout.setExpandRatio(splitPanel, 1.0f);

		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		mainLayout.addComponent(verticalLayout);
		mainLayout.setExpandRatio(verticalLayout, 1.0f);

		editor1 = new AceEditor();
		editor1.setSizeFull();
		editor1.setMode(AceMode.xml);
		// editor1.setTheme("ace/theme/eclipse");

		editor2 = new AceEditor();
		editor2.setSizeFull();
		editor2.setMode(AceMode.xml);

		// splitPanel.setFirstComponent(editor1);
		// splitPanel.setSecondComponent(editor2);

		verticalLayout.addComponent(editor1);
		// verticalLayout.setExpandRatio(editor1, 0.5f);

		verticalLayout.addComponent(editor2);
		// verticalLayout.setExpandRatio(editor2, 0.5f);

		return panel;
	}

	protected void showSlide() {
		presenter.showSlide(editor1.getValue(), editor2.getValue());
	}

	@PostConstruct
	public void postConstruct() {
		buildContent();
	}
}
