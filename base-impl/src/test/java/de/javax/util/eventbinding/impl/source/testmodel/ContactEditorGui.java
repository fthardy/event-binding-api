package de.javax.util.eventbinding.impl.source.testmodel;

import de.javax.util.eventbinding.impl.testmodel.Button;
import de.javax.util.eventbinding.source.EventSource;
import de.javax.util.eventbinding.source.EventSourceProvider;

public class ContactEditorGui {

    @EventSourceProvider("personEditor")
    private final PersonEditorGui personEditor;

    @EventSourceProvider
    final AddressEditorGui addressEditor;

    @EventSource("okButton")
    private final Button okButton = new Button();

    @EventSource("cancelButton")
    private final Button cancelButton = new Button();

    public ContactEditorGui(PersonEditorGui personEditor, AddressEditorGui addressEditor) {
        this.personEditor = personEditor;
        this.addressEditor = addressEditor;
    }
}
