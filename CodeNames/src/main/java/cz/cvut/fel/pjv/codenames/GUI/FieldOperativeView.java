package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.model.Client;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class FieldOperativeView extends Application {

    Label currentTurnLabel;
    Label promptLabel;
    Label promptCardCountLabel;
    GridPane boardContainer;
    private GameController localControl;

    private Button saveBtn;
    private StackPane[][] stackPane = new StackPane[5][5];
    public FieldOperativeView(GameController controller) {
        this.localControl = controller;
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

        promptCardCountLabel = new Label("Num Cards:" + localControl.getCurrentPromptCardCount());
        promptCardCountLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        saveBtn = new Button("Save game");

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
                StackPane cardPane = new StackPane();
                cardPane.setPrefSize(202, 162);

                String name = localControl.getDeck().getCards().get(r).get(c).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(r).get(c);

                Label cardLabel = new Label(name);
                cardLabel.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");

                if (revealedStatus == Key.KeyType.EMPTY) {
                    cardPane.getChildren().add(cardLabel);
                    cardPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY, new BorderWidths(5))));
                    cardPane.setAlignment(cardLabel, Pos.CENTER);
                }
                else {
                    String imgpath = localControl.getImage(revealedStatus);
                    Image backgroundImage = new Image(imgpath);
                    BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                    BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                    cardPane.setBackground(new Background(backgroundImg));
                }
                boardContainer.add(cardPane, r, c);
                stackPane[r][c] = cardPane;
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
        int [] idxs = localControl.getChangedTileIdx(old, localControl.getRevealedCardsBoard()); //the indexes of button that changed
        javafx.application.Platform.runLater(() -> {

            int row = idxs[0];
            int col = idxs[1];

            if (!localControl.hasGameEnded())
                currentTurnLabel.setText("Turn of: " + localControl.getCurrentTurnText());
            else
                currentTurnLabel.setText("Winners are: " + localControl.getWinner());

            promptLabel.setText("Entered prompt: " +localControl.getCurrentPromptText());
            promptCardCountLabel.setText("Num Cards:" + localControl.getCurrentPromptCardCount());

            boardContainer = new GridPane();
            boardContainer.setHgap(30);
            boardContainer.setVgap(30);

            if(row != -1) {
                StackPane cardPane = new StackPane();
                cardPane.setPrefSize(200, 100);

                String name = localControl.getDeck().getCards().get(row).get(col).getName();
                Key.KeyType revealedStatus = localControl.getRevealedCardsBoard().get(row).get(col);

                if (revealedStatus == Key.KeyType.EMPTY) {
                    cardPane.setStyle("-fx-font-size: 20; -fx-font-family: Tahoma");
                    cardPane.getChildren().clear();
                } else {
                    String imgpath = localControl.getImage(revealedStatus);
                    Image backgroundImage = new Image(imgpath);
                    BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
                    BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
                    stackPane[idxs[0]][idxs[1]].getChildren().clear();
                    stackPane[idxs[0]][idxs[1]].setBackground(new Background(backgroundImg));
                }

                stackPane[row][col] = cardPane;
            }
        });

    }

    /**
     * Disables the save button when the game ends
     */
    public void gameEnd() {
        if(localControl.hasGameEnded()){
            saveBtn.setDisable(true);
        }
    }
}
