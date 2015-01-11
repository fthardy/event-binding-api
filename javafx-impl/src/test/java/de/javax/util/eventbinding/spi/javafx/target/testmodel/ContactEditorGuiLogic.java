package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.event.ActionEvent;
import de.javax.util.eventbinding.target.EventTargetProvider;
import de.javax.util.eventbinding.target.HandleEvent;

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

    public void onOk(@HandleEvent(from = "okButton") ActionEvent event) {

    }

    public void onCancel(@HandleEvent(from = "cancelButton") ActionEvent event) {

    }

    public void onButtonClick(@HandleEvent(from = "*") ActionEvent event) {

    }
}
