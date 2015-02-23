package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.scene.input.InputEvent;
import de.javax.util.eventbinding.target.HandleJfxEvent;

public class AddressEditorGuiLogic {

    public void onZipChange(@HandleJfxEvent(from = "zipField") InputEvent event) {
        JavaFxEventCollector.addEvent(event);
    }

    // Short form of @HandleEvent(from="*")
    public void onAnyTextFieldChange(@HandleJfxEvent(eventType = "ANY") InputEvent event) {
        JavaFxEventCollector.addEvent(event);
    }
}
