package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddressEditorGui extends VBox {

    private Text street = new Text();
    private Text zip = new Text();
    private Text cityField = new Text();

    public AddressEditorGui() {
        street.setId("streetField");
        zip.setId("zipField");
        cityField.setId("cityField");
        getChildren().add(street);
        getChildren().add(zip);
        getChildren().add(cityField);
    }
}
