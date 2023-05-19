package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.model.Key;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class SpymasterView extends Application {

    Label currentTurnLabel;
    //GridPane boardContainer;
    StackPane[][] cardPanes  = new StackPane[5][5];

    private GameController localControl;
    public SpymasterView(GameController controller) {
        //super(controller);
        this.localControl = controller;

        //start(stage);
    }

    public static void main() {
        launch();
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

        Label promptFieldLabel = new Label("EnterPrompt");
        promptFieldLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        TextField promptField = new TextField();

        Label numPromptLabel = new Label("Num Cards:");
        numPromptLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        TextField numPromptsField = new TextField();
        numPromptsField.setPrefWidth(40);

        HBox promptBox = new HBox();
        HBox numPromptBox = new HBox();

        Button commitPrompt = new Button("Commit Prompt");

        Button saveBtn = new Button("Save game");

        promptBox.getChildren().addAll(promptFieldLabel,promptField);
        promptBox.setSpacing(10);

        numPromptBox.getChildren().addAll(numPromptLabel,numPromptsField);
        numPromptBox.setSpacing(10);

        HBox spymasterInputBox = new HBox();

        spymasterInputBox.getChildren().addAll(promptBox, numPromptBox, commitPrompt, saveBtn);
        spymasterInputBox.setSpacing(30);
        spymasterInputBox.setAlignment(Pos.TOP_CENTER);
        spymasterInputBox.setStyle("-fx-background-color: gray;");
        spymasterInputBox.setPadding(new Insets(20));

        VBox upperField = new VBox();
        upperField.getChildren().addAll(
                turnPane,
                spymasterInputBox
        );

        GridPane boardContainer = new GridPane();
        boardContainer.setHgap(30);
        boardContainer.setVgap(30);

        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                cardPanes[r][c] = new StackPane();
                cardPanes[r][c].setPrefSize(200, 100);

                String name = localControl.getDeck().getCards().get(r).get(c).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(r).get(c);
                Label cardLabel = new Label(name);

                if (revealedStatus == Key.KeyType.EMPTY) {
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.BLUE) {
                        cardPanes[r][c].setStyle("-fx-background-color: blue;");
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
                    }
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.RED) {
                        cardPanes[r][c].setStyle("-fx-background-color: red;");
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
                    }
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.ASSASSIN) {
                        cardPanes[r][c].setStyle("-fx-background-color: gray;");
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
                    }
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.CIVILIAN) {
                        cardPanes[r][c].setStyle("-fx-background-color: yellow;");
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
                    }

                    cardPanes[r][c].getChildren().add(cardLabel);
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
                    cardPanes[r][c].setBackground(new Background(backgroundImg));
                }
                boardContainer.add(cardPanes[r][c], r, c);

                cardPanes[r][c].setAlignment(cardLabel, Pos.CENTER);
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

        commitPrompt.setOnAction(actionEvent -> {
            if(!localControl.commitPrompt(promptField.getText(), parseInt( numPromptsField.getText()))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in commiting prompt");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Prompt format");
                alert.showAndWait();
                System.err.println("Inputted invalid prompt");
                return;
            }
        });

        saveBtn.setOnAction(actionEvent -> {
            localControl.saveGame();
        });

        gameStage.show();
    }


    public void update()    {

        ArrayList<ArrayList<Key.KeyType>> old = localControl.getRevealedCardsBoard();
        localControl.getGameData();
        int [] idxs = localControl.getChangedTileIdx(old, localControl.getRevealedCardsBoard());

        javafx.application.Platform.runLater(() -> {
            currentTurnLabel.setText("Turn of: " + localControl.getCurrentTurnText());
            if (idxs[0] != -1) {
                String name = localControl.getDeck().getCards().get(idxs[0]).get(idxs[1]).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(idxs[0]).get(idxs[1]);
                System.out.println("x: " + idxs[0] + " y: " + idxs[1]);
                System.out.println("revealed status: " + revealedStatus);
                //Label cardLabel = new Label(name);

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
                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_m.png"};
                    imgpath = randomize(sa,2);
                }
                if (revealedStatus == Key.KeyType.ASSASSIN) {
                    imgpath = "file:src/main/resources/cz/cvut/fel/pjv/codenames/ass.jpg";
                }
                Image backgroundImage = new Image(imgpath);
                BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                cardPanes[idxs[0]][idxs[1]].getChildren().clear();
                cardPanes[idxs[0]][idxs[1]].setBackground(new Background(backgroundImg));
            }


//            for (int r = 0; r < 5; r++) {
//                for (int c = 0; c < 5; c++) {
//                    //StackPane cardPane = new StackPane();
//                    //cardPane.setPrefSize(200, 100);
//
//                    String name = localControl.getDeck().getCards().get(r).get(c).getName();
//                    Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(r).get(c);
//                    Label cardLabel = new Label(name);
//
//                    if (revealedStatus == Key.KeyType.EMPTY) {
//                        if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.BLUE) {
//                            cardPanes[r][c].setStyle("-fx-background-color: blue;");
//                            cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
//                        }
//                        if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.RED) {
//                            cardPanes[r][c].setStyle("-fx-background-color: red;");
//                            cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
//                        }
//                        if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.ASSASSIN) {
//                            cardPanes[r][c].setStyle("-fx-background-color: gray;");
//                            cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
//                        }
//                        if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.CIVILIAN) {
//                            cardPanes[r][c].setStyle("-fx-background-color: yellow;");
//                            cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
//                        }
//
//                        //cardPanes[r][c].getChildren().add(cardLabel);
//                    }
//                    else {
//                        String imgpath = "";
//                        if (revealedStatus == Key.KeyType.RED) {
//                            String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/red_f.jpg",
//                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/red_m.jpg"};
//                            imgpath = randomize(sa,2);
//                        }
//                        if (revealedStatus == Key.KeyType.BLUE) {
//                            String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_f.jpg",
//                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/blu_m.jpg"};
//                            imgpath = randomize(sa,2);
//                        }
//                        if (revealedStatus == Key.KeyType.CIVILIAN) {
//                            String[] sa= {"file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_f.jpg",
//                                    "file:src/main/resources/cz/cvut/fel/pjv/codenames/civ_m.png"};
//                            imgpath = randomize(sa,2);
//
//                        }
//                        if (revealedStatus == Key.KeyType.ASSASSIN) {
//                            imgpath = "file:src/main/resources/cz/cvut/fel/pjv/codenames/ass.jpg";
//                        }
//                        Image backgroundImage = new Image(imgpath);
//                        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
//                        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
//                                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
//                        cardPanes[r][c].getChildren().remove(name);
//                        cardPanes[r][c].setBackground(new Background(backgroundImg));
//                        cardPanes[r][c].setStyle("-fx-background-color: transparent;");
//
//                    }
//                    //cardPanes[r][c].setAlignment(cardLabel, Pos.CENTER);
//                }
//            }
        });
    }

    public String randomize(String[] field, int len){
        int randIdx  = new Random().nextInt(len);
        return field[randIdx];
    }

    public void gameEnd() {
    }
}
