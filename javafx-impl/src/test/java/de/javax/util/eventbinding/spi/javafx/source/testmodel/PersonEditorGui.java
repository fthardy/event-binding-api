package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class PersonEditorGui extends VBox {

    private Text firstName = new Text();
    private Text lastName = new Text();
    private DatePicker birthDate = new DatePicker();

    public PersonEditorGui() {
        firstName.setId("firstNameField");
        lastName.setId("lastNameField");
        birthDate.setId("birthDateField");
        getChildren().add(firstName);
        getChildren().add(lastName);
        getChildren().add(birthDate);
    }

}
