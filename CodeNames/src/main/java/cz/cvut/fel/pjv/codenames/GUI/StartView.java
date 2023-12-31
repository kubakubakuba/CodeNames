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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundPosition;

import java.io.File;

/**
 * @author Prokop Jansa, Jakub Pelc
 * @version 1.0
 */
public class StartView extends Application {

    private String id;
    private String serverIP;

    /**
     * Constructor
     */
    public void startApp() {
        launch();
    }

    /**
     * Start the start window
     * @param stage the stage to show the window on
     */
    @Override
    public void start(Stage stage) {
        // Create a label with custom text
        Label headingLabel = new Label("CodeNames");
        headingLabel.setPadding(new Insets(25));
        headingLabel.setStyle("-fx-font-family: 'Calibri'; -fx-font-size: 62px;");
        Pane titlepane = new BorderPane(headingLabel);

        Label nameLabel = new Label("Enter your name:   ");
        nameLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        // Create the text box
        TextField textField = new TextField();

        Label serverLabel = new Label("Enter server:   ");
        serverLabel.setStyle("-fx-font-family: Impact; -fx-font-size: 20px;");
        // Create the text box

        TextField serverField = new TextField();
        serverField.setText("127.0.0.1:1515");

        // Create the container for the label and text box
        HBox nameContainer = new HBox();
        nameContainer.getChildren().addAll(nameLabel, textField);
        nameContainer.setAlignment(Pos.CENTER);

        HBox serverContainer = new HBox();
        serverContainer.getChildren().addAll(serverLabel, serverField);
        serverContainer.setAlignment(Pos.CENTER);

        Button hostbutton = new Button("Host game");

        Button joinbutton = new Button("Join game");

        VBox buttonbox = new VBox();
        buttonbox.getChildren().addAll(hostbutton, joinbutton);
        buttonbox.setSpacing(10); // Set spacing between elements
        buttonbox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(titlepane,nameContainer, serverContainer, buttonbox);
        vbox.setSpacing(10); // Set spacing between elements
        vbox.setAlignment(Pos.CENTER);

        StackPane bckgrndPane = new StackPane();
        Image backgroundImage = new Image("/background_start.jpeg");
        BackgroundSize backgroundSize = new BackgroundSize(1, 1, true, true, false, false);
        BackgroundImage backgroundImg = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT,
                                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        bckgrndPane.setBackground(new Background(backgroundImg));
        bckgrndPane.getChildren().addAll(vbox);
        bckgrndPane.setAlignment(Pos.CENTER);

        Scene scene = new Scene(bckgrndPane, 650, 600);

        // add functions to the buttons
        joinbutton.setOnAction(e -> {

            id = textField.getText();
            if (textField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in name");
                alert.setHeaderText(null);
                alert.setContentText("The name cannot be empty!");

                alert.showAndWait();
                System.err.println("entered empty name");
                return;
            }
            //set Client id

            serverIP = serverField.getText();
            if(!isValidIpAddress(serverIP)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in server IP");
                alert.setHeaderText(null);
                alert.setContentText("The server IP is not valid!");

                alert.showAndWait();
                System.err.println("entered invalid server IP");
                return;
            }

            String ipaddr = serverIP.split(":")[0];
            int ipport = Integer.parseInt(serverIP.split(":")[1]);

            //join screen
            ServerPicker picker = new ServerPicker(ipaddr, ipport);
            picker.setPreviousScene(scene);
            picker.setStage(stage);
            picker.setID(id);
            // set the new scene on the stage
            stage.setScene(picker.createScene());
        });


        hostbutton.setOnAction(e -> {
            id = textField.getText().replace(";", ",");
            if(id.length() == 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error in name");
                alert.setHeaderText(null);
                alert.setContentText("The name cannot be empty!");

                alert.showAndWait();
                System.err.println("entered empty name");
                return;
            }
            serverIP = serverField.getText();
            String ipaddr = serverIP.split(":")[0];
            int ipport = Integer.parseInt(serverIP.split(":")[1]);

            LobbyController controller = new LobbyController(id, ipaddr, ipport);
            //call create session with id

            switch (controller.createServerSession(id)){
                case 1:
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Unable to host");
                    alert.setHeaderText(null);
                    alert.setContentText("The server is not running");
                    alert.showAndWait();
                    System.err.println("The server is not running");
                    return;
                case 2:
                    System.err.println("Unable to host game");
                    return;
            }
            //call client connect to session
            if (!(controller.connectToSession(id, controller.getLocalClient().getSessionId().toString()) == 0))
            {
                //log connection failed
                return;
            }

            //controller.initSocket();
            //controller.getHostId();
            controller.updatePlayerCount();
            LobbyView lobby = new LobbyView(new Stage(), id, controller, true);
            ChatController chatController = new ChatController(controller.getLocalClient());
            chatController.displayChatWindow();

            controller.setChatController(chatController);
            stage.close();
        });

        stage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        stage.setTitle("Codenames");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Returns the id of the player
     * @return id of the player
     */
    public String getId() {
        return id;
    }

    private boolean isValidIpAddress(String ipString) {
        if(!ipString.contains(":")){
            return false;
        }
        String[] parts = ipString.split(":");
        if (parts.length < 2) {
            return false;
        }

        String ipAddress = parts[0];
        String portString = parts[1];

        String[] octets = ipAddress.split("\\.");

        if (octets.length != 4) {
            return false;
        }

        for (String octet : octets) {
            try {
                int value = Integer.parseInt(octet);

                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        try {
            int port = Integer.parseInt(portString);

            if (port < 0 || port > 65535) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

}
