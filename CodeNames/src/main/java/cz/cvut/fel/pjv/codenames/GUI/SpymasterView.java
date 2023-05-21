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
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class SpymasterView extends Application {

    Label currentTurnLabel;
    StackPane[][] cardPanes  = new StackPane[5][5];
    private HBox promptBox;
    private HBox numPromptBox;
    private Button commitPrompt;
    private GameController localControl;
    private Button saveBtn;

    public SpymasterView(GameController controller) {
        this.localControl = controller;
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

        promptBox = new HBox();
        numPromptBox = new HBox();

        commitPrompt = new Button("Commit Prompt");

        saveBtn = new Button("Save game");

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
                cardPanes[r][c].setPrefSize(202, 162);

                String name = localControl.getDeck().getCards().get(r).get(c).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(r).get(c);
                Label cardLabel = new Label(name);

                Image blueBgImagePath = new Image(getClass().getResource("/cards/word_blue.png").toString());
                Image redBgImagePath = new Image(getClass().getResource("/cards/word_red.png").toString());
                Image civBgImagePath = new Image(getClass().getResource("/cards/word_civ.png").toString());
                Image blackBgImagePath = new Image(getClass().getResource("/cards/word_black.png").toString());

                BackgroundSize bgSize = new BackgroundSize(1, 1, true, true, false, false);
                BackgroundImage bgImgBlue = new BackgroundImage(blueBgImagePath, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
                BackgroundImage bgImgRed = new BackgroundImage(redBgImagePath, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
                BackgroundImage bgImgCiv = new BackgroundImage(civBgImagePath, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);
                BackgroundImage bgImgBlack = new BackgroundImage(blackBgImagePath, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bgSize);

                if (revealedStatus == Key.KeyType.EMPTY) {
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.BLUE) {
                        //cardPanes[r][c].setStyle("-fx-background-color: blue;");
                        cardPanes[r][c].setBackground(new Background(bgImgBlue));
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black;");
                    }
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.RED) {
                        cardPanes[r][c].setBackground(new Background(bgImgRed));
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black;");
                    }
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.ASSASSIN) {
                        cardPanes[r][c].setBackground(new Background(bgImgBlack));
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white;");
                    }
                    if (localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.CIVILIAN) {
                        cardPanes[r][c].setBackground(new Background(bgImgCiv));
                        cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black;");
                    }

                    cardLabel.getTransforms().add(new Translate(0, -17));
                    cardPanes[r][c].getChildren().add(cardLabel);
                }
                else {
                    String imgpath = localControl.getImage(revealedStatus);
                    Image backgroundImage = new Image(imgpath);
                    BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                    BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                    cardPanes[r][c].setBackground(new Background(backgroundImg));
                }
                boardContainer.add(cardPanes[r][c], r, c);

                cardPanes[r][c].setAlignment(cardLabel, Pos.BOTTOM_CENTER);
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
            System.exit(0);
        });

        commitPrompt.setOnAction(actionEvent -> {
            if(!localControl.commitPrompt(promptField.getText(), parseInt( numPromptsField.getText()))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in commiting prompt");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Prompt format or it is not your turn!");
                alert.showAndWait();
                return;
            }
        });

        saveBtn.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CodeNames files (*.cdn)", "*.cdn");
            fileChooser.getExtensionFilters().add(extFilter);

            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                localControl.saveGame(file);
            }
            else{
                System.err.println("File is null");
            }
        });

        gameStage.show();
    }


    public void update()    {
        ArrayList<ArrayList<Key.KeyType>> old = localControl.getRevealedCardsBoard();
        localControl.getGameData();
        int [] idxs = localControl.getChangedTileIdx(old, localControl.getRevealedCardsBoard());

        javafx.application.Platform.runLater(() -> {

            if (!localControl.hasGameEnded())
                currentTurnLabel.setText("Turn of: " + localControl.getCurrentTurnText());
            else
                currentTurnLabel.setText("Winners are: " + localControl.getWinner());

            if (idxs[0] != -1) {
                String name = localControl.getDeck().getCards().get(idxs[0]).get(idxs[1]).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(idxs[0]).get(idxs[1]);
                System.out.println("x: " + idxs[0] + " y: " + idxs[1]);
                System.out.println("revealed status: " + revealedStatus);
                //Label cardLabel = new Label(name);

                String imgpath = localControl.getImage(revealedStatus);
                Image backgroundImage = new Image(imgpath);
                BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                cardPanes[idxs[0]][idxs[1]].getChildren().clear();
                cardPanes[idxs[0]][idxs[1]].setBackground(new Background(backgroundImg));
            }
        });
    }

    /**
     * Ends the game, disables the commit button and the prompt field
     * allows user to save the game if it has not been saved yet
     */
    public void gameEnd() {
        commitPrompt.setDisable(true);
        promptBox.setDisable(true);
        numPromptBox.setDisable(true);
        if(localControl.hasGameEnded()){
            saveBtn.setDisable(true);
        }
    }
}
