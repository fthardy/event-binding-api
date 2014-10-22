package de.javax.util.eventbinding.impl.target

import spock.lang.Specification
import de.javax.util.eventbinding.impl.target.testmodel.AddressEditorGuiLogic
import de.javax.util.eventbinding.impl.target.testmodel.ContactEditorGuiLogic
import de.javax.util.eventbinding.impl.target.testmodel.PersonEditorGuiLogic
import de.javax.util.eventbinding.spi.EventTargetCollector
import de.javax.util.eventbinding.spi.impl.DefaultEventSourceIdSelectorFactory
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector
import de.javax.util.eventbinding.spi.impl.target.DefaultMethodEventTargetFactory

class DefaultEventTargetCollectorSpec extends Specification {

    EventTargetCollector collector = new DefaultEventTargetCollector(
            new DefaultMethodEventTargetFactory(), new DefaultEventSourceIdSelectorFactory() )
    
    def 'collect targets from provider without nested providers'() {
        given:
        def targetProvider = new AddressEditorGuiLogic()
        
        when:
        def eventTargets = collector.collectEventTargetsFrom(targetProvider)
        
        then:
        2 == eventTargets.size()
    }
    
    def 'collect targets from provider with nested providers'() {
        given:
        def targetProvider = new ContactEditorGuiLogic(new PersonEditorGuiLogic(), new AddressEditorGuiLogic())
        
        when:
        def eventTargets = collector.collectEventTargetsFrom(targetProvider)
        
        then:
        6 == eventTargets.size()
    }
}
