package de.javax.util.eventbinding.spi.javafx;

import java.util.Set;

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
    }

}
