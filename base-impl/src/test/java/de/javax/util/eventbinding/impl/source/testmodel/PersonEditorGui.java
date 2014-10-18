package de.javax.util.eventbinding.impl.source.testmodel;

import de.javax.util.eventbinding.impl.testmodel.CalendarComponent;
import de.javax.util.eventbinding.impl.testmodel.TextField;
import de.javax.util.eventbinding.source.EventSource;

public class PersonEditorGui {

    @EventSource("firstNameField")
    private TextField firstName = new TextField();
    @EventSource("lastNameField")
    private TextField lastName = new TextField();
    @EventSource("birthDateField")
    private CalendarComponent birthDate = new CalendarComponent();
}
