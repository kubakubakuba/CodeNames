package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.model.Board;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StartView extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {



        // Create a label with custom text
        Label headingLabel = new Label("CodeNames");
        headingLabel.setPadding(new Insets(25));
        headingLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 62px;");
        Pane titlepane = new BorderPane(headingLabel);

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

        VBox buttonbox = new VBox();
        buttonbox.getChildren().addAll(hostbutton, joinbutton);
        buttonbox.setSpacing(10); // Set spacing between elements
        buttonbox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(titlepane, buttonbox);
        vbox.setSpacing(10); // Set spacing between elements
        vbox.setAlignment(Pos.CENTER);

        StackPane stackPane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/cz/cvut/fel/pjv/codenames/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(700, 600, true, true, true, true);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        stackPane.setBackground(new Background(backgroundImg));

        // Add the BorderPane and VBox to the StackPane
        stackPane.getChildren().addAll(vbox);
        stackPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(stackPane, 700, 600);

        stage.setTitle("Codenames");
        stage.setScene(scene);
        stage.show();
    }
}
