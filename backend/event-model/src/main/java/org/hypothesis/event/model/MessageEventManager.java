/**
 * Apache Licence Version 2.0
 * Please read the LICENCE file
 */
package org.hypothesis.event.model;

import org.hypothesis.interfaces.MessageEventListener;

import javax.swing.event.EventListenerList;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kamil Morong, Tilioteo Ltd
 * 
 *         Hypothesis
 *
 */
@SuppressWarnings("serial")
public class MessageEventManager implements Serializable {

	private final Map<String, EventListenerList> listenersMap = new HashMap<>();
	private boolean enabled = false;

	public final void addListener(String uid, MessageEventListener eventListener) {
		if (!isListening(eventListener, uid)) {
			EventListenerList listeners = listenersMap.get(uid);
			if (listeners == null) {
				listeners = new EventListenerList();
				listenersMap.put(uid, listeners);
			}
			listeners.add(MessageEventListener.class, eventListener);
		}
	}

	public final void fireEvent(MessageEvent event) {
		if (enabled && event != null) {
			String uid = event.getMessage().getUid();
			EventListenerList listenerList = listenersMap.get(uid);

			if (listenerList != null) {
				Object[] listeners = listenerList.getListenerList();

				for (int i = 0; i < listeners.length; i += 2) {
					if (listeners[i] == MessageEventListener.class) {
						((MessageEventListener) listeners[i + 1]).handleEvent(event);
					}
				}
			}
		}
	}

	public final boolean isListening(MessageEventListener eventListener, String uid) {
		EventListenerList listenerList = listenersMap.get(uid);
		if (listenerList != null) {
			Object[] listeners = listenerList.getListenerList();

			for (int i = 0; i < listeners.length; i += 2) {
				if (listeners[i] == MessageEventListener.class && listeners[i + 1] == eventListener) {
					return true;
				}
			}
		}
		return false;
	}

	public final void removeAllListeners() {
		listenersMap.clear();
	}

	public final void removeAllListeners(String uid) {
		EventListenerList listenerList = listenersMap.get(uid);
		if (listenerList != null) {
			Object[] listeners = listenerList.getListenerList();

			for (int i = 0; i < listeners.length; i += 2) {
				if (listeners[i] == MessageEventListener.class) {
					listenerList.remove(MessageEventListener.class, (MessageEventListener) listeners[i]);
				}
			}
		}
		listenersMap.remove(uid);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
