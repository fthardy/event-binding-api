package de.javax.util.eventbinding.spi.javafx.target;

import static org.junit.Assert.assertSame;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

import org.junit.Test;

import de.javax.util.eventbinding.spi.javafx.target.JfxMethodEventTargetFactory.InvalidEventTypeFieldException;

public class JfxMethodEventTargetFactoryTest {

	private JfxMethodEventTargetFactory factory = new JfxMethodEventTargetFactory();
	
	@Test public void shouldFindFieldInEventClass() {
		EventType<?> eventType = this.factory.getEventTypeFromFieldName("MOUSE_CLICKED", MouseEvent.class);
		assertSame(eventType, MouseEvent.MOUSE_CLICKED);
	}
	
	@Test public void shouldFindFieldInSuperClassOfEventClass() {
		EventType<?> eventType = this.factory.getEventTypeFromFieldName("ANY", TestEvent.class);
		assertSame(eventType, Event.ANY);
	}
	
	@Test(expected = InvalidEventTypeFieldException.class)
	public void shouldThrowExceptionWhenFieldDoesNotExist() {
		this.factory.getEventTypeFromFieldName("FOO", TestEvent.class);
	}
	
	@Test(expected = InvalidEventTypeFieldException.class)
	public void shouldThrowExceptionWhenFieldIsNotPublic() {
		this.factory.getEventTypeFromFieldName("NOT_PUBLIC", TestEvent.class);
	}
	
	@Test(expected = InvalidEventTypeFieldException.class)
	public void shouldThrowExceptionOnNonStaticField() {
		this.factory.getEventTypeFromFieldName("NON_STATIC", TestEvent.class);
	}
	
	@Test(expected = InvalidEventTypeFieldException.class)
	public void shouldThrowExceptionWhenFieldIsNotOfTypeEventType() {
		this.factory.getEventTypeFromFieldName("WRONG_TYPE", TestEvent.class);
	}
	
	@Test(expected = InvalidEventTypeFieldException.class)
	public void shouldThrowExceptionWhenFieldIsNull() {
		this.factory.getEventTypeFromFieldName("NULL", TestEvent.class);
	}
	
	public static class TestEvent extends Event {
		
		private static final long serialVersionUID = 1L;
		
		public static final EventType<TestEvent> NULL = null;
		public static final String WRONG_TYPE = "";
		public final EventType<TestEvent> NON_STATIC = new EventType<JfxMethodEventTargetFactoryTest.TestEvent>("NON_STATIC");
		static final EventType<TestEvent> NOT_PUBLIC = new EventType<JfxMethodEventTargetFactoryTest.TestEvent>("NOT_PUBLIC");
		
		public TestEvent() {
			super(EventType.ROOT);
		}
	}
}
