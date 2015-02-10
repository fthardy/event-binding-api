package de.javax.util.eventbinding.spi.impl.target

import spock.lang.Specification
import de.javax.util.eventbinding.impl.target.testmodel.AddressEditorGuiLogic
import de.javax.util.eventbinding.impl.target.testmodel.ContactEditorGuiLogic
import de.javax.util.eventbinding.impl.target.testmodel.PersonEditorGuiLogic
import de.javax.util.eventbinding.spi.EventSourceIdSelector
import de.javax.util.eventbinding.spi.EventSourceIdSelectorFactory
import de.javax.util.eventbinding.spi.EventTarget
import de.javax.util.eventbinding.spi.EventTargetCollector
import de.javax.util.eventbinding.spi.impl.ClassInfoCache
import de.javax.util.eventbinding.spi.impl.SimpleClassInfoCache;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector.CascadedEventSourceIdSelectorFactory

class DefaultEventTargetCollectorSpec extends Specification {

    MethodEventTargetFactory targetFactoryMock = Mock()
    EventSourceIdSelectorFactory selectorFactoryMock = Mock()
    CascadedEventSourceIdSelectorFactory cascadedIdSelectorFactoryMock = Mock()
    ClassInfoCache<TargetProviderClassInfo> cacheMock = Mock();
	HandlerMethodInfoCollector handlerMethodInfoCollectorMock = Mock();

    EventTargetCollector collector = new DefaultEventTargetCollector(
		this.targetFactoryMock,
		this.selectorFactoryMock,
		this.cascadedIdSelectorFactoryMock,
		new DefaultHandlerMethodInfoCollector(this.selectorFactoryMock),
		new SimpleClassInfoCache<TargetProviderClassInfo>())

    def 'A target factory must be set on creation'() {
        when:
        	new DefaultEventTargetCollector(null, this.selectorFactoryMock, this.handlerMethodInfoCollectorMock, this.cacheMock)

        then:
        	thrown(NullPointerException)
    }

    def 'A selector factory must be set on creation'() {
        when:
        	new DefaultEventTargetCollector(this.targetFactoryMock, null, this.handlerMethodInfoCollectorMock, this.cacheMock)

        then:
			thrown(NullPointerException)
    }
    
    def 'A handler method info collector must be set on creation'() {
    	when:
    		new DefaultEventTargetCollector(this.targetFactoryMock, this.selectorFactoryMock, null, this.cacheMock)
    
	    then:
	    	thrown(NullPointerException)
    }

    def 'A cache must be set on creation'() {
        when:
        	new DefaultEventTargetCollector(this.targetFactoryMock, this.selectorFactoryMock, this.handlerMethodInfoCollectorMock, null)

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
        def eventTargets = this.collector.collectEventTargetsFrom(targetProvider)

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
        def addressEditorLogic = new AddressEditorGuiLogic()
        def personEditorLogic = new PersonEditorGuiLogic()
        def contactEditorLogic = new ContactEditorGuiLogic(personEditorLogic, addressEditorLogic);
        EventSourceIdSelector onOkSelectorMock = Mock()
        EventSourceIdSelector onCancelSelectorMock = Mock()
        EventSourceIdSelector onZipChangeSelectorMock = Mock()
        EventSourceIdSelector onBirthDateChangeSelectorMock = Mock()
        EventSourceIdSelector personEditorSelectorMock = Mock()
        EventSourceIdSelector addressEditorSelectorMock = Mock()
        EventSourceIdSelector wildcardSelectorMock1 = Mock()
        EventSourceIdSelector wildcardSelectorMock2 = Mock()
        EventSourceIdSelector wildcardSelectorMock3 = Mock()
        EventSourceIdSelector cascadedSelectorMock1 = Mock()
        EventSourceIdSelector cascadedSelectorMock2 = Mock()
        EventSourceIdSelector cascadedSelectorMock3 = Mock()
        EventTarget onOkEventTargetMock = Mock()
        EventTarget onCancelEventTargetMock = Mock()
        EventTarget onBirthDateChangeEventTargetMock = Mock()
        EventTarget eventTargetMock1 = Mock()
        EventTarget eventTargetMock2 = Mock()
        EventTarget eventTargetMock3 = Mock()
        EventTarget eventTargetMock4 = Mock()

        when:
        def eventTargets = this.collector.collectEventTargetsFrom(contactEditorLogic)

        then:
        this.selectorFactoryMock.createEventSourceIdSelector("okButton") >> onOkSelectorMock
        this.selectorFactoryMock.createEventSourceIdSelector("cancelButton") >> onCancelSelectorMock
        this.selectorFactoryMock.createEventSourceIdSelector("zipField") >> onZipChangeSelectorMock
        this.selectorFactoryMock.createEventSourceIdSelector("birthDateField") >> onBirthDateChangeSelectorMock
        this.selectorFactoryMock.createEventSourceIdSelector("personEditor.*") >> personEditorSelectorMock
        this.selectorFactoryMock.createEventSourceIdSelector("addressEditor.*") >> addressEditorSelectorMock
        3 * this.selectorFactoryMock.createEventSourceIdSelector("*") >>> [wildcardSelectorMock1, wildcardSelectorMock2, wildcardSelectorMock3]
        0 * this.selectorFactoryMock._

        this.cascadedIdSelectorFactoryMock.createCascadedIdSelector(personEditorSelectorMock, onBirthDateChangeSelectorMock) >> cascadedSelectorMock1
        2 * this.cascadedIdSelectorFactoryMock.createCascadedIdSelector(addressEditorSelectorMock, _) >>> [cascadedSelectorMock2, cascadedSelectorMock3]
        0 * this.cascadedIdSelectorFactoryMock._

        this.targetFactoryMock.createEventTarget(contactEditorLogic, _, onOkSelectorMock) >> onOkEventTargetMock
        this.targetFactoryMock.createEventTarget(contactEditorLogic, _, onCancelSelectorMock) >> onCancelEventTargetMock
        this.targetFactoryMock.createEventTarget(personEditorLogic, _, cascadedSelectorMock1) >> onBirthDateChangeEventTargetMock
        this.targetFactoryMock.createEventTarget(addressEditorLogic, _, cascadedSelectorMock2) >> eventTargetMock1
        this.targetFactoryMock.createEventTarget(addressEditorLogic, _, cascadedSelectorMock3) >> eventTargetMock2
        2 * this.targetFactoryMock.createEventTarget(contactEditorLogic, _, _) >>> [eventTargetMock3, eventTargetMock4]
        0 * this.targetFactoryMock._

        expect:
        7 == eventTargets.size()
        eventTargets.containsAll([
            onOkEventTargetMock,
            onCancelEventTargetMock,
            onBirthDateChangeEventTargetMock,
            eventTargetMock1,
            eventTargetMock2,
            eventTargetMock3,
			eventTargetMock4
        ])
    }
}
