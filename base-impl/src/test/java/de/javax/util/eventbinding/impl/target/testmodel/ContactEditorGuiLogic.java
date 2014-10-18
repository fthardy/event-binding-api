package de.javax.util.eventbinding.impl.target.testmodel;

import de.javax.util.eventbinding.impl.testmodel.ButtonClickEvent;
import de.javax.util.eventbinding.target.EventTargetProvider;
import de.javax.util.eventbinding.target.HandleEvent;

public class ContactEditorGuiLogic {

    @EventTargetProvider(from="personEditor") // short form of "personEditor.*"
    private final PersonEditorGuiLogic personEditorLogic;
    
    @EventTargetProvider(from="addressEditor.*")
    final AddressEditorGuiLogic addressEditorLogic;
    
    public ContactEditorGuiLogic(PersonEditorGuiLogic personEditorLogic, AddressEditorGuiLogic addressEditorLogic) {
        this.personEditorLogic = personEditorLogic;
        this.addressEditorLogic = addressEditorLogic;
    }

    public void onOk(@HandleEvent(from="okButton") ButtonClickEvent event) {
        
    }
    
    public void onCancel(@HandleEvent(from="cancelButton") ButtonClickEvent event) {
        
    }
    
    public void onButtonClick(@HandleEvent(from="*") ButtonClickEvent event) {
        
    }
}
