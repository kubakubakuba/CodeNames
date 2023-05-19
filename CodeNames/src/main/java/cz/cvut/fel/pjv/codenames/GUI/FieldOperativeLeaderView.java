package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class FieldOperativeLeaderView extends Application {

    Label currentTurnLabel;
    Label promptLabel;
    Label promptCardCountLabel;
    GridPane boardContainer;
    private GameController localControl;
    public FieldOperativeLeaderView(GameController controller) {
        //super(controller);
        this.localControl = controller;
        //start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage gameStage) {

        localControl.getCurrentTurn();
        currentTurnLabel = new Label("Turn of: " + localControl.getCurrentTurnText());
        currentTurnLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        currentTurnLabel.setAlignment(Pos.TOP_CENTER);
        HBox turnPane = new HBox();
        turnPane.getChildren().addAll(currentTurnLabel);
        turnPane.setAlignment(Pos.CENTER);
        turnPane.setPadding(new Insets(10));

        promptLabel = new Label("Entered prompt: " +localControl.getCurrentPromptText());
        promptLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        promptCardCountLabel = new Label("Num Cards: " + localControl.getCurrentPromptCardCount());
        promptCardCountLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        Button saveBtn = new Button("Save game");

        HBox spymasterOutputBox = new HBox();

        spymasterOutputBox.getChildren().addAll(promptLabel,promptCardCountLabel, saveBtn);
        spymasterOutputBox.setSpacing(30);
        spymasterOutputBox.setAlignment(Pos.TOP_CENTER);
        spymasterOutputBox.setStyle("-fx-background-color: gray;");
        spymasterOutputBox.setPadding(new Insets(20));

        VBox upperField = new VBox();
        upperField.getChildren().addAll(
                turnPane,
                spymasterOutputBox
        );

        boardContainer = new GridPane();
        boardContainer.setHgap(30);
        boardContainer.setVgap(30);

        for (int r = 0; r < 5; r++) {
            final int row = r;
            for (int c = 0; c < 5; c++) {
                final int col = c;
                String name = localControl.getDeck().getCards().get(r).get(c).getName();

                Button choiceButton =new Button();
                choiceButton.setPrefSize(200,100);
                choiceButton.setText(name);
                choiceButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");

                boardContainer.add(choiceButton, c, r);

                choiceButton.setOnAction(e -> {
                    if(!localControl.makeChoice(name)){
                        //alert
                    }
                });
            }
        }

        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(
                upperField,
                boardContainer
        );
        Scene scene = new Scene(mainLayout, 1000, 800);
        gameStage.setScene(scene);
        gameStage.setTitle("View:"+localControl.getClient().getPlayer().getTeam()+
                ' ' +localControl.getClient().getPlayer().getRole());


        gameStage.setOnCloseRequest(event -> {
            localControl.disconnect();
        });

        saveBtn.setOnAction(actionEvent -> {
            localControl.saveGame();
        });

        gameStage.show();
    }

}
