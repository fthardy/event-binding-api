package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import org.junit.Before;
import org.junit.Test;

import de.javax.util.eventbinding.DefaultEventBinderFactory;
import de.javax.util.eventbinding.EventBinderFactory;
import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.AddressEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.ContactEditorGuiLogic;
import de.javax.util.eventbinding.spi.javafx.target.testmodel.PersonEditorGuiLogic;

public class JavaFxEventBindingConnectorFactoryTest {

    private EventBinderFactory factory;
    private ContactEditorGui contactGui;
    private ContactEditorGuiLogic contactLogic;
    private PersonEditorGui personEditorGui;
    private PersonEditorGuiLogic personEditorLogic;
    private AddressEditorGui addressEditorGui;
    private AddressEditorGuiLogic addressEditorLogic;

    @Before
    public void prepare() throws Exception {
        setupJavaFx();
        this.factory = new DefaultEventBinderFactory();
        this.personEditorGui = new PersonEditorGui();
        this.personEditorLogic = new PersonEditorGuiLogic();
        this.addressEditorGui = new AddressEditorGui();
        this.addressEditorLogic = new AddressEditorGuiLogic();
        this.contactGui = new ContactEditorGui(personEditorGui, addressEditorGui);
        this.contactLogic = new ContactEditorGuiLogic(personEditorLogic, addressEditorLogic);
    }

    private void setupJavaFx() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel(); // Initializes the JavaFx Platform
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        new MyApp().start(new Stage()); // Create and
                                                        // initialize
                                                        // your app.

                    }
                });
            }
        });
        thread.start();// Initialize the thread
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void button() throws Exception {
        EventBinding binding = factory.createEventBinder().bind(contactGui, contactLogic);
    }
}
