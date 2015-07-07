package de.javax.util.eventbinding.impl.source;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.javax.util.eventbinding.impl.source.testmodel.AddressEditorGui;
import de.javax.util.eventbinding.impl.source.testmodel.ContactEditorGui;
import de.javax.util.eventbinding.impl.source.testmodel.PersonEditorGui;
import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventBindingConnectorFactory;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceCollector;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;

public class DefaultEventSourceCollectorTest {

    private AddressEditorGui adressEditorGui;
    private PersonEditorGui personEditorGui;
    private ContactEditorGui contactEditorGui;
    private EventSourceCollector eventSourceCollector;

    @Before
    public void prepare() throws Exception {
        adressEditorGui = new AddressEditorGui();
        personEditorGui = new PersonEditorGui();
        contactEditorGui = new ContactEditorGui(personEditorGui, adressEditorGui);
        eventSourceCollector = new DefaultEventSourceCollector(new DefaultEventSourceFactory(
                new DefaultEventBindingConnectorFactory()));
    }

    @Test
    public void collectEventSourcesFrom() throws Exception {
        Set<EventSource> collectedEventSources = eventSourceCollector.collectEventSourcesFrom(contactEditorGui);
        Assert.assertEquals(9, collectedEventSources.size());
        Set<String> eventSourceIds = new HashSet<String>();
        for (EventSource eventSource : collectedEventSources) {
            eventSourceIds.add(eventSource.getId().toString());
        }
        Assert.assertTrue(eventSourceIds.contains("personEditor.firstNameField"));
        Assert.assertTrue(eventSourceIds.contains("personEditor.lastNameField"));
        Assert.assertTrue(eventSourceIds.contains("personEditor.birthDateField"));
        Assert.assertTrue(eventSourceIds.contains("addressEditor.streetField"));
        Assert.assertTrue(eventSourceIds.contains("addressEditor.zipField"));
        Assert.assertTrue(eventSourceIds.contains("addressEditor.cityField"));
        Assert.assertTrue(eventSourceIds.contains("okButton"));
        Assert.assertTrue(eventSourceIds.contains("cancelButton"));
        Assert.assertTrue(eventSourceIds.contains(EventSourceId.ROOT));
    }

}
