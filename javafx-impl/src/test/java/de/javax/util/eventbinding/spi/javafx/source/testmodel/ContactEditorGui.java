package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ContactEditorGui extends VBox {

    private final PersonEditorGui personEditor;

    private final AddressEditorGui addressEditor;

    private final Button okButton = new Button();

    private final Button cancelButton = new Button();

    public ContactEditorGui(PersonEditorGui personEditor, AddressEditorGui addressEditor) {
        this.personEditor = personEditor;
        this.addressEditor = addressEditor;
        personEditor.setId("personEditor");
        addressEditor.setId("addressEditor");
        okButton.setId("okButton");
        cancelButton.setId("cancelButton");
        getChildren().add(personEditor);
        getChildren().add(addressEditor);
        getChildren().add(okButton);
        getChildren().add(cancelButton);
    }
}
