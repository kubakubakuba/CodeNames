package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.ChatController;
import cz.cvut.fel.pjv.codenames.controller.LobbyController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class ServerPicker extends Application {

    private Scene previousScene;
    private Stage previousStage;
    private String ID;

    private String serverIP;
    private int serverPort;

    private ArrayList<String> sessions;

    public static void main(String[] args) {
        launch(args);
    }

    public ServerPicker(String serverIP, int serverPort){
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        System.out.println("Server IP: " + serverIP + " Server Port: " + serverPort);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }
    public Scene createScene() {
        // Create a VBox to hold the buttons
        VBox buttonContainer = new VBox();
        buttonContainer.setSpacing(10); // Set spacing between buttons
        buttonContainer.setPadding(new Insets(10)); // Set padding around the container


        LobbyController controller = new LobbyController(ID, serverIP, serverPort);
        sessions = controller.getServerSessions();

        for(String label : sessions) {
            Button button = new Button(label);

            button.setOnAction(actionEvent -> {
                //attempt to connect to session
                switch (controller.connectToSession(ID, label)){
                    case 1:
                        connectError("Unexpected server answer, please try again.");
                        return;
                    case 2:
                        connectError("You have the same name as someone else in the session.");
                        return;
                    case 3:
                        connectError("The session does not exist anymore.");
                        return;
                }
                controller.getLocalClient().setSessionId(label);
                controller.setHostId();
                controller.updatePlayerCount();
                LobbyView lobby = new LobbyView(new Stage(), ID, controller, false);
                previousStage.close();
                ChatController chatController = new ChatController(controller.getLocalClient());
                chatController.displayChatWindow();

                controller.setChatController(chatController);
            });
            buttonContainer.getChildren().add(button);
        }

        // Create a ScrollPane and set the buttonContainer as its content
        ScrollPane scrollPane = new ScrollPane(buttonContainer);
        scrollPane.setMaxWidth(300);
        scrollPane.setMaxHeight(200);
        scrollPane.setPrefViewportWidth(150);
        scrollPane.setPrefViewportHeight(200);

        // Create and format label
        Label label = new Label("Pick Session");
        label.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        // Create an HBox to hold the label
        HBox labelBox = new HBox();
        labelBox.setPadding(new Insets(50));
        labelBox.getChildren().add(label);
        labelBox.setAlignment(Pos.CENTER);

        // Create a VBox to hold the labelBox and scrollPane
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().addAll(labelBox, scrollPane);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(50));

        // Create a button with "Return" label
        Button returnButton = new Button("Return");
        returnButton.setOnAction(event ->
        {
            previousStage.setScene(previousScene);
        });

        // Create an HBox to hold the returnButton
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().add(returnButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Create the root VBox to hold vbox and buttonBox
        VBox root = new VBox();
        root.setSpacing(10);
        root.getChildren().addAll(vbox, buttonBox);

        StackPane bckrndPane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/cz/cvut/fel/pjv/codenames/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        bckrndPane.setBackground(new Background(backgroundImg));
        bckrndPane.getChildren().addAll(root);
        bckrndPane.setAlignment(Pos.CENTER);
        // Create the scene
        return new Scene(bckrndPane, 650, 600);
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }
    public void setStage(Stage stage)   {
        this.previousStage = stage;
    }

    public void setID(String ID)    {
        this.ID = ID;
    }

    private void connectError(String errorMsg)   {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Unable to join session");
        alert.setHeaderText(null);
        alert.setContentText(errorMsg);
        alert.showAndWait();
        System.err.println("Unable to join session");
    }
}
