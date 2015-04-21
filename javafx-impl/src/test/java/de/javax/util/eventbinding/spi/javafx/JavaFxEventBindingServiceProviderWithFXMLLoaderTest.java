package de.javax.util.eventbinding.spi.javafx;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.InputMethodEvent;

import org.junit.Assert;
import org.junit.Test;

import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinderInstanceFactory;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.AddressEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.ContactEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.JavaFxEventCollector;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.JavaFxEventCollector.Entry;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.PersonEditorGuiLogic;

public class JavaFxEventBindingServiceProviderWithFXMLLoaderTest extends AbstractJavaFXTest {

    private EventBinder eventBinder;
    private Object logic;
    private Parent gui;

    public JavaFxEventBindingServiceProviderWithFXMLLoaderTest() {
    }

    @Test
    public void guiAsRoot() throws Exception {
        internalTest("/gui_as_root.fxml");
    }

    @Test
    public void guiNotRoot() throws Exception {
        internalTest("/gui_not_root.fxml");
    }

    @Test
    public void guiWithController() throws Exception {
        URL url = getClass().getResource("/gui_with_controller.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(url);
        gui = loader.load();
        Object logic = loader.getController();
        internalTest(gui, logic);

    }

    private void internalTest(String path) throws Exception {
        URL url = getClass().getResource(path);
        gui = FXMLLoader.load(url);
        logic = new ContactEditorGuiLogic(new PersonEditorGuiLogic(), new AddressEditorGuiLogic());
        internalTest(gui, logic);
    }

    @SuppressWarnings("unchecked")
    private void internalTest(Parent gui, Object logic) throws Exception {
        eventBinder = EventBinderInstanceFactory.newEventBinderInstance();
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
        assertFiredEventReceived(findEventTarget(gui, "okButton"), new ActionEvent(),
                ContactEditorGuiLogic.METHOD_ID_OK, ContactEditorGuiLogic.METHOD_ID_BUTTON_CLICK);
        assertFiredEventReceived(findEventTarget(gui, "cancelButton"), new ActionEvent(),
                ContactEditorGuiLogic.METHOD_ID_CANCEL, ContactEditorGuiLogic.METHOD_ID_BUTTON_CLICK);

        // events for AddressEditorGui
        assertFiredEventReceived(findEventTarget(gui, "zipField"), new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0), AddressEditorGuiLogic.METHOD_ID_ON_ZIP_CHANGE,
                AddressEditorGuiLogic.METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE);
        assertFiredEventReceived(findEventTarget(gui, "cityField"), new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0), AddressEditorGuiLogic.METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE);
        assertFiredEventReceived(findEventTarget(gui, "streetField"), new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0), AddressEditorGuiLogic.METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE);

        // no events for text fields in PersonEditorGui
        assertFiredEventReceived(findEventTarget(gui, "firstNameField"), new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0));
        assertFiredEventReceived(findEventTarget(gui, "lastNameField"), new InputMethodEvent(InputMethodEvent.ANY,
                Collections.EMPTY_LIST, "", 0));

        // events for PersonEditorGui
        assertFiredEventReceived(findEventTarget(gui, "birthDateField"), new ActionEvent(),
                PersonEditorGuiLogic.METHOD_ID_ON_BIRTH_DATE_CHANGE);

    }

    private javafx.event.EventTarget findEventTarget(Parent parent, String id) {
        if (id.equals(parent.getId())) {
            return parent;
        }
        for (Node child : parent.getChildrenUnmodifiable()) {
            if (id.equals(child.getId())) {
                return child;
            }
            if (child instanceof Parent) {
                javafx.event.EventTarget found = findEventTarget((Parent) child, id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
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
