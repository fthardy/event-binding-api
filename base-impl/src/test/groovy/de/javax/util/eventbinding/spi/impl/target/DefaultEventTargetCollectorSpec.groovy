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
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTargetCollector.CascadedEventSourceIdSelectorFactory

class DefaultEventTargetCollectorSpec extends Specification {

	MethodEventTargetFactory targetFactoryMock = Mock()
	EventSourceIdSelectorFactory selectorFactoryMock = Mock()
	CascadedEventSourceIdSelectorFactory cascadedIdSelectorFactoryMock = Mock()
	ClassInfoCache<TargetProviderClassInfo> cacheMock = Mock();

	EventTargetCollector collector = new DefaultEventTargetCollector(
	this.targetFactoryMock, this.selectorFactoryMock, this.cascadedIdSelectorFactoryMock, this.cacheMock)

	def 'A target factory must be set on creation'() {
		when:
		new DefaultEventTargetCollector(null, this.selectorFactoryMock, this.cacheMock)

		then:
		thrown(NullPointerException)
	}

	def 'A selector factory must be set on creation'() {
		when:
		new DefaultEventTargetCollector(this.targetFactoryMock, null, this.cacheMock)

		then:
		thrown(NullPointerException)
	}

	def 'A cache must be set on creation'() {
		when:
		new DefaultEventTargetCollector(this.targetFactoryMock, this.selectorFactoryMock, null)

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
		EventSourceIdSelector selectorMock1 = Mock()
		EventSourceIdSelector selectorMock2 = Mock()
		EventSourceIdSelector selectorMock3 = Mock()
		EventSourceIdSelector selectorMock4 = Mock()
		EventSourceIdSelector selectorMock5 = Mock()
		EventSourceIdSelector selectorMock6 = Mock()
		EventSourceIdSelector selectorMock7 = Mock()
		EventSourceIdSelector selectorMock8 = Mock()
		EventSourceIdSelector selectorMock9 = Mock()
		EventSourceIdSelector selectorMock10 = Mock()
		EventSourceIdSelector selectorMock11 = Mock()
		EventTarget eventTargetMock1 = Mock()
		EventTarget eventTargetMock2 = Mock()
		EventTarget eventTargetMock3 = Mock()
		EventTarget eventTargetMock4 = Mock()
		EventTarget eventTargetMock5 = Mock()
		EventTarget eventTargetMock6 = Mock()

		when:
		def eventTargets = this.collector.collectEventTargetsFrom(contactEditorLogic)

		then:
		this.selectorFactoryMock.createEventSourceIdSelector("okButton") >> selectorMock1
		this.selectorFactoryMock.createEventSourceIdSelector("cancelButton") >> selectorMock2
		2 * this.selectorFactoryMock.createEventSourceIdSelector("*") >>> [selectorMock3, selectorMock7]
		this.selectorFactoryMock.createEventSourceIdSelector("personEditor.*") >> selectorMock4
		this.selectorFactoryMock.createEventSourceIdSelector("addressEditor.*") >> selectorMock5
		this.selectorFactoryMock.createEventSourceIdSelector("zipField") >> selectorMock6
		this.selectorFactoryMock.createEventSourceIdSelector("birthDateField") >> selectorMock8
		0 * this.selectorFactoryMock._

		this.cascadedIdSelectorFactoryMock.createCascadedIdSelector(selectorMock4, selectorMock8) >> selectorMock9
		this.cascadedIdSelectorFactoryMock.createCascadedIdSelector(selectorMock5, selectorMock6) >> selectorMock10
		this.cascadedIdSelectorFactoryMock.createCascadedIdSelector(selectorMock5, selectorMock7) >> selectorMock11
		0 * this.cascadedIdSelectorFactoryMock._

		this.targetFactoryMock.createEventTarget(contactEditorLogic, _, selectorMock1) >> eventTargetMock1
		this.targetFactoryMock.createEventTarget(contactEditorLogic, _, selectorMock2) >> eventTargetMock2
		this.targetFactoryMock.createEventTarget(contactEditorLogic, _, selectorMock3) >> eventTargetMock3
		this.targetFactoryMock.createEventTarget(personEditorLogic, _, selectorMock9) >> eventTargetMock4
		this.targetFactoryMock.createEventTarget(addressEditorLogic, _, selectorMock10) >> eventTargetMock5
		this.targetFactoryMock.createEventTarget(addressEditorLogic, _, selectorMock11) >> eventTargetMock6
		0 * this.targetFactoryMock._

		expect:
		6 == eventTargets.size()
		eventTargets.containsAll([eventTargetMock1, eventTargetMock2, eventTargetMock3, eventTargetMock4, eventTargetMock5, eventTargetMock6])
	}
}
