package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.LobbyController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.Socket;

public class LobbyView extends Application {

    private String ID;
    private boolean isHost = false;

    private LobbyController localControl = null;

    public LobbyView(Stage lobbyStage, String ID, LobbyController control) {
        this.ID = ID;
        this.localControl = control;
        start(lobbyStage);

        Socket socket = localControl.getLocalClient().getSocket();

        Thread serverListenerThread = new Thread(new LobbyViewListener(socket, this));

        serverListenerThread.start();
    }
    public LobbyView(Stage lobbyStage, String ID, LobbyController control, int x) {
        this.ID = ID;
        this.localControl = control;
        isHost = true;
        localControl.setHostId();
        start(lobbyStage);

        Socket socket = localControl.getLocalClient().getSocket();

        Thread serverListenerThread = new Thread(new LobbyViewListener(socket, this));

        serverListenerThread.start();

    }

    @Override
    public void start(Stage lobbyStage) {
        lobbyStage.setScene(createLobbyScene());
        lobbyStage.setTitle("Lobby hosted by " + localControl.getHostId());
        lobbyStage.show();
    }

    private Scene createLobbyScene() {

        if (ID.equals(localControl.getHostId())) {
            return createHostScene();
        } else {
            return createGuestLobby();
        }
    }

    private Scene createHostScene() {

        Label playerCounter = new Label("Number of players:");
        playerCounter.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        FileChooser fileChooser = new FileChooser();

        Button buttonStart = new Button("Start game");
        Button buttonLoad = new Button("Load game");
        Button buttonLoadDeck = new Button("Load deck");

        buttonLoadDeck.setOnAction(e -> {
            fileChooser.setTitle("Open Deck File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CodeNames Deck files", "*.dck"));
            File deckFile = fileChooser.showOpenDialog(buttonLoadDeck.getScene().getWindow());

        });

        // Set spacing between label1, label2, and buttons
        HBox gameStarter = new HBox(50);
        gameStarter.getChildren().addAll(buttonStart, buttonLoad, buttonLoadDeck);
        gameStarter.setAlignment(Pos.CENTER);

        // Layout
        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(30);
        layout.setPadding(new Insets(30));
        layout.getChildren().addAll(
                playerCounter,
                gameStarter,
                createCommonView()
        );

        return setBackground(layout);

    }

    private Scene createGuestLobby() {
        Label playerCounter = new Label("Number of players:");
        playerCounter.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        VBox layout = new VBox();
        layout.setAlignment(Pos.CENTER);
        layout.setSpacing(30);
        layout.setPadding(new Insets(30));
        layout.getChildren().addAll(
                playerCounter,
                createCommonView()
        );

        return setBackground(layout);
    }

    public VBox createCommonView()  {

        ScrollPane scrollPane = new ScrollPane();
        VBox scrollBox = new VBox();


        //playerlist TODO
        for (int i = 1; i <= 10; i++) {
            scrollBox.getChildren().add(new Label("String " + i));
        }
        scrollPane.setContent(scrollBox);
        scrollPane.setMaxSize(200,150);

        // Labels
        Label redCounter = new Label("Number of RED players:");
        Label blueCounter = new Label("Number of BLUE players:");
        redCounter.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        blueCounter.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");


        // Buttons
        Button buttonRed = new Button("RED");
        Button buttonBlue = new Button("BLUE");

        HBox playerCounters = new HBox(50);
        playerCounters.getChildren().addAll(redCounter, blueCounter);
        playerCounters.setAlignment(Pos.CENTER);

        HBox teamChoosers = new HBox(100);
        teamChoosers.getChildren().addAll(buttonRed, buttonBlue);
        teamChoosers.setAlignment(Pos.CENTER);

        ComboBox<String> roleSelect = new ComboBox<>();
        roleSelect.setPromptText("Select a role");
        roleSelect.getItems().addAll("Spymaster", "Field Operative", "FOPS Leader");

        Label localTeam = new Label("Your Team: ");
        Label localRole = new Label("Your Role: ");

        localTeam.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        localRole.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        //TODO
        //implement the functionalities of the buttons
        buttonRed.setOnAction(event->   {
            //localControl.updateNumPlayers("chooseRED", ID);
                });
        buttonBlue.setOnAction(event->   {
            //localControl.updateNumPlayers("chooseBLUE", ID);
        });

        VBox commonLayout = new VBox();
        commonLayout.setAlignment(Pos.CENTER);
        commonLayout.setSpacing(30);
        commonLayout.setPadding(new Insets(30));
        commonLayout.getChildren().addAll(
                scrollPane,
                playerCounters,
                teamChoosers,
                roleSelect,
                localTeam,
                localRole
        );

        return commonLayout;
    }

    public Scene setBackground(VBox layout) {
        StackPane bckgrndPane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/cz/cvut/fel/pjv/codenames/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        // Create the background image
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        bckgrndPane.setBackground(new Background(backgroundImg));
        bckgrndPane.getChildren().addAll(layout);
        bckgrndPane.setAlignment(Pos.CENTER);
        return new Scene(bckgrndPane, 650, 600);
    }

    public void update(){
        System.out.println("updating player count");
        localControl.setPlayerCount();
    }

    public void teamError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error in Team");
        alert.setHeaderText(null);
        alert.setContentText("Unable to join team");
        alert.showAndWait();
        System.err.println("Unable to join team");
    }

    public void roleError(boolean roleChosen) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error in choosing role");
        alert.setHeaderText(null);
        if (!roleChosen)
        {
            alert.setContentText("You need to be in a team to choose a role");
            alert.showAndWait();
            System.err.println("You need to be in a team to choose a role");
        }
        else {
            alert.setContentText("Role is already occupied");
            alert.showAndWait();
            System.err.println("Role is already occupied");
        }
    }

}
