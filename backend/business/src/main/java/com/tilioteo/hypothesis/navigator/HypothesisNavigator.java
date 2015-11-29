/**
 * 
 */
package com.tilioteo.hypothesis.navigator;

import com.tilioteo.hypothesis.business.SessionManager;
import com.tilioteo.hypothesis.data.model.User;
import com.tilioteo.hypothesis.event.interfaces.MainUIEvent.PostViewChangeEvent;
import com.tilioteo.hypothesis.eventbus.MainEventBus;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author kamil
 *
 */
@SuppressWarnings("serial")
public class HypothesisNavigator extends Navigator {

	private static final HypothesisViewType ERROR_VIEW = HypothesisViewType.PACKS;
	private ViewProvider errorViewProvider;
	private MainEventBus bus;

	public HypothesisNavigator(MainEventBus bus, final ComponentContainer container) {
		super(UI.getCurrent(), container);

		this.bus = bus;

		initViewChangeListener();
		initViewProviders();
	}

	private void initViewChangeListener() {
		addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(final ViewChangeEvent event) {
				HypothesisViewType view = HypothesisViewType.getByViewName(event.getViewName());

				User user = SessionManager.getLoggedUser();
				if (user != null) {
					return view.isAllowed(user.getRoles());
				}
				return false;
			}

			@Override
			public void afterViewChange(final ViewChangeEvent event) {
				// Appropriate events get fired after the view is changed.
				bus.post(new PostViewChangeEvent(event.getViewName()));
				// MainEventBus.get().post(new BrowserResizeEvent());
				// MainEventBus.get().post(new CloseOpenWindowsEvent());
			}
		});
	}

	private void initViewProviders() {
		// A dedicated view provider is added for each separate view type
		for (final HypothesisViewType viewType : HypothesisViewType.values()) {
			ViewProvider viewProvider = new HypothesisViewProvider(bus, viewType);

			addProvider(viewProvider);
		}

		setErrorProvider(new ViewProvider() {
			@Override
			public String getViewName(final String viewAndParameters) {
				return ERROR_VIEW.getViewName();
			}

			@Override
			public View getView(final String viewName) {
				return errorViewProvider.getView(ERROR_VIEW.getViewName());
			}
		});
	}
}