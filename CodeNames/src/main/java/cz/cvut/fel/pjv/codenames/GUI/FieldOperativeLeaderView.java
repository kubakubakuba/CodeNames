package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.model.Key;
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
        localControl.getGameData();

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
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(r).get(c);


                Button choiceButton =new Button();
                choiceButton.setPrefSize(200,100);
                if (revealedStatus == Key.KeyType.EMPTY) {
                    choiceButton.setText(name);
                    choiceButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");
                }
                else {
                    String imgpath = "";
                    if (revealedStatus == Key.KeyType.RED) {
                        String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/red_f.jpg",
                                "file:src/main/resources/cz/cvut/fel/pjv/codenames/red_m.jpg"};
                        imgpath = randomize(sa,2);
                    }
                    if (revealedStatus == Key.KeyType.BLUE) {
                        String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_f.jpg",
                                "file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_m.jpg"};
                        imgpath = randomize(sa,2);
                    }
                    if (revealedStatus == Key.KeyType.CIVILIAN) {
                        String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_f.jpg",
                                "file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_m.jpg"};
                        imgpath = randomize(sa,2);

                    }
                    if (revealedStatus == Key.KeyType.ASSASSIN) {
                        imgpath = "file:src/main/resources/cz/cvut/fel/pjv/codenames/ass.jpg";
                    }
                    Image backgroundImage = new Image(imgpath);
                    BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                    BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                    choiceButton.setBackground(new Background(backgroundImg));
                }
                boardContainer.add(choiceButton, r, c);

                choiceButton.setOnAction(e -> {
                    if(!localControl.makeChoice(row, col)){
                        //alert illegal move
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

    public void update()    {
        localControl.getGameData();
        javafx.application.Platform.runLater(() -> {
            currentTurnLabel.setText("Turn of: " + localControl.getCurrentTurnText());

            promptLabel.setText("Entered prompt: " +localControl.getCurrentPromptText());
            promptCardCountLabel.setText("Num Cards:" + localControl.getCurrentPromptCardCount());

            boardContainer = new GridPane();
            boardContainer.setHgap(30);
            boardContainer.setVgap(30);

            for (int r = 0; r < 5; r++) {
                final int row = r;
                for (int c = 0; c < 5; c++) {
                    final int col = c;
                    String name = localControl.getDeck().getCards().get(r).get(c).getName();
                    Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(r).get(c);


                    Button choiceButton =new Button();
                    choiceButton.setPrefSize(200,100);
                    if (revealedStatus == Key.KeyType.EMPTY) {
                        choiceButton.setText(name);
                        choiceButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");
                    }
                    else {
                        String imgpath = "";
                        if (revealedStatus == Key.KeyType.RED) {
                            String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/red_f.jpg",
                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/red_m.jpg"};
                            imgpath = randomize(sa,2);
                        }
                        if (revealedStatus == Key.KeyType.BLUE) {
                            String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_f.jpg",
                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_m.jpg"};
                            imgpath = randomize(sa,2);
                        }
                        if (revealedStatus == Key.KeyType.CIVILIAN) {
                            String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_f.jpg",
                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_m.jpg"};
                            imgpath = randomize(sa,2);

                        }
                        if (revealedStatus == Key.KeyType.ASSASSIN) {
                            imgpath = "file:src/main/resources/cz/cvut/fel/pjv/codenames/ass.jpg";
                        }
                        Image backgroundImage = new Image(imgpath);
                        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                        choiceButton.setBackground(new Background(backgroundImg));
                    }
                    boardContainer.add(choiceButton, r, c);

                    choiceButton.setOnAction(e -> {
                        if(!localControl.makeChoice(row, col)){
                            //alert illegal move
                        }

                    });
                }
            }

        });
    }
    public String randomize(String[] field, int len){
        int randIdx  = new Random().nextInt(len);
        return field[randIdx];
    }
}
