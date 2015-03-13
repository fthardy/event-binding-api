package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ContactEditorGui extends VBox {

    public final PersonEditorGui personEditor;

    public final AddressEditorGui addressEditor;

    public final Button okButton = new Button();

    public final Button cancelButton = new Button();

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
