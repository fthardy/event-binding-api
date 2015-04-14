package de.javax.util.eventbinding.spi.javafx.target.testmodel;

import javafx.event.ActionEvent;
import de.javax.util.eventbinding.target.EventTargetProvider;
import de.javax.util.eventbinding.target.HandleJfxEvent;

public class ContactEditorGuiLogic {

    public static final String METHOD_ID_OK = "onOk";
    public static final String METHOD_ID_CANCEL = "onCancel";
    public static final String METHOD_ID_BUTTON_CLICK = "onButtonClick";

    @EventTargetProvider(from = "personEditor")
    // short form of "personEditor.*"
    private final PersonEditorGuiLogic personEditorLogic;

    @EventTargetProvider(from = "addressEditor")
    final AddressEditorGuiLogic addressEditorLogic;

    public ContactEditorGuiLogic() {
        this(new PersonEditorGuiLogic(), new AddressEditorGuiLogic());
    }

    public ContactEditorGuiLogic(PersonEditorGuiLogic personEditorLogic, AddressEditorGuiLogic addressEditorLogic) {
        this.personEditorLogic = personEditorLogic;
        this.addressEditorLogic = addressEditorLogic;
    }

    public void onOk(@HandleJfxEvent(from = "okButton") ActionEvent event) {
        JavaFxEventCollector.addEvent(METHOD_ID_OK, event);
    }

    public void onCancel(@HandleJfxEvent(from = "cancelButton") ActionEvent event) {
        JavaFxEventCollector.addEvent(METHOD_ID_CANCEL, event);
    }

    public void onButtonClick(@HandleJfxEvent(from = "*") ActionEvent event) {
        JavaFxEventCollector.addEvent(METHOD_ID_BUTTON_CLICK, event);
    }
}
