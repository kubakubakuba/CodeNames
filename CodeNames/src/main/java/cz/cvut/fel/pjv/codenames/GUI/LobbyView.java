package cz.cvut.fel.pjv.codenames.GUI;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LobbyView extends Application {

    private String ID;
    private boolean isHost;

    public LobbyView(Stage lobbyStage, String ID, boolean isHost) {
        this.ID = ID;
        this.isHost = isHost;
        start(lobbyStage);

    }

    @Override
    public void start(Stage lobbyStage) {
        lobbyStage.setScene(createLobbyScene());
        lobbyStage.setTitle("Lobby hosted by ");
        lobbyStage.show();
    }

    private Scene createLobbyScene() {

        if (isHost) {
            return createHostScene();
        } else {
            return createGuestLobby();
        }
    }

    private Scene createHostScene() {

        Label playerCounter = new Label("Number of players:");
        playerCounter.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");

        Button buttonStart = new Button("Start game");
        Button buttonLoad = new Button("Load game");

        // Set spacing between label1, label2, and buttons
        HBox gameStarter = new HBox(50);
        gameStarter.getChildren().addAll(buttonStart, buttonLoad);
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
        StackPane bckgrndPane = new StackPane();
        Image backgroundImage = new Image("file:src/main/resources/cz/cvut/fel/pjv/codenames/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        // Create the background image
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        bckgrndPane.setBackground(new Background(backgroundImg));
        bckgrndPane.getChildren().addAll(layout);
        bckgrndPane.setAlignment(Pos.CENTER);

        //TODO
        //implement the functionalities of the buttons

        return new Scene(bckgrndPane, 650, 600);

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
}
