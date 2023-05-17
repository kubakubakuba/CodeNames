package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.model.Deck;
import cz.cvut.fel.pjv.codenames.model.Key;
import cz.cvut.fel.pjv.codenames.model.Player;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GuiTesting extends Application {


    Label currentTurnLabel;
    Label promptLabel;
    Label promptCardCountLabel;
    GridPane boardContainer;

    Key key = new Key(Player.PlayerTeam.RED);
    Deck deck = new Deck("src/main/resources/cz/cvut/fel/pjv/codenames/Names.dck");

    private GameController localControl;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage gameStage) {


        currentTurnLabel = new Label("Turn of: ");
        currentTurnLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        currentTurnLabel.setAlignment(Pos.TOP_CENTER);
        HBox turnPane = new HBox();
        turnPane.getChildren().addAll(currentTurnLabel);
        turnPane.setAlignment(Pos.CENTER);
        turnPane.setPadding(new Insets(10));

        promptLabel = new Label("Entered prompt: Sharp");
        promptLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        promptCardCountLabel = new Label("Num Cards: 4");
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
            for (int c = 0; c < 5; c++) {

                String name = deck.getCards().get(r).get(c).getName();
                Label cardLabel = new Label(name);
                Button choiceButton =new Button();
                choiceButton.setPrefSize(200,100);
                choiceButton.setText(name);
                choiceButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");

                boardContainer.add(choiceButton, c, r);

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


        gameStage.show();
    }




//    @Override
//    public void start(Stage gameStage) {
//
//        currentTurn = new Label("Turn of:");
//        currentTurn.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
//        currentTurn.setAlignment(Pos.TOP_CENTER);
//        HBox turnPane = new HBox();
//        turnPane.getChildren().addAll(currentTurn);
//        turnPane.setAlignment(Pos.CENTER);
//        turnPane.setPadding(new Insets(10));
//
//        Key key = new Key(Player.PlayerTeam.RED);
//        Deck deck = new Deck();
//
//        Label promptFieldLabel = new Label("EnterPrompt");
//        promptFieldLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
//        TextField promptField = new TextField();
//
//        Label numPromptLabel = new Label("Num Cards:");
//        numPromptLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
//        TextField numPromptsField = new TextField();
//        numPromptsField.setPrefWidth(40);
//
//        HBox promptBox = new HBox();
//        HBox numPromptBox = new HBox();
//
//        Button commitPrompt = new Button("Commit Prompt");
//
//        Button saveBtn = new Button("Save game");
//
//        promptBox.getChildren().addAll(promptFieldLabel,promptField);
//        promptBox.setSpacing(10);
//
//        numPromptBox.getChildren().addAll(numPromptLabel,numPromptsField);
//        numPromptBox.setSpacing(10);
//
//        HBox spymasterInputBox = new HBox();
//
//        spymasterInputBox.getChildren().addAll(promptBox, numPromptBox, commitPrompt, saveBtn);
//        spymasterInputBox.setSpacing(30);
//        spymasterInputBox.setAlignment(Pos.TOP_CENTER);
//        spymasterInputBox.setStyle("-fx-background-color: gray;");
//        spymasterInputBox.setPadding(new Insets(20));
//
//        VBox upperField = new VBox();
//        upperField.getChildren().addAll(
//                turnPane,
//                spymasterInputBox
//        );
//
//        GridPane boardContainer = new GridPane();
//        boardContainer.setHgap(30);
//        boardContainer.setVgap(30);
//
//        for (int r = 0; r < 5; r++) {
//            for (int c = 0; c < 5; c++) {
//                StackPane cardPane = new StackPane();
//                cardPane.setPrefSize(200, 100);
//                String name = deck.getCards().get(r).get(c).getName();
//                Label cardLabel = new Label(name);
//
//                if(key.getSolution().get(r).get(c) == Key.KeyType.BLUE){
//                    cardPane.setStyle("-fx-background-color: blue;");
//                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
//                }
//                if(key.getSolution().get(r).get(c) == Key.KeyType.RED){
//                    cardPane.setStyle("-fx-background-color: red;");
//                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
//                }
//                if(key.getSolution().get(r).get(c) == Key.KeyType.ASSASSIN){
//                    cardPane.setStyle("-fx-background-color: gray;");
//                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
//                }
//                if(key.getSolution().get(r).get(c) == Key.KeyType.CIVILIAN){
//                    cardPane.setStyle("-fx-background-color: yellow;");
//                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
//                }
//
//                cardPane.getChildren().add(cardLabel);
//                boardContainer.add(cardPane, c, r);
//
//                StackPane.setAlignment(cardLabel, Pos.CENTER);
//            }
//        }
//
//        VBox mainLayout = new VBox();
//        mainLayout.setSpacing(10);
//        mainLayout.setPadding(new Insets(20));
//        mainLayout.getChildren().addAll(
//                upperField,
//                boardContainer
//        );
//
//
//        Scene scene = new Scene(mainLayout, 1000, 800);
//        gameStage.setScene(scene);
//        // gameStage.setTitle("View:"+localControl.getClient().getPlayer().getTeam()+
//        //                     ' ' +localControl.getClient().getPlayer().getRole());
//        gameStage.setOnCloseRequest(event -> {
//            //localControl.disconnect();
//        });
//        gameStage.show();
//    }
}
