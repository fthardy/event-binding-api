package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.event.ActionEvent;
import de.javax.util.eventbinding.target.HandleJfxEvent;

public class PersonEditorGuiLogic {

    public void onBirthDateChange(@HandleJfxEvent(from = "birthDateField") ActionEvent event) {
        JavaFxEventCollector.addEvent(event);
    }
}
