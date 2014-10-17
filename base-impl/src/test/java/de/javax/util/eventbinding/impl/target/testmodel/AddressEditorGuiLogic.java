package de.javax.util.eventbinding.impl.target.testmodel;

import de.javax.util.eventbinding.impl.testmodel.TextChangeEvent;
import de.javax.util.eventbinding.target.HandleEvent;

public class AddressEditorGuiLogic {

    public void onZipChange(@HandleEvent(from="zipField") TextChangeEvent event) {
        
    }
    
    // TODO Should only get events from corresponding event source provider!!!
    public void onAnyTextFieldChange(@HandleEvent TextChangeEvent event) {
        
    }
}
