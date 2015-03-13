package de.javax.util.eventbinding.spi.javafx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.InputMethodEvent;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.javax.util.eventbinding.DefaultEventBinderFactory;
import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.AddressEditorGui;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.ContactEditorGui;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.PersonEditorGui;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.AddressEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.ContactEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.JavaFxEventCollector;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.JavaFxEventCollector.Entry;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.PersonEditorGuiLogic;

public class JavaFxEventBindingServiceProviderTest extends AbstractJavaFXTest {

    private ContactEditorGui gui;
    private ContactEditorGuiLogic logic;
    private EventBinder eventBinder;

    public JavaFxEventBindingServiceProviderTest() {
    }

    @Before
    public void prepare() throws Exception {
        super.prepare();
        gui = new ContactEditorGui(new PersonEditorGui(), new AddressEditorGui());
        logic = new ContactEditorGuiLogic(new PersonEditorGuiLogic(), new AddressEditorGuiLogic());
        eventBinder = new DefaultEventBinderFactory().createEventBinder();
    }

    @Test
    public void bind() throws Exception {
        EventBinding eventBinding = eventBinder.bind(gui, logic);
        Set<EventTarget> boundTargets = getBoundTargets(eventBinding);
        Assert.assertEquals(6, boundTargets.size());
        // targets bound in ContactEditorGuiLogic
        EventTargetInfo.create(ActionEvent.class, ActionEvent.ANY, "[okButton]").assertEqualsOneTarget(boundTargets);
        EventTargetInfo.create(ActionEvent.class, ActionEvent.ANY, "[cancelButton]")
                .assertEqualsOneTarget(boundTargets);
        EventTargetInfo.create(ActionEvent.class, ActionEvent.ANY, "[*]").assertEqualsOneTarget(boundTargets);
        // targets bound in AddressEditorGuiLogic
        EventTargetInfo.create(InputMethodEvent.class, InputMethodEvent.ANY, "[addressEditor, zipField]")
                .assertEqualsOneTarget(boundTargets);
        EventTargetInfo.create(InputMethodEvent.class, InputMethodEvent.ANY, "[addressEditor, *]")
                .assertEqualsOneTarget(boundTargets);
        // targets bound in PersonEditorGuiLogic
        EventTargetInfo.create(ActionEvent.class, ActionEvent.ANY, "[personEditor, birthDateField]")
                .assertEqualsOneTarget(boundTargets);
        // fire events and check if correct events have been received

        // events for ContactEditorGui
        assertFiredEventReceived(gui.okButton, new ActionEvent(), ContactEditorGuiLogic.METHOD_ID_OK,
                ContactEditorGuiLogic.METHOD_ID_BUTTON_CLICK);
        assertFiredEventReceived(gui.cancelButton, new ActionEvent(), ContactEditorGuiLogic.METHOD_ID_CANCEL,
                ContactEditorGuiLogic.METHOD_ID_BUTTON_CLICK);

        // events for AddressEditorGui
        assertFiredEventReceived(gui.addressEditor.zip, new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0), AddressEditorGuiLogic.METHOD_ID_ON_ZIP_CHANGE,
                AddressEditorGuiLogic.METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE);
        assertFiredEventReceived(gui.addressEditor.cityField, new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0), AddressEditorGuiLogic.METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE);
        assertFiredEventReceived(gui.addressEditor.street, new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0), AddressEditorGuiLogic.METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE);

        // no events for text fields in PersonEditorGui
        assertFiredEventReceived(gui.personEditor.firstName, new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0));
        assertFiredEventReceived(gui.personEditor.lastName, new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0));

        // events for PersonEditorGui
        assertFiredEventReceived(gui.personEditor.birthDate, new ActionEvent(),
                PersonEditorGuiLogic.METHOD_ID_ON_BIRTH_DATE_CHANGE);

    }

    private void assertFiredEventReceived(javafx.event.EventTarget target, Event event, String... expectedEventIds) {
        Event.fireEvent(target, event);
        List<String> expectedEventIdList = new ArrayList<String>(Arrays.asList(expectedEventIds));
        List<String> actualEventIdList = new ArrayList<String>();
        while (JavaFxEventCollector.hasMoreElements()) {
            Entry entry = JavaFxEventCollector.getElement();
            actualEventIdList.add(entry.getId());
        }
        Assert.assertTrue(actualEventIdList.containsAll(expectedEventIdList));
    }

    static class EventTargetInfo {
        private Class<?> eventClass;
        private String eventSourceId;
        private EventType<?> eventType;

        public EventTargetInfo(Class<?> eventClass, EventType<?> eventType, String eventSourceId) {
            this.eventClass = eventClass;
            this.eventType = eventType;
            this.eventSourceId = eventSourceId;
        }

        static EventTargetInfo create(Class<?> eventClass, EventType<?> eventType, String eventSourceId) {
            return new EventTargetInfo(eventClass, eventType, eventSourceId);
        }

        public void assertEqualsOneTarget(Set<EventTarget> eventTargets) {
            for (EventTarget eventTarget : eventTargets) {
                Assert.assertTrue(eventTarget instanceof JfxEventTarget);
                JfxEventTarget jfxEventTarget = (JfxEventTarget) eventTarget;

                if (jfxEventTarget.getEventClass().equals(eventClass)
                        && jfxEventTarget.getEventSourceIdSelector().toString().equals(this.eventSourceId)
                        && jfxEventTarget.getEventType().equals(this.eventType)) {
                    return;
                }
            }
            Assert.fail("no event target matches");
        }
    }

}
