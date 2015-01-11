package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.event.ActionEvent;
import de.javax.util.eventbinding.target.HandleEvent;

public class PersonEditorGuiLogic {

    public void onBirthDateChange(@HandleEvent(from = "birthDateField") ActionEvent event) {

    }
}
