package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.scene.input.InputMethodEvent;
import de.javax.util.eventbinding.target.HandleJfxEvent;

public class AddressEditorGuiLogic {

    public static final String METHOD_ID_ON_ZIP_CHANGE = "onZipChange";
    public static final String METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE = "onAnyTextFieldChange";

    public void onZipChange(@HandleJfxEvent(from = "zipField") InputMethodEvent event) {
        JavaFxEventCollector.addEvent(METHOD_ID_ON_ZIP_CHANGE, event);
    }

    // Short form of @HandleEvent(from="*")
    public void onAnyTextFieldChange(@HandleJfxEvent(eventType = "ANY") InputMethodEvent event) {
        JavaFxEventCollector.addEvent(METHOD_ID_ON_ANY_TEXT_FIELD_CHANGE, event);
    }
}
