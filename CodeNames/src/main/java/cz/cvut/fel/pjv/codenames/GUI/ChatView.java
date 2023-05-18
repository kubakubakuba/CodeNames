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

public class ChatView extends Application {
    private Stage stage;
    private TextArea messages;
    private TextArea input;

    private boolean enabled = false;
    private ChatController chatControl;

    private Client localClient;

    public ChatView(ChatController chatControl, Client client) {
        this.chatControl = chatControl;
        this.localClient = client;
    }
    public void start(Stage stage) {
        this.stage = stage;
        //stage.setTitle("CodeNames Chat - " + localClient.getId());
        //stage.centerOnScreen();
        showChatWindow("CodeNames Chat - " + localClient.getId());
    }

    public void showChatWindow(String userName) {
        Label msgLabel = new Label("Chat log");
        messages = new TextArea();
        messages.setEditable(false);
        messages.setWrapText(true);
        VBox.setVgrow(messages, Priority.ALWAYS);
        Label inputLabel = new Label("Send message");
        input = new TextArea();
        input.setMaxHeight(28);
        input.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMessage();
            }
        });
        Button sendButton = new Button("Send");
        sendButton.setOnAction((ActionEvent e) -> {
            sendMessage();
        });
        VBox vbox = new VBox(4, msgLabel, messages, inputLabel, input, sendButton);
        vbox.setPadding(new Insets(8));
        vbox.setAlignment(Pos.CENTER);
        Scene chatScene = new Scene(vbox, 640, 480);
        stage.setTitle(userName);
        stage.setScene(chatScene);
        stage.show();
    }

    private void sendMessage() {
        String message = removeLineBreaks(input.getText().strip());
        if (message.length() > 0) {
            message = message.replace(";", ",");
            chatControl.sendMessage(message);
            input.setText("");
        }
    }

    private String removeLineBreaks(String str) {
        // see: https://stackoverflow.com/questions/2163045/how-to-remove-line-breaks-from-a-file-in-java
        return str.replaceAll("\\R+", " ");
    }

    public void addMessage(String msg) {
        messages.appendText(msg + "\n\n");
    }

    public void enableChat(){
        enabled = true;
    }

    public void disableChat(){
        enabled = false;
    }
}
