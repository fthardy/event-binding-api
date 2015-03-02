package de.javax.util.eventbinding.spi.javafx.target;

import javafx.event.EventType;
import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;
import de.javax.util.eventbinding.spi.javafx.JfxEventTarget;

/**
 * The implementation for JavaFX event targets.
 *
 * @author Frank Hardy
 */
public class JfxEventTargetImpl extends DefaultEventTarget implements JfxEventTarget {

	private final EventType<?> eventType;

	public JfxEventTargetImpl(EventSourceIdSelector sourceIdSelector, Class<?> eventClass, EventType<?> eventType, EventDispatcher dispatcher) {
		super(sourceIdSelector, eventClass, dispatcher);
		
		if (eventType == null) {
			throw new NullPointerException("Undefined event type!");
		}
		this.eventType = eventType;
	}

	@Override
	public EventType<?> getEventType() {
		return this.eventType;
	}
}
