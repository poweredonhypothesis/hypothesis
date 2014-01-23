/**
 * 
 */
package com.tilioteo.hypothesis.ui;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

import com.tilioteo.hypothesis.common.StringMap;
import com.tilioteo.hypothesis.common.Strings;
import com.tilioteo.hypothesis.core.SlideFactory;
import com.tilioteo.hypothesis.core.SlideManager;
import com.tilioteo.hypothesis.core.SlideUtility;
import com.tilioteo.hypothesis.dom.SlideXmlConstants;
import com.tilioteo.hypothesis.event.ButtonPanelData;
import com.tilioteo.hypothesis.model.AbstractBaseAction;
import com.tilioteo.hypothesis.model.Command;
import com.tilioteo.hypothesis.model.CommandFactory;
import com.vaadin.ui.Alignment;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
@SuppressWarnings("serial")
public class ButtonPanel extends MultipleComponentPanel<Button> implements
		SlideComponent {

	private SlideManager slideManager;
	private ParentAlignment parentAlignment;

	private List<Button.ClickListener> clickListeners = new ArrayList<Button.ClickListener>();
	Button selected = null;
	private String[] captions;

	public ButtonPanel() {
		this.parentAlignment = new ParentAlignment();
	}

	public ButtonPanel(SlideManager slideManager) {
		this();
		this.slideManager = slideManager;

		setStyleName("light");
		setSizeUndefined();
	}

	public void addButtonClickListener(Button.ClickListener buttonClickListener) {
		this.clickListeners.add(buttonClickListener);
	}

	@Override
	protected void addChildsTo(List<Button> list) {
		int i = 1;
		for (String caption : captions) {
			Button button = new Button();
			button.setCaption(caption);
			button.setData(String.format("%s_%d",
					this.getData() != null ? (String) this.getData() : "", i++));

			for (Button.ClickListener listener : clickListeners)
				button.addClickListener(listener);

			list.add(button);
		}
	}

	public Alignment getAlignment() {
		return parentAlignment.getAlignment();
	}

	public Button getSelected() {
		return selected;
	}

	public void loadFromXml(Element element) {
		// if (element.getName().equals(SlideXmlConstants.BUTTON_PANEL)) {

		setProperties(element);
		setHandlers(element);

	}

	private void setClickHandler(String actionId) {
		final ButtonPanelData data = new ButtonPanelData(this, slideManager);
		final Command componentEvent = CommandFactory
				.createButtonPanelClickEventCommand(data);
		final Command action = CommandFactory.createActionCommand(slideManager,
				actionId);

		addButtonClickListener(new Button.ClickListener() {
			public void buttonClick(Button.ClickEvent event) {
				data.setButton((Button) event.getSource());
				componentEvent.execute();
				action.execute();
			}
		});
	}

	protected void setHandler(Element element) {
		String name = element.getName();
		String action = null;
		AbstractBaseAction anonymousAction = SlideFactory.getInstatnce()
				.createAnonymousAction(element);
		if (anonymousAction != null)
			action = anonymousAction.getId();

		if (!Strings.isNullOrEmpty(action)) {
			if (name.equals(SlideXmlConstants.CLICK)) {
				setClickHandler(action);
			}
			// TODO add other event handlers
		}
	}

	private void setHandlers(Element element) {
		List<Element> handlers = SlideUtility.getHandlerElements(element);

		for (Element handler : handlers) {
			setHandler(handler);
		}
	}

	protected void setProperties(Element element) {
		StringMap properties = SlideUtility.getPropertyValueMap(element);

		this.captions = properties.getStringArray(SlideXmlConstants.CAPTIONS);

		ComponentUtility.setComponentPanelProperties(this, element, properties,
				parentAlignment);
	}

	public void setSelected(Button button) {
		this.selected = button;
	}

	public void setSlideManager(SlideManager slideManager) {
		this.slideManager = slideManager;
	}
}