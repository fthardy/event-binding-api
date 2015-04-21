package de.javax.util.eventbinding

import spock.lang.Specification
import de.javax.util.eventbinding.impl.DefaultEventBinder
import de.javax.util.eventbinding.spi.EventBindingFactory
import de.javax.util.eventbinding.spi.EventSource
import de.javax.util.eventbinding.spi.EventSourceCollector
import de.javax.util.eventbinding.spi.EventSourceId
import de.javax.util.eventbinding.spi.EventSourceIdSelector
import de.javax.util.eventbinding.spi.EventTarget
import de.javax.util.eventbinding.spi.EventTargetCollector

class EventBinderSpec extends Specification {

    private EventBinder eventBinder;

    private EventTargetCollector eventTargetCollectorMock = Mock()
    private EventSourceCollector eventSourceCollectorMock = Mock()
	private EventBindingFactory eventBindingFactoryMock = Mock()

    def setup() {
        this.eventBinder = new DefaultEventBinder(this.eventSourceCollectorMock, this.eventTargetCollectorMock, this.eventBindingFactoryMock)
    }

    def 'No event sources are found'() {
        given:
        def targetProvider = new Object()
        def sourceProvider = new Object()
        EventSource eventSourceMock = Mock()

        when:
        EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)

        then:'the target collector is obtained from the service'
        this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >> ([] as Set)

        then:
        thrown(EventBindingException)
    }

    def 'No event targets are found'() {
        given:
        def targetProvider = new Object()
        def sourceProvider = new Object()
        EventSource eventSourceMock = Mock()

        when:
        EventBinding eventBinding = this.eventBinder.bind(sourceProvider, targetProvider)

        then:'the target collector is obtained from the service'
        this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >> ([eventSourceMock] as Set)

        then:'the target collector is obtained from the service'
        this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >> ([] as Set)

        then:
        thrown(EventBindingException)
    }

    def 'No event targets can be bound to any sources'() {
        given:
        def targetProvider = new Object()
        def sourceProvider = new Object()
        EventSourceIdSelector eventTarget1IdSelector = new EventSourceIdSelector("foo")
        EventSourceIdSelector eventTarget2IdSelector = new EventSourceIdSelector("bar")
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
        this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)

        then:'the target collector is obtained from the service'
        this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >>
                ([eventTargetMock1, eventTargetMock2] as Set)

        then:'none of the event sources are matching with any event target'
        eventTarget1IdSelector.matches(eventSource1Id) >> false
        eventTarget1IdSelector.matches(eventSource2Id) >> false
        eventTarget2IdSelector.matches(eventSource1Id) >> false
        eventTarget2IdSelector.matches(eventSource2Id) >> false

        then:
        UnboundEventTargetsException e = thrown(UnboundEventTargetsException)

        expect:
        e.targetDescriptions.length == 2
        this.eventBinder.inStrictBindingMode == false
    }

    def 'Not all targets can be bound in strict binding mode'() {
        given:
        this.eventBinder.inStrictBindingMode = true

        def targetProvider = new Object()
        def sourceProvider = new Object()
        EventSourceIdSelector eventTarget1IdSelector = new EventSourceIdSelector("foo")
        EventSourceIdSelector eventTarget2IdSelector = new EventSourceIdSelector("bar")
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
        this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)

        then:'the target collector is obtained from the service'
        this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >>
                ([eventTargetMock1, eventTargetMock2] as Set)

        then:'for target 1 no source is matching'
        eventTarget1IdSelector.matches(eventSource1Id) >> false
        eventTarget1IdSelector.matches(eventSource2Id) >> false
        and:'for target 2 source 1 is matching'
        eventTarget2IdSelector.matches(eventSource1Id) >> true
        eventTarget2IdSelector.matches(eventSource2Id) >> false
        eventSourceMock1.bindTo(eventTargetMock2)
        eventTargetMock2.isBound() >> true

        then:
        UnboundEventTargetsException e = thrown(UnboundEventTargetsException)

        expect:
        e.targetDescriptions.length == 1
        this.eventBinder.inStrictBindingMode == true
    }

    def 'Binding succeeds in strict binding mode'() {
        given:
        this.eventBinder.inStrictBindingMode = true

        def targetProvider = new Object()
        def sourceProvider = new Object()
        EventSourceIdSelector eventTarget1IdSelector = new EventSourceIdSelector("foo")
        EventSourceIdSelector eventTarget2IdSelector = new EventSourceIdSelector("bar")
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
        this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)

        then:'the target collector is obtained from the service'
        this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >>
                ([eventTargetMock1, eventTargetMock2] as Set)

        then:'for target 1 source 1 is matching'
        eventTarget1IdSelector.matches(eventSource1Id) >> true
        eventTarget1IdSelector.matches(eventSource2Id) >> false
        eventSourceMock1.bindTo(eventTargetMock1)
        eventTargetMock1.isBound() >> true
        and:'for target 2 source 1 is matching'
        eventTarget2IdSelector.matches(eventSource1Id) >> true
        eventTarget2IdSelector.matches(eventSource2Id) >> false
        eventSourceMock1.bindTo(eventTargetMock2)
        eventTargetMock2.isBound() >> true

        then:'a binding is created'
		this.eventBindingFactoryMock.createEventBinding(
                this.eventBinder,
                sourceProvider, targetProvider, { givenSetOfBoundTargets ->
                    givenSetOfBoundTargets.size() == 2 &&
                            givenSetOfBoundTargets.containsAll([eventTargetMock1, eventTargetMock2])
                }) >> eventBindingMock

        expect:
        eventBinding == eventBindingMock
        this.eventBinder.inStrictBindingMode == true
    }

    def 'Binding succeeds in non-strict binding mode'() {
        given:
        def targetProvider = new Object()
        def sourceProvider = new Object()
        EventSourceIdSelector eventTarget1IdSelector = new EventSourceIdSelector("foo")
        EventSourceIdSelector eventTarget2IdSelector = new EventSourceIdSelector("bar")
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
        this.eventSourceCollectorMock.collectEventSourcesFrom(sourceProvider) >>
                ([eventSourceMock1, eventSourceMock2] as Set)

        then:'the target collector is obtained from the service'
        this.eventTargetCollectorMock.collectEventTargetsFrom(targetProvider) >>
                ([eventTargetMock1, eventTargetMock2] as Set)

        then:'for target 1 no sources are matching'
        eventTarget1IdSelector.matches(eventSource1Id) >> false
        eventTarget1IdSelector.matches(eventSource2Id) >> false
        and:'for target 2 source 2 is matching'
        eventTarget2IdSelector.matches(eventSource1Id) >> false
        eventTarget2IdSelector.matches(eventSource2Id) >> true
        eventSourceMock2.bindTo(eventTargetMock2)
        eventTargetMock2.isBound() >> true

        then:'a binding is created'
        this.eventBindingFactoryMock.createEventBinding(
                this.eventBinder,
                sourceProvider, targetProvider, { givenSetOfBoundTargets ->
                    givenSetOfBoundTargets.size() == 1 &&
                            givenSetOfBoundTargets.contains(eventTargetMock2)
                }) >> eventBindingMock

        expect:
        eventBinding == eventBindingMock
        this.eventBinder.inStrictBindingMode == false
    }
}
