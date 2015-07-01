package de.javax.util.eventbinding.spi.javafx.source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.javax.util.eventbinding.spi.EventSource;
import de.javax.util.eventbinding.spi.EventSourceCollector;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.impl.source.DefaultEventSourceFactory;
import de.javax.util.eventbinding.spi.javafx.AbstractJavaFXTest;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.AddressEditorGui;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.ContactEditorGui;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.PersonEditorGui;

/**
 * This tests validates that the all components of {@link ContactEditorGui} are recognized as event sources including
 * the reference to ContactEditorGui itself.
 * 
 * @author Matthias Hanisch
 *
 */
public class JavaFxEventSourceCollectorTest extends AbstractJavaFXTest {

    private EventSourceCollector eventSourceCollector;

    @Before
    @Override
    public void prepare() throws Exception {
        super.prepare();
        this.eventSourceCollector = new JavaFxEventSourceCollector(new DefaultEventSourceFactory());
    }

    @Test
    public void collectEventSources() {
        // collected event sources must contain ROOT event source
        List<String> expectedEventsourceIds = new ArrayList<String>(Arrays.asList(EventSourceId.ROOT,
                // collected event sources must contain addressEditor and its components
                "addressEditor", "addressEditor.streetField", "addressEditor.zipField", "addressEditor.cityField",
                // collected event sources must contain personEditor and its components
                "personEditor", "personEditor.firstNameField", "personEditor.lastNameField",
                "personEditor.birthDateField",
                // collected event sources must contain buttons of contactEditor
                "okButton", "cancelButton"));
        ContactEditorGui gui = new ContactEditorGui(new PersonEditorGui(), new AddressEditorGui());
        Set<EventSource> eventSources = this.eventSourceCollector.collectEventSourcesFrom(gui);
        Assert.assertEquals(eventSources.toString(), expectedEventsourceIds.size(), eventSources.size());
        Set<String> eventSourceIds = new HashSet<String>();
        for (EventSource eventSource : eventSources) {
            eventSourceIds.add(eventSource.getId().toString());
        }
        expectedEventsourceIds.removeAll(eventSourceIds);
        Assert.assertEquals(0, expectedEventsourceIds.size());
    }
}
