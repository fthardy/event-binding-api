package de.javax.util.eventbinding.spi.javafx.target;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javafx.event.EventType;
import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.target.MethodEventTargetFactory;

/**
 * Creates instances of {@link JfxEventTargetImpl}.
 *
 * @author Frank Hardy
 */
public class JfxMethodEventTargetFactory implements MethodEventTargetFactory {

	@Override
	public EventTarget createEventTarget(
			EventSourceIdSelector sourceIdSelector, Class<?> eventClass, Object eventTypeFieldName, EventDispatcher dispatcher) {
		return new JfxEventTargetImpl(
				sourceIdSelector, eventClass, this.getEventTypeFromFieldName(eventTypeFieldName.toString(), eventClass), dispatcher);
	}

	private EventType<?> getEventTypeFromFieldName(String eventTypeFieldName, Class<?> eventClass) {
		EventType<?> eventType = null;
		try {
			Field field = eventClass.getField(eventTypeFieldName);
			if (EventType.class.isAssignableFrom(field.getType())) {
				if (Modifier.isStatic(field.getModifiers())) {
					eventType = (EventType<?>) field.get(null);
				} else {
					// TODO the field is an instance member field (we do not handle this)!!!
				}
			} else {
				// TODO the field is not of type EventType!!!
			}
		} catch (Exception e) {
			// TODO Field does not exist or is not accessible
			e.printStackTrace();
		}
		return eventType;
	}
}
