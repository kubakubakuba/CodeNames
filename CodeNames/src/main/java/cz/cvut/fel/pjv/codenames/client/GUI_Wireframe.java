package cz.cvut.fel.pjv.codenames.client;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class GUI_Wireframe extends Application {

    //testing version of gui
    @Override
    public void start(Stage primaryStage) {

        // Create a button
        Button btn = new Button();
        btn.setText("say 'Hello world!'");
        btn.setOnAction(event -> System.out.println("Hello world!"));

        // Add the button to a layout
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        // Create a scene and add it to the stage
        Scene scene = new Scene(root, 600, 450);
        primaryStage.setScene(scene);
        primaryStage.setTitle("My window");
        primaryStage.show();
    }

    public static void main() {
        launch();
    }
}