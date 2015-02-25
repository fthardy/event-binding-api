package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.event.ActionEvent;
import de.javax.util.eventbinding.target.EventTargetProvider;
import de.javax.util.eventbinding.target.HandleJfxEvent;

public class ContactEditorGuiLogic {

    @EventTargetProvider(from = "personEditor")
    // short form of "personEditor.*"
    private final PersonEditorGuiLogic personEditorLogic;

    @EventTargetProvider(from = "addressEditor.*")
    final AddressEditorGuiLogic addressEditorLogic;

    public ContactEditorGuiLogic(PersonEditorGuiLogic personEditorLogic, AddressEditorGuiLogic addressEditorLogic) {
        this.personEditorLogic = personEditorLogic;
        this.addressEditorLogic = addressEditorLogic;
    }

    public void onOk(@HandleJfxEvent(from = "okButton") ActionEvent event) {
        JavaFxEventCollector.addEvent(event);
    }

    public void onCancel(@HandleJfxEvent(from = "cancelButton") ActionEvent event) {
        JavaFxEventCollector.addEvent(event);
    }

    public void onButtonClick(@HandleJfxEvent(from = "*") ActionEvent event) {
        JavaFxEventCollector.addEvent(event);
    }
}
