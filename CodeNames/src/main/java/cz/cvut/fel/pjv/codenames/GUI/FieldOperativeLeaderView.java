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

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class FieldOperativeLeaderView extends Application {

    Label currentTurnLabel;
    Label promptLabel;
    Label promptCardCountLabel;
    GridPane boardContainer;
    private GameController localControl;
    private Button[][] buttons = new Button[5][5];
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
                buttons[r][c] = choiceButton;

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
            //TODO: save game on host PC and terminate gamelobby
            localControl.getChatController().closeChat();
            localControl.disconnect();
        });

        saveBtn.setOnAction(actionEvent -> {
            localControl.saveGame();
        });

        gameStage.show();
    }

    public void update()    {
        System.out.println("update");

        ArrayList<ArrayList<Key.KeyType>> old = localControl.getRevealedCardsBoard();
        localControl.getGameData();
        int [] idxs = localControl.getChangedTileIdx(old, localControl.getRevealedCardsBoard()); //the indexes of button that changed
        javafx.application.Platform.runLater(() -> {
            currentTurnLabel.setText("Turn of: " + localControl.getCurrentTurnText());

            promptLabel.setText("Entered prompt: " +localControl.getCurrentPromptText());
            promptCardCountLabel.setText("Num Cards:" + localControl.getCurrentPromptCardCount());

            int row = idxs[0];
            int col = idxs[1];

            if (idxs[0] != -1) {
                String name = localControl.getDeck().getCards().get(row).get(col).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(row).get(col);
                System.out.println("Codename " + name + " revealed as " + revealedStatus);

                Button choiceButton = buttons[row][col];  // Get button from array
                choiceButton.setPrefSize(200, 100);

                System.out.println("revealed status: " + revealedStatus + "keytype equals" + revealedStatus.equals(Key.KeyType.EMPTY));
                if (revealedStatus == Key.KeyType.EMPTY) {
                    System.out.println("reveal type empty");
                    choiceButton.setText(name);
                    choiceButton.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");
                } else {
                    String imgpath = "";
                    if (revealedStatus == Key.KeyType.RED) {
                        String[] sa = {"file:src/main/resources/cz/cvut/fel/pjv/codenames/red_f.jpg",
                                "file:src/main/resources/cz/cvut/fel/pjv/codenames/red_m.jpg"};
                        imgpath = randomize(sa, 2);
                        choiceButton.setText("");
                    }
                    if (revealedStatus == Key.KeyType.BLUE) {
                        String[] sa = {"file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_f.jpg",
                                "file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_m.jpg"};
                        imgpath = randomize(sa, 2);
                        choiceButton.setText("");
                    }
                    if (revealedStatus == Key.KeyType.CIVILIAN) {
                        String[] sa = {"file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_f.jpg",
                                "file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_m.jpg"};
                        imgpath = randomize(sa, 2);
                        choiceButton.setText("");

                    }
                    if (revealedStatus == Key.KeyType.ASSASSIN) {
                        imgpath = "file:src/main/resources/cz/cvut/fel/pjv/codenames/ass.jpg";
                        choiceButton.setText("");
                    }
                    System.out.println("imgpath: " + imgpath);
                    Image backgroundImage = new Image(imgpath);
                    BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                    BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                    choiceButton.setBackground(new Background(backgroundImg));
                }

                choiceButton.setOnAction(e -> {
                    if (!localControl.makeChoice(row, col)) {
                        System.err.println("Invalid choice!");
                    }
                });
            }
        });
    }
    public String randomize(String[] field, int len){
        int randIdx  = new Random().nextInt(len);
        return field[randIdx];
    }

    public void gameEnd() {
        for(Button[] button : buttons){
            for(Button b : button){
                b.setDisable(true);
            }
        }
    }
}
