package de.javax.util.eventbinding.spi.impl.target;

import static org.junit.Assert.*;

import java.lang.reflect.Method;

import org.junit.Assert;
import org.junit.Test;

import de.javax.util.eventbinding.spi.impl.target.MethodAdaptingEventDispatcher.EventDispatchingFailedException;

class MethodAdaptingEventDispatcherTest {
	
	@Test(expected = NullPointerException) void methodShouldNeverBeNull() {
		new MethodAdaptingEventDispatcher(null, null)
	}
	
	@Test(expected = NullPointerException) void instanceShouldNotBeNullWhenInstanceMethodIsGiven() {
		Method method = this.getInstanceMethod()
		
		new MethodAdaptingEventDispatcher(method, null)
	}
	
	@Test(expected = IllegalArgumentException) void instanceMethodShouldBeMethodOfGivenInstance() {
		Method method = this.getInstanceMethod()
		
		new MethodAdaptingEventDispatcher(method, "")
	}
	
	@Test(expected = EventDispatchingFailedException) void shouldEncapsulateThrownException() {
		Method method = this.getExceptionThrowingMethod()

		MethodAdaptingEventDispatcher dispatcher = new MethodAdaptingEventDispatcher(method, this)
		dispatcher.dispatchEvent(null)
	}

	@Test void shouldDispatchToPublicInstanceMethod() {
		Method method = this.getInstanceMethod()
		
		MethodAdaptingEventDispatcher dispatcher = new MethodAdaptingEventDispatcher(method, this)
		
		Event event = new Event()
		dispatcher.dispatchEvent(event)
		
		assert event.handled
	}
	
	@Test void shouldDispatchToPublicStaticMethod() {
		Method method = this.getStaticMethod()

		MethodAdaptingEventDispatcher dispatcher = new MethodAdaptingEventDispatcher(method, null)
		
		Event event = new Event()
		dispatcher.dispatchEvent(event)
		
		assert event.handled
	}

	@Test void shouldRenderHumanReadableStringForInstanceMethod() {
		Method method = this.getInstanceMethod()
		
		String text = new MethodAdaptingEventDispatcher(method, this).toString()
		assert text
		assert text ==~ /^.+\s@\s${this.class.name}\s\(.+\)$/
	}

	@Test void shouldRenderHumanReadableStringForStaticMethod() {
		Method method = this.getStaticMethod()

		String text = new MethodAdaptingEventDispatcher(method, null).toString()
		assert text
		assert text ==~ /^.+\s@\s${this.class.name}$/
	}
	
	private Method getInstanceMethod() {
		this.class.getMethod('eventHandler', Event.class)
	}
	
	private Method getStaticMethod() {
		this.class.getMethod('staticEventHandler', Event.class)
	}
	
	private Method getExceptionThrowingMethod() {
		this.class.getMethod('exceptionThrowingEventHandler', Event.class)
	}

	void eventHandler(Event event) {
		assert event
		event.handled = true
	}
	
	void exceptionThrowingEventHandler(Event event) {
		throw new NullPointerException('Test!')
	}
	
	static staticEventHandler(Event event) {
		assert event
		event.handled = true
	}
	
	class Event {
		boolean handled
	}
}
