package de.javax.util.eventbinding.impl.source;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import de.javax.util.eventbinding.impl.source.testmodel.AddressEditorGui;
import de.javax.util.eventbinding.impl.source.testmodel.ContactEditorGui;
import de.javax.util.eventbinding.impl.source.testmodel.PersonEditorGui;
import de.javax.util.eventbinding.impl.testmodel.ButtonClickEvent;
import de.javax.util.eventbinding.impl.testmodel.CalendarChangeEvent;
import de.javax.util.eventbinding.impl.testmodel.TextChangeEvent;
import de.javax.util.eventbinding.spi.EventDispatcher;
import de.javax.util.eventbinding.spi.EventSourceIdSelector;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.impl.DefaultEventSourceIdSelector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceCollector;
import de.javax.util.eventbinding.spi.impl.target.DefaultEventTarget;

public class DefaultEventSourceProviderTest {

    private AddressEditorGui adressEditorGui;
    private PersonEditorGui personEditorGui;
    private ContactEditorGui contactEditorGui;
    private EventSourceCollector eventSourceCollector;

    private EventDispatcher eventDispatcher;

    @Before
    public void prepare() throws Exception {
        adressEditorGui = new AddressEditorGui();
        personEditorGui = new PersonEditorGui();
        contactEditorGui = new ContactEditorGui(personEditorGui, adressEditorGui);
        eventSourceCollector = new DefaultEventSourceCollector();
        eventDispatcher = Mockito.mock(EventDispatcher.class);
    }

    @Test
    public void findEventSourceById() throws Exception {
        checkBoundSources("personEditor.firstNameField", TextChangeEvent.class, 1);
        checkBoundSources("personEditor.lastNameField", TextChangeEvent.class, 1);
        checkBoundSources("personEditor.birthDateField", CalendarChangeEvent.class, 1);

        checkBoundSources("personEditor.birthDateField", TextChangeEvent.class, 0);

        checkBoundSources("addressEditor.streetField", TextChangeEvent.class, 1);
        checkBoundSources("addressEditor.zipField", TextChangeEvent.class, 1);
        checkBoundSources("addressEditor.cityField", TextChangeEvent.class, 1);

        checkBoundSources("okButton", ButtonClickEvent.class, 1);
        checkBoundSources("cancelButton", ButtonClickEvent.class, 1);
    }

    @Test
    public void findEventSourceByWildcard() throws Exception {
        checkBoundSources("personEditor.*", TextChangeEvent.class, 2);
        checkBoundSources("personEditor.*", CalendarChangeEvent.class, 1);
        checkBoundSources("addressEditor.*", TextChangeEvent.class, 3);
        checkBoundSources("*", TextChangeEvent.class, 5);
        checkBoundSources("*", CalendarChangeEvent.class, 1);
        checkBoundSources("*", ButtonClickEvent.class, 2);
    }

    private void checkBoundSources(
            String eventSourceIdSelectorAsString, Class<?> eventType, int expectedNumberOfBoundSources) {
        
        EventSourceIdSelector eventSourceIdSelector = new DefaultEventSourceIdSelector(eventSourceIdSelectorAsString);
        EventTarget eventTarget = new DefaultEventTarget(eventSourceIdSelector, eventType, eventDispatcher);
        if (expectedNumberOfBoundSources > 0) {
            eventSourceCollector.bindTargetToSources(eventTarget, contactEditorGui);
            Assert.assertFalse(eventTarget.getBoundSources().isEmpty());
            Assert.assertEquals(expectedNumberOfBoundSources, eventTarget.getBoundSources().size());
        } else {
            eventSourceCollector.bindTargetToSources(eventTarget, contactEditorGui);
            Assert.assertTrue(eventTarget.getBoundSources().isEmpty());
        }
        if (!eventTarget.getBoundSources().isEmpty()) {
            eventTarget.unbindFromSources();
        }
        Assert.assertEquals(0, eventTarget.getBoundSources().size());
    }
}
