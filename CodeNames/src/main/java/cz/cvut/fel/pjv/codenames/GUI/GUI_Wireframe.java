package cz.cvut.fel.pjv.codenames.GUI;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GUI_Wireframe extends Application {

    //testing version of gui
    @Override
    public void start(Stage stage) {
        // Create a label with custom text
        Label headingLabel = new Label("Welcome to CodeNames!");
        headingLabel.setStyle("-fx-font-size: 62px;"); // Set the font size

        // Create buttons
        Button hostbutton = new Button("Host game");
        hostbutton.setOnAction(e -> {
            //TODO -redirect to new lobby
        });

        Button joinbutton = new Button("Join game");
        joinbutton.setOnAction(e -> {
            //TODO - implement join screen
            Scene scene1 = new Scene(new Label("This is Scene 2"), 700, 600);
            // set the new scene on the stage
            stage.setScene(scene1);
        });

        // Create a VBox for the buttons
        VBox buttonbox = new VBox();
        buttonbox.getChildren().addAll(hostbutton, joinbutton);
        buttonbox.setSpacing(10); // Set spacing between elements
        buttonbox.setAlignment(Pos.CENTER);

        // Create a BorderPane for the title label
        BorderPane titlepane = new BorderPane();
        titlepane.setCenter(headingLabel);

        // Create a StackPane for the background image
        StackPane stackpane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/cz/cvut/fel/pjv/codenames/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(700, 600, true, true, true, true);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        stackpane.setBackground(new Background(backgroundImg));

        // Add the BorderPane and VBox to the StackPane
        stackpane.getChildren().addAll(titlepane, buttonbox);
        stackpane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(stackpane, 700, 600);

        stage.setTitle("Codenames");
        stage.setScene(scene);
        stage.show();
    }

    public static void main() {
        launch();
    }
}