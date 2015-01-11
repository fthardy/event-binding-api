package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.scene.input.InputEvent;
import de.javax.util.eventbinding.target.HandleEvent;

public class AddressEditorGuiLogic {

    public void onZipChange(@HandleEvent(from = "zipField") InputEvent event) {

    }

    // Short form of @HandleEvent(from="*")
    public void onAnyTextFieldChange(@HandleEvent InputEvent event) {

    }
}
