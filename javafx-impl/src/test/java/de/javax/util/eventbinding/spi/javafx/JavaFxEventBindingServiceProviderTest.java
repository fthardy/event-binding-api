package de.javax.util.eventbinding.spi.javafx;

import java.util.Set;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import de.javax.util.eventbinding.DefaultEventBinderFactory;
import de.javax.util.eventbinding.EventBinder;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.EventSourceId;
import de.javax.util.eventbinding.spi.EventTarget;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.AddressEditorGui;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.ContactEditorGui;
import de.javax.util.eventbinding.spi.javafx.source.testmodel.PersonEditorGui;
import de.javax.util.eventbinding.spi.javafx.target.JfxEventTarget;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.AddressEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.ContactEditorGuiLogic;
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
        EventTargetInfo.create(ActionEvent.class, "okButton", ActionEvent.ANY).assertEqualsOneTarget(boundTargets);
        EventTargetInfo.create(ActionEvent.class, "cancelButton", ActionEvent.ANY).assertEqualsOneTarget(boundTargets);
        Assert.assertEquals(6, boundTargets.size());
    }

    static class EventTargetInfo {
        private Class<?> eventClass;
        private String eventSourceSelector;
        private EventType<?> eventType;

        public EventTargetInfo(Class<?> eventClass, String eventSourceSelector, EventType<?> eventType) {
            this.eventClass = eventClass;
            this.eventSourceSelector = eventSourceSelector;
            this.eventType = eventType;
        }

        static EventTargetInfo create(Class<?> eventClass, String eventSourceSelector, EventType<?> eventType) {
            return new EventTargetInfo(eventClass, eventSourceSelector, eventType);
        }

        public void assertEqualsOneTarget(Set<EventTarget> eventTargets) {
            for (EventTarget eventTarget : eventTargets) {
                Assert.assertTrue(eventTarget instanceof JfxEventTarget);
                JfxEventTarget javaFxEventTarget = (JfxEventTarget) eventTarget;
                if (eventTarget.getEventClass().equals(eventClass)
                        && eventTarget.getEventSourceIdSelector().matches(new EventSourceId(eventSourceSelector))
                        && javaFxEventTarget.getEventType().equals(eventType)) {
                    return;
                }
            }
            Assert.fail("no event target matches");
        }
    }

}
