package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AddressEditorGui extends VBox {

    public Text street = new Text();
    public Text zip = new Text();
    public Text cityField = new Text();

    public AddressEditorGui() {
        street.setId("streetField");
        zip.setId("zipField");
        cityField.setId("cityField");
        getChildren().add(street);
        getChildren().add(zip);
        getChildren().add(cityField);
    }
}
