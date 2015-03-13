package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.event.ActionEvent;
import de.javax.util.eventbinding.target.HandleJfxEvent;

public class PersonEditorGuiLogic {

    public static final String METHOD_ID_ON_BIRTH_DATE_CHANGE = "onBirthDateChange";

    public void onBirthDateChange(@HandleJfxEvent(from = "birthDateField") ActionEvent event) {
        JavaFxEventCollector.addEvent(METHOD_ID_ON_BIRTH_DATE_CHANGE, event);
    }
}
