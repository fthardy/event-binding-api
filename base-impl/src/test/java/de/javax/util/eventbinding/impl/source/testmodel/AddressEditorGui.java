package de.javax.util.eventbinding.impl.source.testmodel;

import de.javax.util.eventbinding.impl.testmodel.TextField;
import de.javax.util.eventbinding.source.EventSource;

public class AddressEditorGui {

    @EventSource("streetField")
    private TextField street = new TextField();
    @EventSource("zipField")
    private TextField zip = new TextField();
    @EventSource
    private TextField cityField = new TextField();
}
