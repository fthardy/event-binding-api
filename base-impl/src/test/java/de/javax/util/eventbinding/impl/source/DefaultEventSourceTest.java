package de.javax.util.eventbinding.impl.source;

import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.javax.util.eventbinding.impl.source.testmodel.AddressEditorGui;
import de.javax.util.eventbinding.impl.source.testmodel.ContactEditorGui;
import de.javax.util.eventbinding.impl.source.testmodel.PersonEditorGui;
import de.javax.util.eventbinding.impl.testmodel.ButtonClickEvent;
import de.javax.util.eventbinding.impl.testmodel.CalendarChangeEvent;
import de.javax.util.eventbinding.impl.testmodel.ParentComponentEvent;
import de.javax.util.eventbinding.impl.testmodel.TextChangeEvent;
import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;

public class DefaultEventSourceTest {

    private AddressEditorGui adressEditorGui;
    private PersonEditorGui personEditorGui;
    private ContactEditorGui contactEditorGui;
    private EventSourceCollector eventSourceCollector;
    private EventDispatcher dispatcher;
    private Set<EventSource> collectedEventSources;

    @Before
    public void prepare() throws Exception {
        adressEditorGui = new AddressEditorGui();
        personEditorGui = new PersonEditorGui();
        contactEditorGui = new ContactEditorGui(personEditorGui, adressEditorGui);
        eventSourceCollector = new DefaultEventSourceCollector(new DefaultEventSourceFactory(
                new DefaultEventBindingConnectorFactory()));
        dispatcher = Mockito.mock(EventDispatcher.class);
        collectedEventSources = eventSourceCollector.collectEventSourcesFrom(contactEditorGui);
    }

    @Test
    public void bindToTarget() throws Exception {
        checkBindToTarget("personEditor.firstNameField", TextChangeEvent.class, true);
        checkBindToTarget("personEditor.lastNameField", TextChangeEvent.class, true);
        checkBindToTarget("personEditor.birthDateField", CalendarChangeEvent.class, true);

        checkBindToTarget("personEditor.birthDateField", TextChangeEvent.class, false);

        checkBindToTarget("addressEditor.streetField", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.zipField", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.cityField", TextChangeEvent.class, true);

        checkBindToTarget("okButton", ButtonClickEvent.class, true);
        checkBindToTarget("cancelButton", ButtonClickEvent.class, true);
        checkBindToTarget("/", ParentComponentEvent.class, true);
    }

    @Test
    public void bindToTargetWildcard() throws Exception {
        checkBindToTarget("personEditor.firstNameField", "personEditor.*", TextChangeEvent.class, true);
        checkBindToTarget("personEditor.lastNameField", "personEditor.*", TextChangeEvent.class, true);
        checkBindToTarget("personEditor.birthDateField", "personEditor.*", CalendarChangeEvent.class, true);
        checkBindToTarget("personEditor.firstNameField", "*", TextChangeEvent.class, true);
        checkBindToTarget("personEditor.lastNameField", "*", TextChangeEvent.class, true);
        checkBindToTarget("personEditor.birthDateField", "*", CalendarChangeEvent.class, true);

        checkBindToTarget("personEditor.birthDateField", "personEditor.*", TextChangeEvent.class, false);
        checkBindToTarget("personEditor.birthDateField", "*", TextChangeEvent.class, false);

        checkBindToTarget("addressEditor.streetField", "addressEditor.*", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.zipField", "addressEditor.*", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.cityField", "addressEditor.*", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.streetField", "*", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.zipField", "*", TextChangeEvent.class, true);
        checkBindToTarget("addressEditor.cityField", "*", TextChangeEvent.class, true);

        checkBindToTarget("okButton", "*", ButtonClickEvent.class, true);
        checkBindToTarget("cancelButton", "*", ButtonClickEvent.class, true);
        checkBindToTarget("okButton", "personEditor.*", ButtonClickEvent.class, false);
        checkBindToTarget("cancelButton", "personEditor.*", ButtonClickEvent.class, false);
    }

    private void checkBindToTarget(String eventSourceIdSelectorExpression, Class<?> eventType, boolean expectedToBind)
            throws Exception {
        checkBindToTarget(eventSourceIdSelectorExpression, eventSourceIdSelectorExpression, eventType, expectedToBind);
    }

    private void checkBindToTarget(String eventSourceIdSelectorExpression, String eventTargetIdExpression,
            Class<?> eventType, boolean expectedToBind) throws Exception {
        EventSourceIdSelector eventSourceIdSelector = new EventSourceIdSelector(eventSourceIdSelectorExpression);
        EventSourceIdSelector eventTargetIdSelector = new EventSourceIdSelector(eventTargetIdExpression);
        EventSource eventSource = getEventSource(collectedEventSources, eventSourceIdSelector);
        Assert.assertNotNull(eventSource);
        EventTarget eventTarget = new DefaultEventTarget(eventTargetIdSelector, eventType, dispatcher);
        eventSource.bindTo(eventTarget);
        Assert.assertEquals(expectedToBind, eventTarget.isBound());
        if (eventTarget.isBound()) {
            eventTarget.unbindFromSources();
            Assert.assertFalse(eventTarget.isBound());
        }
    }

    private EventSource getEventSource(Set<EventSource> collectedEventSources,
            EventSourceIdSelector eventSourceIdSelector) {
        for (EventSource eventSource : collectedEventSources) {
            if (eventSourceIdSelector.matches(eventSource.getId())) {
                return eventSource;
            }
        }
        return null;
    }

}
