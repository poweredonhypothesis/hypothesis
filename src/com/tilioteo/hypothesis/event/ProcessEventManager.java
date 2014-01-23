/**
 * 
 */
package com.tilioteo.hypothesis.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;

import javax.swing.event.EventListenerList;

import com.vaadin.ui.UI;

/**
 * @author Kamil Morong - Hypothesis
 * 
 */
public class ProcessEventManager {

	private static HashMap<UI, ProcessEventManager> applicationEventManagers = new HashMap<UI, ProcessEventManager>();

	public static final ProcessEventManager get(UI ui) {
		ProcessEventManager eventManager = applicationEventManagers
				.get(ui);
		if (eventManager == null) {
			eventManager = new ProcessEventManager();
			registerApplicationEventManager(ui, eventManager);
		}
		return eventManager;
	}

	public static final void registerApplicationEventManager(
			UI ui, ProcessEventManager eventManager) {
		applicationEventManagers.put(ui, eventManager);
	}

	public static final void unregisterApplicationEventManager(
			UI ui) {
		applicationEventManagers.remove(ui);
	}

	private HashMap<Class<? extends ProcessEvent>, EventListenerList> listenersMap = new HashMap<Class<? extends ProcessEvent>, EventListenerList>();

	private ProcessEventManager() {

	}

	public final void addListener(
			Class<? extends ProcessEvent> eventClass,
			ProcessEventListener eventListener) {
		if (!isListening(eventListener, eventClass)) {
			EventListenerList listeners = listenersMap.get(eventClass);
			if (listeners == null) {
				listeners = new EventListenerList();
				listenersMap.put(eventClass, listeners);
			}
			listeners.add(ProcessEventListener.class, eventListener);
		}
	}

	@SafeVarargs
	public final void addListener(ProcessEventListener eventListener,
			Class<? extends ProcessEvent>... eventClasses) {
		for (Class<? extends ProcessEvent> eventClass : eventClasses) {
			addListener(eventClass, eventListener);
		}
	}

	@SuppressWarnings("unchecked")
	private EventListenerList findListeners(
			Class<? extends ProcessEvent/*Object*/> eventClass) {
		EventListenerList listeners = listenersMap.get(eventClass);
		if (listeners == null
				&& !eventClass.getSuperclass().equals(EventObject.class))
			return findListeners((Class<? extends ProcessEvent/*Object*/>) eventClass
					.getSuperclass());

		return listeners;
	}

	public final void fireEvent(ProcessEvent event) {
		if (event != null) {
			EventListenerList listenerList = findListeners(event.getClass());

			if (listenerList != null) {
				Object[] listeners = listenerList.getListenerList();

				for (int i = 0; i < listeners.length; i += 2) {
					if (listeners[i] == ProcessEventListener.class) {
						((ProcessEventListener) listeners[i + 1])
								.handleEvent(event);
					}
				}
			}
		}
	}

	public final boolean isListening(ProcessEventListener eventListener,
			Class<? extends ProcessEvent> eventClass) {
		EventListenerList listenerList = findListeners(eventClass);
		if (listenerList != null) {
			Object[] listeners = listenerList.getListenerList();

			for (int i = 0; i < listeners.length; i += 2) {
				if (listeners[i] == ProcessEventListener.class
						&& listeners[i + 1] == eventListener) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * public final void removeAllListeners(Class<? extends
	 * ProcessEvent> eventClass) { EventListenerList listeners =
	 * listenersMap.get(eventClass); if (listeners != null) {
	 * listeners.remove(ProcessEventListener.class, eventListener); }
	 * listenersMap.remove(eventClass); }
	 * 
	 * public final void removeAllListeners() { listenersMap.clear(); }
	 */

	public final void removeListener(
			Class<? extends ProcessEvent> eventClass,
			ProcessEventListener eventListener) {
		EventListenerList listeners = listenersMap.get(eventClass);
		if (listeners != null) {
			listeners.remove(ProcessEventListener.class, eventListener);
		}
	}

	public final void removeListener(ProcessEventListener eventListener) {
		Collection<EventListenerList> listenersCollection = listenersMap
				.values();
		for (EventListenerList listeners : listenersCollection) {
			if (listeners != null) {
				listeners.remove(ProcessEventListener.class, eventListener);
			}
		}
	}
}