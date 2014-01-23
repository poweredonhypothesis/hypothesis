/**
 * 
 */
package com.tilioteo.hypothesis.model;

import com.tilioteo.hypothesis.core.SlideManager;
import com.tilioteo.hypothesis.event.AbstractComponentEvent;
import com.tilioteo.hypothesis.event.ButtonData;
import com.tilioteo.hypothesis.event.ButtonEvent;
import com.tilioteo.hypothesis.event.ButtonPanelData;
import com.tilioteo.hypothesis.event.ButtonPanelEvent;
import com.tilioteo.hypothesis.event.ImageData;
import com.tilioteo.hypothesis.event.ImageEvent;
import com.tilioteo.hypothesis.event.RadioPanelData;
import com.tilioteo.hypothesis.event.RadioPanelEvent;
import com.tilioteo.hypothesis.ui.Button;
import com.tilioteo.hypothesis.ui.Image;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
public class CommandFactory {

	public static Command createActionCommand(HasActions actions,
			String actionId) {
		final AbstractBaseAction action = actions != null ? actions.getActions()
				.get(actionId) : null;

		return new Command() {
			public void execute() {
				if (action != null)
					action.execute();
			}
		};
	}

	public static Command createButtonClickEventCommand(Button component,
			SlideManager slideManager) {
		final ButtonEvent event = new ButtonEvent.Click(new ButtonData(
				component, slideManager));

		return createComponentEventCommand(event);
	}

	public static Command createButtonPanelClickEventCommand(
			ButtonPanelData data) {
		final ButtonPanelEvent event = new ButtonPanelEvent.Click(data);

		return createComponentEventCommand(event);
	}

	private static Command createComponentEventCommand(
			final AbstractComponentEvent<?> event) {
		return new Command() {
			public void execute() {
				event.getComponentData().getSlideManager().getEventManager()
						.fireEvent(event);
			}
		};
	}

	public static Command createImageClickEventCommand(ImageData data) {
		final ImageEvent event = new ImageEvent.Click(data);

		return createComponentEventCommand(event);
	}

	public static Command createImageLoadEventCommand(Image component,
			SlideManager slideManager) {
		final ImageEvent event = new ImageEvent.Load(new ImageData(
				component, slideManager));

		return createComponentEventCommand(event);
	}

	public static Command createRadioPanelClickEventCommand(RadioPanelData data) {
		final RadioPanelEvent event = new RadioPanelEvent.Click(data);

		return createComponentEventCommand(event);
	}

}
