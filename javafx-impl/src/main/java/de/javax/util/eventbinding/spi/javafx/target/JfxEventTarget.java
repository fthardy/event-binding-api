package de.javax.util.eventbinding.spi.javafx.target;

import javafx.event.EventType;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * A JavaFX specific extension of the event target interface.<br/>
 * Provides the event type as additional meta data.
 *
 * @author Frank Hardy
 */
public interface JfxEventTarget extends EventTarget {

	/**
	 * @return the event type of the events which the receiving event target can handle.
	 */
	EventType<?> getEventType();
}
