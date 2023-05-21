package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.controller.LobbyController;
import cz.cvut.fel.pjv.codenames.controller.LobbyListener;
import cz.cvut.fel.pjv.codenames.model.Key;
import cz.cvut.fel.pjv.codenames.model.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LobbyView extends Application {

    private String ID;
    private boolean isHost;
    private Label playerCounter;
    private ScrollPane scrollPane;
    private Label redCounter;
    private Label blueCounter;
    private Label localTeam;
    private Label localRole;
    private Stage stage;
    private LobbyController localControl = null;


    public LobbyView(Stage lobbyStage, String ID, LobbyController control, boolean isPlayerHost) {
        this.stage = lobbyStage;
        this.ID = ID;
        this.localControl = control;
        isHost = isPlayerHost;
        localControl.setHostId();
        start(lobbyStage);

        System.out.println("isPlayerHost: " + isPlayerHost);

        Thread serverListenerThread = new Thread(new LobbyListener(this, localControl.getLocalClient()));

        serverListenerThread.start();

    }

    @Override
    public void start(Stage lobbyStage) {
        lobbyStage.setScene(createLobbyScene());
        lobbyStage.setTitle("Lobby hosted by " + localControl.getHostId());

        lobbyStage.setOnCloseRequest(event -> {
            localControl.getChatController().closeChat();
            localControl.disconnect();
            System.exit(0);
        });

        lobbyStage.show();
    }

    /**
     * Updates the scene
     */
    public void update() {
        localControl.updatePlayerCount();
        javafx.application.Platform.runLater(() -> {
            playerCounter.setText("Number of players: " + localControl.getPlayerCount());

            VBox scrollBox = new VBox();

            ArrayList<String> idList = localControl.getIdList();
            for (String id : idList)  {
                scrollBox.getChildren().add(new Label(id));
            }
            scrollPane.setContent(scrollBox);

            redCounter.setText("Number of RED players:" + localControl.getRBNPlayers()[0]);
            blueCounter.setText("Number of BLUE players:" + localControl.getRBNPlayers()[1]);

            localTeam.setText("Your Team: " + localControl.getLocalClient().getPlayer().getTeam());
            localRole.setText("Your Role: " + localControl.getLocalClient().getPlayer().getRole());
        });
    }

    /**
     * Shows an error message when the player is unable to start the game
     */
    public void startGame(){
        javafx.application.Platform.runLater(() -> {
            GameController gameController = new GameController(localControl.getLocalClient(), localControl.getChatController());
            gameController.displayGameWindow();

            this.stage.close();
        });
    }

    /**
     * Creates the lobby scene for the host and the guest
     * @return the lobby scene
     */
    private Scene createLobbyScene() {

        if (isHost) {
            return createHostScene();
        } else {
            return createGuestLobby();
        }
    }

    /**
     * Creates the lobby scene for the host
     * @return the lobby scene
     */
    private Scene createHostScene() {

        playerCounter = new Label("Number of players:" + localControl.getPlayerCount());
        playerCounter.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        FileChooser fileChooser = new FileChooser();

        Button buttonStart = new Button("New game");
        Button buttonLoad = new Button("Load game");
        Button buttonLoadDeck = new Button("Load deck");

        buttonLoadDeck.setOnAction(e -> {
            fileChooser.setTitle("Open Deck File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CodeNames Deck files", "*.dck"));
            File deckFile = fileChooser.showOpenDialog(buttonLoadDeck.getScene().getWindow());
            if (deckFile != null) {
                localControl.setGameDeck(deckFile.getAbsolutePath());
            }
        });

        buttonStart.setOnAction(e -> {
            if(canStartGame()){
                this.stage.close();
                System.out.println("Starting game!");
                localControl.startTheGame();

            }
            else{
                System.out.println("Cannot start game!");
            }
        });

        buttonLoad.setOnAction(e -> {
            if(!canStartGame()) {
                System.err.println("Cannot start game!");
                return;
            }

            FileChooser fChooser = new FileChooser();
            fChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("CodeNames save file", "*.cdn"));
            File selectedFile = fChooser.showOpenDialog(stage);

            if (selectedFile == null) {
                System.err.println("No file selected!");
                return;
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    fileContent.append(line).append("\n");
                }

                String content = fileContent.toString();

                boolean ok = localControl.loadTheGame(content);
                if(ok){
                    this.stage.close();
                    System.out.println("Starting (loading) game!");
                }
                else{
                    System.out.println("Cannot start game!");
                }
            } catch (IOException ex) {
                System.err.println("Error occurred while reading file contents!");
                ex.printStackTrace();
            }
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

    /**
     * Creates the guest lobby scene
     * @return the guest lobby scene
     */
    private Scene createGuestLobby() {
        playerCounter = new Label("Number of players:" + localControl.getPlayerCount());
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

    private VBox createCommonView()  {

        scrollPane = new ScrollPane();
        VBox scrollBox = new VBox();

        ArrayList<String> idList = localControl.getIdList();
        for (String id : idList)  {
            scrollBox.getChildren().add(new Label(id));
        }
        scrollPane.setContent(scrollBox);
        scrollPane.setMaxSize(200,150);

        // Labels
        redCounter = new Label("Number of RED players:" + localControl.getRBNPlayers()[0]);
        blueCounter = new Label("Number of BLUE players:" + localControl.getRBNPlayers()[1]);
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

        localTeam = new Label("Your Team: " + localControl.getLocalClient().getPlayer().getTeam());
        localRole = new Label("Your Role: " + localControl.getLocalClient().getPlayer().getRole());

        localTeam.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        localRole.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        //TODO
        //implement the functionalities of the buttons
        buttonRed.setOnAction(event->   {
            if (!localControl.chooseTeam(Player.PlayerTeam.RED) || !localControl.chooseRole(Player.PlayerRole.NONE)){
                teamError();
                return;
            }
            roleSelect.setValue(null);
            localControl.getLocalClient().getPlayer().setTeam(Player.PlayerTeam.RED);
        });

        buttonBlue.setOnAction(event->   {
            if (!localControl.chooseTeam(Player.PlayerTeam.BLUE) || !localControl.chooseRole(Player.PlayerRole.NONE)){
                teamError();
                return;
            }
            roleSelect.setValue(null);
            localControl.getLocalClient().getPlayer().setTeam(Player.PlayerTeam.BLUE);
        });

        roleSelect.setOnAction(event -> {
            String selectedRole = roleSelect.getSelectionModel().getSelectedItem();
            // Perform actions based on the selected role
            if (selectedRole.equals("Spymaster")) {

                if(!localControl.chooseRole(Player.PlayerRole.SPY_MASTER)){
                    roleError(localControl.getLocalClient().getPlayer().getTeam() == Player.PlayerTeam.NONE);
                    roleSelect.setValue(null);
                    return;
                }
                localControl.getLocalClient().getPlayer().setRole(Player.PlayerRole.SPY_MASTER);

            } else if (selectedRole.equals("Field Operative")) {

                if(!localControl.chooseRole(Player.PlayerRole.FIELD_OPERATIVE)){
                    roleError(localControl.getLocalClient().getPlayer().getTeam() == Player.PlayerTeam.NONE);
                    roleSelect.setValue(null);
                    return;
                }
                localControl.getLocalClient().getPlayer().setRole(Player.PlayerRole.FIELD_OPERATIVE);

            } else if (selectedRole.equals("FOPS Leader")) {

                if(!localControl.chooseRole(Player.PlayerRole.FIELD_OPERATIVE_LEADER)){
                    roleError(localControl.getLocalClient().getPlayer().getTeam() == Player.PlayerTeam.NONE);
                    roleSelect.setValue(null);
                    return;
                }
                localControl.getLocalClient().getPlayer().setRole(Player.PlayerRole.FIELD_OPERATIVE_LEADER);
            }
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

    /**
     * Sets the background of the scene
     * @param layout the layout of the scene
     * @return the scene with the background
     */
    private Scene setBackground(VBox layout) {
        StackPane bckgrndPane = new StackPane();
        Image backgroundImage = new Image("/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        // Create the background image
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        bckgrndPane.setBackground(new Background(backgroundImg));
        bckgrndPane.getChildren().addAll(layout);
        bckgrndPane.setAlignment(Pos.CENTER);
        bckgrndPane.setAlignment(Pos.CENTER);
        return new Scene(bckgrndPane, 650, 600);
    }


    /**
     * Checks if the game can be started
     * @return true if the game can be started, false otherwise
     */
    private boolean canStartGame(){
        localControl.updatePlayerCount();
        localControl.updatePlayerRoles();

        if(localControl.getRBNPlayers()[0] < 2 || localControl.getRBNPlayers()[1] < 2){
            System.out.println("Not enough players in one of the teams");
            System.out.println("Red players: " + localControl.getRBNPlayers()[0]);
            System.out.println("Blue players: " + localControl.getRBNPlayers()[1]);
            return false;
        }

        int[] pRoles = localControl.getPlayerRoles();

        int noneCount = pRoles[6];
        if(pRoles[0] != 1 || pRoles[2] != 1 || pRoles[3] != 1 || pRoles[5] != 1 || noneCount > 0){
            return false;
        }

        return true;
    }

    /**
     * Shows an error message when the player is unable to join a team
     */
    private void teamError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error in Team");
        alert.setHeaderText(null);
        alert.setContentText("Unable to join team");
        alert.showAndWait();
        System.err.println("Unable to join team");
    }

    /**
     * Shows an error message when the player is unable to choose a role
     * @param roleNotChosen true if the player is not in a team, false if the role is already occupied
     */
    private void roleError(boolean roleNotChosen){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error in choosing role");
        alert.setHeaderText(null);
        if (roleNotChosen){
            alert.setContentText("You need to be in a team to choose a role");
            alert.showAndWait();
            System.err.println("You need to be in a team to choose a role");
        }
        else{
            alert.setContentText("Role is already occupied");
            alert.showAndWait();
            System.err.println("Role is already occupied");
        }
    }
}
