package cz.cvut.fel.pjv.codenames.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.*;

public class ServerPicker extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
    public Scene createScene() {
        // Create a VBox to hold the buttons
        VBox buttonContainer = new VBox();
        buttonContainer.setSpacing(10); // Set spacing between buttons
        buttonContainer.setPadding(new Insets(10)); // Set padding around the container

        // Add buttons to the VBox
        for (int i = 1; i <= 20; i++) {
            Button button = new Button("Button " + i);
            buttonContainer.getChildren().add(button);
        }

        // Create a ScrollPane and set the buttonContainer as its content
        ScrollPane scrollPane = new ScrollPane(buttonContainer);
        scrollPane.setFitToWidth(true); // Enable horizontal scroll bar if needed

        // Create a label
        Label label = new Label("Pick Session");

        // Create an HBox to hold the label
        HBox labelBox = new HBox();
        labelBox.setPadding(new Insets(10));
        labelBox.getChildren().add(label);

        // Create a VBox to hold the labelBox and scrollPane
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(labelBox, scrollPane);

        // Create a button with "Return" label
        Button returnButton = new Button("Return");
        //returnButton.setOnAction(event -> ));

        // Create an HBox to hold the returnButton
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(returnButton);

        // Create the root VBox to hold vbox and buttonBox
        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(vbox, buttonBox);

        // Create the scene
        return new Scene(root, 300, 400);
    }

}
