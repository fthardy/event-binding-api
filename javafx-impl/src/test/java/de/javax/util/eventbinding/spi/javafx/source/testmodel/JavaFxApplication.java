package de.javax.util.eventbinding.spi.javafx.source.testmodel;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JavaFxApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root);

        stage.setScene(scene);
    }
    /*
     * public class YourApplication extends Application {
     * 
     * 
     * public static void main(String[] args) { Application.launch(args); }
     * 
     * @Override public void start(Stage primaryStage) { primaryStage.setTitle("Hello World"); Group root = new Group();
     * Scene scene = new Scene(root, 300, 250); Button btn = new Button(); btn.setLayoutX(100); btn.setLayoutY(80);
     * btn.setText("Hello World"); btn.setOnAction(new EventHandler<ActionEvent>() {
     * 
     * public void handle(ActionEvent event) { System.out.println("Hello World"); } }); root.getChildren().add(btn);
     * primaryStage.setScene(scene); primaryStage.show(); } }
     */

}
