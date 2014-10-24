package de.javax.util.eventbinding.impl.target

import spock.lang.Specification
import de.javax.util.eventbinding.impl.target.testmodel.AddressEditorGuiLogic
import de.javax.util.eventbinding.impl.target.testmodel.ContactEditorGuiLogic
import de.javax.util.eventbinding.impl.target.testmodel.PersonEditorGuiLogic
import de.javax.util.eventbinding.spi.EventSourceIdSelector
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory
import de.javax.util.eventbinding.spi.EventTarget
import de.javax.util.eventbinding.spi.EventTargetCollector
import de.javax.util.eventbinding.spi.impl.DefaultEventSourceIdSelector
import de.javax.util.eventbinding.spi.impl.DefaultEventSourceIdSelectorFactory
import de.javax.util.eventbinding.spi.impl.target.CascadedEventSourceIdSelector
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector
import de.javax.util.eventbinding.spi.impl.target.DefaultMethodEventTargetFactory;
import de.javax.util.eventbinding.spi.impl.target.MethodEventTargetFactory

class DefaultEventTargetCollectorSpec extends Specification {
    
    MethodEventTargetFactory targetFactoryMock = Mock()
    EventSourceIdSelectorFactory selectorFactoryMock = Mock()
    
    EventTargetCollector collector = new DefaultEventTargetCollector(this.targetFactoryMock, this.selectorFactoryMock)
    
    def 'A target factory must be set on creation'() {
        when:
            new DefaultEventTargetCollector(null, this.selectorFactoryMock)
            
        then:
            thrown(NullPointerException)
    }
    
    def 'A selector factory must be set on creation'() {
        when:
            new DefaultEventTargetCollector(this.targetFactoryMock, null)
    
        then:
            thrown(NullPointerException)
    }
    
    def 'Collect event targets from provider without nested providers'() {
        given:
            def targetProvider = new AddressEditorGuiLogic()
            EventSourceIdSelector selectorMock1 = Mock()
            EventSourceIdSelector selectorMock2 = Mock()
            EventTarget eventTargetMock1 = Mock()
            EventTarget eventTargetMock2 = Mock()
        
        when:
            def eventTargets = collector.collectEventTargetsFrom(targetProvider)
        
        then:
            this.selectorFactoryMock.createEventSourceIdSelector("zipField") >> selectorMock1
            this.selectorFactoryMock.createEventSourceIdSelector("*") >> selectorMock2
            this.targetFactoryMock.createEventTarget(targetProvider, _, selectorMock1) >> eventTargetMock1
            this.targetFactoryMock.createEventTarget(targetProvider, _, selectorMock2) >> eventTargetMock2
        
        expect:
            2 == eventTargets.size()
            eventTargets.containsAll([eventTargetMock1, eventTargetMock2])
    }
    
    def 'Collect event targets from provider with nested providers'() {
        given:
            collector = new DefaultEventTargetCollector(
                new DefaultMethodEventTargetFactory(), new DefaultEventSourceIdSelectorFactory())
            def targetProvider = new ContactEditorGuiLogic(new PersonEditorGuiLogic(), new AddressEditorGuiLogic())

        when:
            def eventTargets = collector.collectEventTargetsFrom(targetProvider)

        then:
            6 == eventTargets.size()
    }
}
  