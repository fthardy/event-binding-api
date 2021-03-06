package de.javax.util.eventbinding.spi.javafx;

import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;

import org.junit.Before;

import de.javax.util.eventbinding.EventBinding;
import de.javax.util.eventbinding.impl.DefaultEventBinding;
import de.javax.util.eventbinding.spi.EventTarget;

/**
 * Base class for testing with javafx classes. Setup-method provides initialization of javafx runtime.
 * 
 * @author Matthias Hanisch
 *
 */
public class AbstractJavaFXTest {

    @Before
    public void prepare() throws Exception {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                new JFXPanel();
                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {
                        new JavaFXTestApplication().start(new Stage());
                    }
                });
            }
        });
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    class JavaFXTestApplication extends Application {

        @Override
        public void start(Stage arg0) {
        }

    }

    protected final Set<EventTarget> getBoundTargets(EventBinding eventBinding) {
    	return ((DefaultEventBinding) eventBinding).getBoundTargets();
    }
}
