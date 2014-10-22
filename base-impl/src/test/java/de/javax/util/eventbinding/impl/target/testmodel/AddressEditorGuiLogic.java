package de.javax.util.eventbinding.impl.target.testmodel;

import de.javax.util.eventbinding.impl.testmodel.TextChangeEvent;
import de.javax.util.eventbinding.target.HandleEvent;

public class AddressEditorGuiLogic {

    public void onZipChange(@HandleEvent(from="zipField") TextChangeEvent event) {
        
    }
    
    // Short form of @HandleEvent(from="*")
    public void onAnyTextFieldChange(@HandleEvent TextChangeEvent event) {
        
    }
}
