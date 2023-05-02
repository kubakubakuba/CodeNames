package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.model.Board;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundPosition;


public class StartView extends Application {

    public static void main() {
        launch();
    }

    @Override
    public void start(Stage stage) {

        // Create a label with custom text
        Label headingLabel = new Label("CodeNames");
        headingLabel.setPadding(new Insets(25));
        headingLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 62px;");
        Pane titlepane = new BorderPane(headingLabel);

        Label nameLabel = new Label("Enter your name:   ");
        nameLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        // Create the text box
        TextField textField = new TextField();


        // Create the container for the label and text box
        HBox nameContainer = new HBox();
        nameContainer.getChildren().addAll(nameLabel, textField);
        nameContainer.setAlignment(Pos.CENTER);

        Button hostbutton = new Button("Host game");

        Button joinbutton = new Button("Join game");


        VBox buttonbox = new VBox();
        buttonbox.getChildren().addAll(hostbutton, joinbutton);
        buttonbox.setSpacing(10); // Set spacing between elements
        buttonbox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(titlepane,nameContainer, buttonbox);
        vbox.setSpacing(10); // Set spacing between elements
        vbox.setAlignment(Pos.CENTER);

        StackPane bckgrndPane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/cz/cvut/fel/pjv/codenames/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        // Create the background image
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        bckgrndPane.setBackground(new Background(backgroundImg));
        bckgrndPane.getChildren().addAll(vbox);
        bckgrndPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(bckgrndPane, 650, 600);

        // add functions to the buttons
        joinbutton.setOnAction(e -> {

            String inputId = textField.getText();

            //call create client

            //join screen
            ServerPicker picker = new ServerPicker();
            picker.setPreviousScene(scene);
            picker.setStage(stage);
            picker.setID(inputId);
            // set the new scene on the stage
            stage.setScene(picker.createScene());
        });

        hostbutton.setOnAction(e -> {

            String inputId = textField.getText();

            //call create server with id
            //set id to hostid of the server

            //call create client

            //call client connect

            //TODO -redirect to new lobby
            LobbyView lobby = new LobbyView(new Stage(), inputId, true);
            stage.close();
        });

        stage.setTitle("Codenames");
        stage.setScene(scene);
        stage.show();
    }


}
