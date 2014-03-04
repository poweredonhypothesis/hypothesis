/**
 * 
 */
package com.tilioteo.hypothesis.ui.view;

import java.util.List;

import com.tilioteo.hypothesis.entity.Pack;
import com.tilioteo.hypothesis.model.PacksModel;
import com.tilioteo.hypothesis.ui.BrowserApplet;
import com.tilioteo.hypothesis.ui.PackPanel;
import com.tilioteo.hypothesis.ui.PackPanel.StartEvent;
import com.tilioteo.hypothesis.ui.PackPanel.StartListener;
import com.vaadin.event.MouseEvents;
import com.vaadin.event.MouseEvents.ClickEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

/**
 * @author kamil
 *
 */
@SuppressWarnings("serial")
public class PacksView extends HypothesisView implements MouseEvents.ClickListener, StartListener {
	
	private PacksModel packsModel;
	private VerticalLayout packLayout;
	private BrowserApplet applet;
	
	public PacksView() {
		
		packsModel = new PacksModel();
		
		setSizeFull();
		
		VerticalLayout verticalLayout = new VerticalLayout();
		verticalLayout.setSizeFull();
		addComponent(verticalLayout);
		
		Panel topPanel = new Panel();
		topPanel.setWidth("100%");
		topPanel.setHeight("100px");
		verticalLayout.addComponent(topPanel);
		
		VerticalLayout panelLayout = new VerticalLayout();
		panelLayout.setSizeFull();
		topPanel.setContent(panelLayout);
		
		Label label = new Label("<H1>Hypothesis<H1>");
		label.setContentMode(ContentMode.HTML);
		label.setWidth(null);
		panelLayout.addComponent(label);
		panelLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);
		panelLayout.setMargin(true);		
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setSizeFull();
		verticalLayout.addComponent(horizontalLayout);
		verticalLayout.setExpandRatio(horizontalLayout, 1.0f);
		
		Panel leftPanel = new Panel();
		leftPanel.setSizeFull();
		
		applet = new BrowserApplet();
		applet.setWidth("100px");
		applet.setHeight("100px");
		leftPanel.setContent(applet);
		

		horizontalLayout.addComponent(leftPanel);
		horizontalLayout.setExpandRatio(leftPanel, 0.2f);
		
		Panel mainPanel = new Panel();
		mainPanel.setSizeFull();
		horizontalLayout.addComponent(mainPanel);
		horizontalLayout.setExpandRatio(mainPanel, 1.0f);
		
		packLayout = new VerticalLayout();
		packLayout.setHeight(null);
		packLayout.setWidth("100%");
		mainPanel.setContent(packLayout);

		List<Pack> packs = packsModel.getPublicPacks();
				
		for (Pack pack : packs) {
			PackPanel packPanel = new PackPanel(pack);
			packPanel.addClickListener(this);
			packPanel.addStartListener(this);
			
			packLayout.addComponent(packPanel);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void click(ClickEvent event) {
		for (Component component : packLayout) {
			if (component instanceof PackPanel) {
				PackPanel packPanel = (PackPanel)component;
				if (packPanel != event.getComponent()) {
					packPanel.collapse();
				} else if (packPanel.isCollapsed()) {
					packPanel.rise();
				} else {
					packPanel.collapse();
				}
			}
		}
	}

	@Override
	public void start(StartEvent event) {
		packsModel.startTest(event.getPack(), applet);
	}

}
