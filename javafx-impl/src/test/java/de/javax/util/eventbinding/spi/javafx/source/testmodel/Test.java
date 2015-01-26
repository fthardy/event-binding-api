package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;
import de.javax.util.eventbinding.spi.impl.reflect.Filter;
import de.javax.util.eventbinding.spi.impl.reflect.MethodPredicate;

public class Test {

    public static void main(String[] args) {
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
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Node node = new Label();
        Filter<Method> filter = new Filter(Arrays.asList(node.getClass().getMethods()));
        filter = filter.filter(new MethodPredicate.MethodNamePredicate("getOn.*"));
        Collection<Method> methods = filter.getElements();
        for (Method m : methods) {
            Type returnType = m.getGenericReturnType();
            if (returnType == null) {
                continue;
            }
            if (returnType instanceof ParameterizedTypeImpl) {

                WildcardTypeImpl clazz = (WildcardTypeImpl) ((ParameterizedTypeImpl) returnType)
                        .getActualTypeArguments()[0];
                System.out.println(clazz.getLowerBounds()[0].getTypeName());
            }

        }
        System.exit(0);
    }
}
