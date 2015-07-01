package de.javax.util.eventbinding.spi.impl.target

import spock.lang.Specification
import de.javax.util.eventbinding.spi.EventTarget
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory
import de.javax.util.eventbinding.spi.impl.target.metadata.DefaultHandlerMethodDescriptorCollector
import de.javax.util.eventbinding.spi.impl.target.metadata.DefaultTargetProviderClassAnalyzer
import de.javax.util.eventbinding.target.EventTargetProvider
import de.javax.util.eventbinding.target.HandleEvent

class DefaultEventTargetCollectorSpec extends Specification {

    MethodEventTargetFactory methodEventTargetFactoryMock = Mock()

    DefaultEventTargetCollector collector = new DefaultEventTargetCollector(
    new DefaultTargetProviderClassAnalyzer(new DefaultHandlerMethodDescriptorCollector()), methodEventTargetFactoryMock, new DefaultEventBindingConnectorFactory())

    def 'Should evaluate nested target providers'() {
        given:
        TestTargetProvider targetProvider = new TestTargetProvider()
        targetProvider.nested = new TestTargetProvider()

        EventTarget eventTarget1 = Mock()
        EventTarget eventTarget2 = Mock()
        EventTarget eventTarget3 = Mock()
        EventTarget eventTarget4 = Mock()

        when:
        Set eventTargets = collector.collectEventTargetsFrom(targetProvider)

        then:
        methodEventTargetFactoryMock.createMethodEventTarget(targetProvider, "", _, _) >>> [eventTarget1, eventTarget2]
        methodEventTargetFactoryMock.createMethodEventTarget(targetProvider.nested, "foo", _, _) >>> [eventTarget3, eventTarget4]

        expect:
        eventTargets.containsAll([
            eventTarget1,
            eventTarget2,
            eventTarget3,
            eventTarget4
        ])
    }
}

class TestTargetProvider {

    @EventTargetProvider(from='foo') TestTargetProvider nested

    void handleMethod1(@HandleEvent String event) {
    }

    void handleMethod2(@HandleEvent(from = "test") Integer event) {
    }
}

