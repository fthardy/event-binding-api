package de.javax.util.eventbinding.impl.target.testmodel;

import de.javax.util.eventbinding.impl.testmodel.CalendarChangeEvent;
import de.javax.util.eventbinding.target.HandleEvent;

public class PersonEditorGuiLogic {

    public void onBirthDateChange(@HandleEvent(from="birthDateField") CalendarChangeEvent event) {
        
    }
}
