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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SpymasterView extends Application {

    Label currentTurnLabel;
    String currentTurnText;
    private GameController localControl;
    public SpymasterView(GameController controller) {
        //super(controller);
        this.localControl = controller;

        localControl.getSessionKey();
        localControl.getSessionDeck();

        start(stage);
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
                StackPane cardPane = new StackPane();
                cardPane.setPrefSize(200, 100);
                String name = localControl.getDeck().getCards().get(r).get(c).getName();
                Label cardLabel = new Label(name);

                if(localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.BLUE){
                    cardPane.setStyle("-fx-background-color: blue;");
                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
                }
                if(localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.RED){
                    cardPane.setStyle("-fx-background-color: red;");
                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
                }
                if(localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.ASSASSIN){
                    cardPane.setStyle("-fx-background-color: gray;");
                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: white");
                }
                if(localControl.getKey().getSolution().get(r).get(c) == Key.KeyType.CIVILIAN){
                    cardPane.setStyle("-fx-background-color: yellow;");
                    cardLabel.setStyle("-fx-font-family: Tahoma; -fx-font-size: 20px;-fx-text-fill: black");
                }

                cardPane.getChildren().add(cardLabel);
                boardContainer.add(cardPane, c, r);

                cardPane.setAlignment(cardLabel, Pos.CENTER);
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
            if(!localControl.commitPrompt()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in commiting prompt");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Prompt format");
                alert.showAndWait();
                System.err.println("Inputted invalid prompt");
            }
        });

        saveBtn.setOnAction(actionEvent -> {
            localControl.saveGame();
        });

        gameStage.show();
    }


    public void update()    {

    }
}
