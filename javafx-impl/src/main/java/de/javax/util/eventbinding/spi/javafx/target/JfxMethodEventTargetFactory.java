package de.javax.util.eventbinding.spi.javafx.target;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javafx.event.EventType;
import de.javax.util.eventbinding.EventBindingException;
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

	EventType<?> getEventTypeFromFieldName(String eventTypeFieldName, Class<?> eventClass) {
		Field field;
		try {
			field = eventClass.getField(eventTypeFieldName);
		} catch (NoSuchFieldException e) {
			throw new InvalidEventTypeFieldException(
					"Cound not find a public static field with the specified name '"
							+ eventTypeFieldName + "' in class "
							+ eventClass.getName() + " or in any of its ancestors!");
		}
		
		if (Modifier.isStatic(field.getModifiers())) {
			if (EventType.class.isAssignableFrom(field.getType())) {
				EventType<?> eventType;
				try {
					eventType = (EventType<?>) field.get(null);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new EventTypeFieldAccessException(e);
				}
				if (eventType == null) {
					throw new InvalidEventTypeFieldException(this.createInvalidFieldExceptionMessage(field, "is null"));
				}
				return eventType;
			} else {
				throw new InvalidEventTypeFieldException(
						this.createInvalidFieldExceptionMessage(field, "is not of type " + EventType.class.getName()));
			}
		} else {
			throw new InvalidEventTypeFieldException(this.createInvalidFieldExceptionMessage(field, "is not a static field"));
		}
	}
	
	private String createInvalidFieldExceptionMessage(Field field, String reason) {
		return new StringBuilder("The specified field '")
				.append(field.getName()).append("' found in class ")
				.append(field.getType().getName()).append(" ").append(reason).append("!")
				.toString();
	}
	
	public static class EventTypeFieldAccessException extends EventBindingException {
		
		private static final long serialVersionUID = -105984619129334001L;

		public EventTypeFieldAccessException(Throwable cause) {
			super("", cause);
		}
	}
	
	public static class InvalidEventTypeFieldException extends EventBindingException {
		
		private static final long serialVersionUID = -2515888454563743934L;

		public InvalidEventTypeFieldException(String message) {
			super(message);
		}
	}
}
