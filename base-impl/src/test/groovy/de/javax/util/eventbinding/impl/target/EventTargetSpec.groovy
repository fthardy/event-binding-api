package de.javax.util.eventbinding.impl.target

import org.junit.After;
import org.mockito.Mockito;

import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceProvider;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;
import spock.lang.Specification

class EventTargetSpec extends Specification {

	private EventTarget eventTargetForSingleSource
	private EventTarget eventTargetForMultiSource
	
	private EventSourceProvider eventSourceProviderMock = Mock()
	private EventDispatcher eventDispatcherMock = Mock()
	
	def setup() {
		this.eventTargetForSingleSource = new DefaultEventTarget("testSourceId", String.class, this.eventDispatcherMock)
		this.eventTargetForMultiSource = new DefaultEventTarget(null, String.class, this.eventDispatcherMock)
	}
	
	def 'target is bound to at least one source'() {
		given:
		EventSource eventSourceMock = Mock()
		
		when:
		boolean bound = this.eventTargetForSingleSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSource("testSourceId", String.class) >> eventSourceMock
		
		then:
		eventSourceMock.register(this.eventDispatcherMock) >> { }
		
		expect:
		bound == true
 	}
	
	def 'target cannot bound to source with given identifier'() {
		when:
		boolean bound = this.eventTargetForSingleSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSourcesByType("testSourceId", String.class) >> null
		
		expect:
		bound == false
	}
	
	def 'it is not allowed to call bind on an already bound target'() {
		given:
		EventSource eventSourceMock = Mock()
		
		when:
		boolean bound = this.eventTargetForMultiSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSourcesByType(String.class) >> ([eventSourceMock] as Set)
		
		when:
		this.eventTargetForMultiSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		thrown(IllegalStateException)
	}
	
	def 'it is allowed to call bind again when the call before returned false'() {
		when:
		boolean bound = this.eventTargetForSingleSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSourcesByType("testSourceId", String.class) >> null
		
		expect:
		bound == false
		
		when:
		bound = this.eventTargetForSingleSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSourcesByType("testSourceId", String.class) >> null

		expect:
		bound == false
	}

	def 'it is not allowed to call release on an already released target'() {
		given:
		EventSource eventSourceMock = Mock()
		
		when:
		boolean bound = this.eventTargetForMultiSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSourcesByType(String.class) >> ([eventSourceMock] as Set<EventSource>)
		eventSourceMock.register(this.eventDispatcherMock) >> { }
		
		expect:
		bound == true
		
		when:
		this.eventTargetForMultiSource.release()
		
		then:
		eventSourceMock.unregisterEventDispatcher() >> { }
	}
	
	def 'it is not allowed to call release on an unbound target'() {
		when:
		boolean bound = this.eventTargetForSingleSource.bindToSourcesOf(this.eventSourceProviderMock)
		
		then:
		this.eventSourceProviderMock.findEventSourcesByType("testSourceId", String.class) >> null
		
		expect:
		bound == false
		
		when:
		bound = this.eventTargetForSingleSource.release()
		
		then:
		thrown(IllegalStateException)
	}
}
