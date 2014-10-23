package de.javax.util.eventbinding

import org.spockframework.compiler.model.ThenBlock;

import spock.lang.Specification
import de.javax.util.eventbinding.spi.EventBindingServiceProvider
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector
import de.javax.util.eventbinding.spi.EventSourceId
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget
import de.javax.util.eventbinding.spi.EventTargetCollector

class EventBinderSpec extends Specification {
	
	private EventBinder eventBinder;
	
	private EventBindingServiceProvider serviceProviderMock = Mock()
	private EventTargetCollector eventTargetCollectorMock = Mock()
	private EventSourceCollector eventSourceCollectorMock = Mock()
	
	def setup() {
		this.eventBinder = new DefaultEventBinder(this.serviceProviderMock)
	}
    
    def 'No service provider is defined on construction'() {
        when:
        new DefaultEventBinder(null)
        
        then:
        thrown(NullPointerException)
    }
    
    def 'No event sources are found'() {
        given:
            def targetProvider = new Object()
            def sourceProvider = new Object()
            EventSource eventSourceMock = Mock()
            
        when:
            EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
        
        then:'the target collector is obtained from the service'
            this.serviceProviderMock.getEventSourceCollector() >> this.eventSourceCollectorMock
        and:
            this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >> ([] as Set)
    
        then:
            thrown(NoEventSourcesFoundException)
    }
    
    def 'No event targets are found'() {
        given:
            def targetProvider = new Object()
            def sourceProvider = new Object()
            EventSource eventSourceMock = Mock()
            
        when:
            EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
        
        then:'the target collector is obtained from the service'
            this.serviceProviderMock.getEventSourceCollector() >> this.eventSourceCollectorMock
        and:
            this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >> ([eventSourceMock] as Set)
            
        then:'the target collector is obtained from the service'
            this.serviceProviderMock.getEventTargetCollector() >> this.eventTargetCollectorMock
        and:
            this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >> ([] as Set)

        then:
            thrown(NoEventTargetsFoundException)
    }
	
	def 'No event targets can be bound to any sources'() {
		given:
    		def targetProvider = new Object()
    		def sourceProvider = new Object()
            EventSourceIdSelector eventTarget1IdSelector = Mock()
            EventSourceIdSelector eventTarget2IdSelector = Mock()
    		EventTarget eventTargetMock1 = Mock()
    		EventTarget eventTargetMock2 = Mock()
    		EventSource eventSourceMock1 = Mock()
    		EventSource eventSourceMock2 = Mock()
    		EventBinding eventBindingMock = Mock()
            EventSourceId eventSource1Id = new EventSourceId(['addressEditor', 'okButton'])
            EventSourceId eventSource2Id = new EventSourceId(['addressEditor', 'cancelButton'])
            eventSourceMock1.getId() >> eventSource1Id
            eventSourceMock2.getId() >> eventSource2Id
            eventTargetMock1.getEventSourceIdSelector() >> eventTarget1IdSelector
            eventTargetMock2.getEventSourceIdSelector() >> eventTarget2IdSelector
            
		when:
		    EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
		
        then:'the source collector is obtained from the service'
            this.serviceProviderMock.getEventSourceCollector() >> this.eventSourceCollectorMock
        and:'two sources are found at the given event source provider object'
            this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)
        
		then:'the target collector is obtained from the service'
		    this.serviceProviderMock.getEventTargetCollector() >> this.eventTargetCollectorMock
	    and:'two event targets are found for the given event target provider object'
	        this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >> 
                ([eventTargetMock1, eventTargetMock2] as Set)
		                
		then:'none of the event sources are matching with an event target'
            eventTarget1IdSelector.matches(eventSource1Id) >> false
            eventTarget1IdSelector.matches(eventSource2Id) >> false
            eventTarget2IdSelector.matches(eventSource1Id) >> false
            eventTarget2IdSelector.matches(eventSource2Id) >> false
				
		then:
		    UnboundTargetsException e = thrown(UnboundTargetsException)
		
		expect:
    		e.unboundTargetInfos.length == 2
    		this.eventBinder.strictBindingMode == false
	}
	
	def 'Not all targets can be bound in strict binding mode'() {
		given:
		    this.eventBinder.setStrictBindingMode(true)

            def targetProvider = new Object()
            def sourceProvider = new Object()
            EventSourceIdSelector eventTarget1IdSelector = Mock()
            EventSourceIdSelector eventTarget2IdSelector = Mock()
            EventTarget eventTargetMock1 = Mock()
            EventTarget eventTargetMock2 = Mock()
            EventSource eventSourceMock1 = Mock()
            EventSource eventSourceMock2 = Mock()
            EventBinding eventBindingMock = Mock()
            EventSourceId eventSource1Id = new EventSourceId(['addressEditor', 'okButton'])
            EventSourceId eventSource2Id = new EventSourceId(['addressEditor', 'cancelButton'])
            eventSourceMock1.getId() >> eventSource1Id
            eventSourceMock2.getId() >> eventSource2Id
            eventTargetMock1.getEventSourceIdSelector() >> eventTarget1IdSelector
            eventTargetMock2.getEventSourceIdSelector() >> eventTarget2IdSelector
		 
        when:
            EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
        
        then:'the source collector is obtained from the service'
            this.serviceProviderMock.getEventSourceCollector() >> this.eventSourceCollectorMock
        and:'two sources are found at the given event source provider object'
            this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)
        
        then:'the target collector is obtained from the service'
            this.serviceProviderMock.getEventTargetCollector() >> this.eventTargetCollectorMock
        and:'two event targets are found for the given event target provider object'
            this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >> 
                ([eventTargetMock1, eventTargetMock2] as Set)
                        
        then:'one of the event sources is matching with an event target'
            eventTarget1IdSelector.matches(eventSource1Id) >> false
            eventTarget1IdSelector.matches(eventSource2Id) >> false
            eventTarget2IdSelector.matches(eventSource1Id) >> true
            eventTarget2IdSelector.matches(eventSource2Id) >> false
        and:'the binding between the event source and event target is successful'
            eventSourceMock1.bindTo(eventTargetMock2) >> true
		
		then:
		    UnboundTargetsException e = thrown(UnboundTargetsException)
		
		expect:
    		e.unboundTargetInfos.length == 1
    		this.eventBinder.strictBindingMode == true
	}
	
	def 'Binding succeeds in strict binding mode'() {
        given:
            this.eventBinder.setStrictBindingMode(true)

            def targetProvider = new Object()
            def sourceProvider = new Object()
            EventSourceIdSelector eventTarget1IdSelector = Mock()
            EventSourceIdSelector eventTarget2IdSelector = Mock()
            EventTarget eventTargetMock1 = Mock()
            EventTarget eventTargetMock2 = Mock()
            EventSource eventSourceMock1 = Mock()
            EventSource eventSourceMock2 = Mock()
            EventBinding eventBindingMock = Mock()
            EventSourceId eventSource1Id = new EventSourceId(['addressEditor', 'okButton'])
            EventSourceId eventSource2Id = new EventSourceId(['addressEditor', 'cancelButton'])
            eventSourceMock1.getId() >> eventSource1Id
            eventSourceMock2.getId() >> eventSource2Id
            eventTargetMock1.getEventSourceIdSelector() >> eventTarget1IdSelector
            eventTargetMock2.getEventSourceIdSelector() >> eventTarget2IdSelector
         
        when:
            EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
        
        then:'the source collector is obtained from the service'
            this.serviceProviderMock.getEventSourceCollector() >> this.eventSourceCollectorMock
        and:'two sources are found at the given event source provider object'
            this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)
        
        then:'the target collector is obtained from the service'
            this.serviceProviderMock.getEventTargetCollector() >> this.eventTargetCollectorMock
        and:'two event targets are found for the given event target provider object'
            this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >> 
                ([eventTargetMock1, eventTargetMock2] as Set)
                        
        then:'all of the event sources are matching with all event target'
            eventTarget1IdSelector.matches(eventSource1Id) >> true
            eventTarget1IdSelector.matches(eventSource2Id) >> false
            eventTarget2IdSelector.matches(eventSource1Id) >> true
            eventTarget2IdSelector.matches(eventSource2Id) >> false
        and:'the binding between the event sources and event targets is successful'
            eventSourceMock1.bindTo(eventTargetMock2) >> true
            eventSourceMock1.bindTo(eventTargetMock1) >> true
            
        then:'a binding is created'
            this.serviceProviderMock.createEventBinding(
                sourceProvider, targetProvider, { givenSetOfBoundTargets ->
                    givenSetOfBoundTargets.size() == 2 && 
                    givenSetOfBoundTargets.containsAll([eventTargetMock1, eventTargetMock2])
                }) >> eventBindingMock
		
		expect:
		eventBinding == eventBindingMock
		this.eventBinder.strictBindingMode == true
	}

	def 'Binding succeeds in non-strict binding mode'() {
		given:
            def targetProvider = new Object()
            def sourceProvider = new Object()
            EventSourceIdSelector eventTarget1IdSelector = Mock()
            EventSourceIdSelector eventTarget2IdSelector = Mock()
            EventTarget eventTargetMock1 = Mock()
            EventTarget eventTargetMock2 = Mock()
            EventSource eventSourceMock1 = Mock()
            EventSource eventSourceMock2 = Mock()
            EventBinding eventBindingMock = Mock()
            EventSourceId eventSource1Id = new EventSourceId(['addressEditor', 'okButton'])
            EventSourceId eventSource2Id = new EventSourceId(['addressEditor', 'cancelButton'])
            eventSourceMock1.getId() >> eventSource1Id
            eventSourceMock2.getId() >> eventSource2Id
            eventTargetMock1.getEventSourceIdSelector() >> eventTarget1IdSelector
            eventTargetMock2.getEventSourceIdSelector() >> eventTarget2IdSelector
         
        when:
            EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)
        
        then:'the source collector is obtained from the service'
            this.serviceProviderMock.getEventSourceCollector() >> this.eventSourceCollectorMock
        and:'two sources are found at the given event source provider object'
            this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)
        
        then:'the target collector is obtained from the service'
            this.serviceProviderMock.getEventTargetCollector() >> this.eventTargetCollectorMock
        and:'two event targets are found for the given event target provider object'
            this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >> 
                ([eventTargetMock1, eventTargetMock2] as Set)
                        
        then:'all of the event sources are matching with all event target'
            eventTarget1IdSelector.matches(eventSource1Id) >> true
            eventTarget1IdSelector.matches(eventSource2Id) >> true
            eventTarget2IdSelector.matches(eventSource1Id) >> true
            eventTarget2IdSelector.matches(eventSource2Id) >> false
        and:'the binding between the event sources and event targets is successful'
            eventSourceMock1.bindTo(eventTargetMock1) >> true
            eventSourceMock2.bindTo(eventTargetMock1) >> true
            eventSourceMock1.bindTo(eventTargetMock2) >> false
            
        then:'a binding is created'
            this.serviceProviderMock.createEventBinding(
                sourceProvider, targetProvider, { givenSetOfBoundTargets ->
                    givenSetOfBoundTargets.size() == 1 && 
                    givenSetOfBoundTargets.contains(eventTargetMock1)
                }) >> eventBindingMock
        
        expect:
            eventBinding == eventBindingMock
            this.eventBinder.strictBindingMode == false
	}
}
