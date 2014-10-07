package de.javax.util.eventbinding

import spock.lang.Specification
import de.javax.util.eventbinding.spi.EventBindingServiceProvider
import de.javax.util.eventbinding.spi.EventSourceProvider
import de.javax.util.eventbinding.spi.EventTarget
import de.javax.util.eventbinding.spi.EventTargetCollector

class EventBinderSpec extends Specification {
	
	private EventBinder eventBinder;
	
	private EventBindingServiceProvider serviceProviderMock = Mock()
	private EventTargetCollector targetCollectorMock = Mock()
	private EventSourceProvider eventSourceProviderMock = Mock()
	
	def setup() {
		this.eventBinder = new DefaultEventBinder(this.serviceProviderMock)
	}
	
	def 'None of the found targets could be bound to the source'() {
		given:
		def targetProvider = new Object()
		def sourceProvider = new Object()
		EventTarget eventTargetMock1 = Mock()
		EventTarget eventTargetMock2 = Mock()
		EventBinding eventBindingMock = Mock()
		 
		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
		then:'A source provider is created for the source object'
		this.serviceProviderMock.createEventSourceProvider(sourceProvider) >> this.eventSourceProviderMock
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock

		then:'Two event targets are found by the target collector'
		this.targetCollectorMock.collectEventTargetsFrom(
			targetProvider) >> ([eventTargetMock1, eventTargetMock2] as Set)
			
		then:'None event target is bound to the source'
		eventTargetMock1.bindToSourcesOf(this.eventSourceProviderMock) >> false
		eventTargetMock2.bindToSourcesOf(this.eventSourceProviderMock) >> false
				
		then:
		UnboundTargetsException e = thrown(UnboundTargetsException)
		
		expect:
		e.unboundTargetInfos.length == 2
		this.eventBinder.strictBindingMode == false
	}
	
	def 'At least one target could not be bound in strict binding mode'() {
		given:
		this.eventBinder.setStrictBindingMode(true)

		def targetProvider = new Object()
		def sourceProvider = new Object()
		EventTarget eventTargetMock1 = Mock()
		EventTarget eventTargetMock2 = Mock()
		EventBinding eventBindingMock = Mock()
		 
		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
		then:'A source provider is created for the source object'
		this.serviceProviderMock.createEventSourceProvider(sourceProvider) >> this.eventSourceProviderMock
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock

		then:'Two event targets are found by the target collector'
		this.targetCollectorMock.collectEventTargetsFrom(targetProvider) >>
			([eventTargetMock1, eventTargetMock2] as Set)
			
		then:'One event target is bound to the source and the other is not bound'
		eventTargetMock1.bindToSourcesOf(this.eventSourceProviderMock) >> true
		eventTargetMock2.bindToSourcesOf(this.eventSourceProviderMock) >> false
		
		then:
		UnboundTargetsException e = thrown(UnboundTargetsException)
		
		expect:
		e.unboundTargetInfos.length == 1
		this.eventBinder.strictBindingMode == true
	}
	
	def 'Successfully establish an event binding (strict binding mode)'() {
		given:
		this.eventBinder.setStrictBindingMode(true)
		
		def targetProvider = new Object()
		def sourceProvider = new Object()
		EventTarget eventTargetMock1 = Mock()
		EventTarget eventTargetMock2 = Mock()
		EventBinding eventBindingMock = Mock()
		 
		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
		then:'A source provider is created for the source object'
		this.serviceProviderMock.createEventSourceProvider(sourceProvider) >> this.eventSourceProviderMock
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock

		then:'Two event targets are found by the target collector'
		this.targetCollectorMock.collectEventTargetsFrom(
			targetProvider) >> ([eventTargetMock1, eventTargetMock2] as Set)
			
		then:'Both event targets are bound to the source'
		eventTargetMock1.bindToSourcesOf(this.eventSourceProviderMock) >> true
		eventTargetMock2.bindToSourcesOf(this.eventSourceProviderMock) >> true
		
		then:'An event binding container is created with the found and bound event target'
		this.serviceProviderMock.createEventBinding(sourceProvider, targetProvider,
			{ it.size() == 2 && it.contains(eventTargetMock1) && it.contains(eventTargetMock2) }) >> eventBindingMock
		
		expect:
		eventBinding == eventBindingMock
		this.eventBinder.strictBindingMode == true
	}

	def 'Successfully establish an event binding (non-strict binding mode)'() {
		given:
		def targetProvider = new Object()
		def sourceProvider = new Object()
		EventTarget eventTargetMock1 = Mock()
		EventTarget eventTargetMock2 = Mock()
		EventBinding eventBindingMock = Mock()
		 
		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)

		then:'A source provider is created for the source object'
		this.serviceProviderMock.createEventSourceProvider(sourceProvider) >> this.eventSourceProviderMock
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock

		then:'Two event targets are found by the target collector'
		this.targetCollectorMock.collectEventTargetsFrom(targetProvider) >> 
			([eventTargetMock1, eventTargetMock2] as Set)
			
		then:'One event target is bound to the source and the other is not bound'
		eventTargetMock1.bindToSourcesOf(this.eventSourceProviderMock) >> true
		eventTargetMock2.bindToSourcesOf(this.eventSourceProviderMock) >> false
		
		then:'An event binding container is created with the found and bound event target'
		this.serviceProviderMock.createEventBinding(sourceProvider, targetProvider,
			{ it.size() == 1 && it.contains(eventTargetMock1) }) >> eventBindingMock
		
		expect:
		eventBinding == eventBindingMock
		this.eventBinder.strictBindingMode == false
	}
	
	def 'No targets are found at target provider (target collector returns null)'() {
		given:
		def targetProvider = new Object()
		def sourceProvider = new Object()

		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock

		then:
		thrown(NoEventTargetsFoundException)
	}
	
	def 'No targets are found at target provider (target collector returns emtpy set)'() {
		given:
		def targetProvider = new Object()
		def sourceProvider = new Object()

		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock
		
		then:'No targets are found by the target collector'
		this.targetCollectorMock.collectEventTargetsFrom(targetProvider) >> ([] as Set)

		then:
		thrown(NoEventTargetsFoundException)
	}
	
	def 'No targets are found at target provider (target collector throws NoTargetsFoundException)'() {
		given:
		def targetProvider = new Object()
		def sourceProvider = new Object()

		when:
		EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
		then:'The target collector is obtained from the service'
			this.serviceProviderMock.getEventTargetCollector() >> this.targetCollectorMock

		then:'No targets are found by the target collector'
		this.targetCollectorMock.collectEventTargetsFrom(targetProvider) >> { throw new NoEventTargetsFoundException() }

		then:
		thrown(NoEventTargetsFoundException)
	}
}
