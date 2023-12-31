package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.ChatController;
import cz.cvut.fel.pjv.codenames.model.Client;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChatView extends Application {

    private Stage stage;
    private TextArea chatLog;
    private TextArea inputField;
    private boolean enabled = true;
    private ChatController chatControl;
    private Client localClient;
    private double xOffset = 0;
    private double yOffset = 0;

    /**
     * Constructor
     * @param chatControl the chat controller
     * @param client the client
     */
    public ChatView(ChatController chatControl, Client client) {
        this.chatControl = chatControl;
        this.localClient = client;
    }

    /**
     * Start the chat window
     * @param stage the stage to show the window on
     */
    public void start(Stage stage) {
        this.stage = stage;
        stage.initStyle(StageStyle.UNDECORATED);
        showChatWindow("CodeNames Chat - " + localClient.getId());
    }

    /**
     * Show the chat window
     * @param title the title of the window
     */
    public void showChatWindow(String title) {
        Label msgLabel = new Label("Chat log");
        chatLog = new TextArea();
        chatLog.setEditable(false);
        chatLog.setWrapText(true);
        VBox.setVgrow(chatLog, Priority.ALWAYS);
        Label inputLabel = new Label("Send message");
        inputField = new TextArea();
        inputField.setMaxHeight(28);
        inputField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if(enabled) {
                    sendMessage();
                }
            }
        });
        Button sendButton = new Button("Send");
        sendButton.setOnAction((ActionEvent e) -> {
            if(enabled) {
                sendMessage();
            }
        });
        VBox vbox = new VBox(4, msgLabel, chatLog, inputLabel, inputField, sendButton);
        vbox.setPadding(new Insets(8));
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 640, 480);
        stage.setTitle(title);
        stage.setScene(scene);

        scene.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        scene.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        stage.show();
    }

    /**
     * Add message to chat log
     * @param msg the message to add
     */
    public void addMessage(String msg) {
        javafx.application.Platform.runLater(() -> {
            chatLog.appendText(msg + "\n\n");
        });
    }

    /**
     * Enable chat
     */
    public void enableChat(){
        enabled = true;
    }

    /**
     * Disable chat
     */
    public void disableChat(){
        enabled = false;
    }

    /**
     * Close chat window
     */
    public void closeChat(){
        stage.close();
    }

    /**
     * Send message to server
     */
    private void sendMessage() {
        String message = removeLineBreaks(inputField.getText().strip());
        if (message.length() > 0) {
            message = message.replace(";", ",");
            chatControl.sendMessage(message);
            inputField.setText("");
        }
    }

    /**
     * Remove line breaks from string
     * @param str the string to remove line breaks from
     * @return the string without line breaks
     */
    private String removeLineBreaks(String str) {
        // see: https://stackoverflow.com/questions/2163045/how-to-remove-line-breaks-from-a-file-in-java
        return str.replaceAll("\\R+", " ");
    }
}
