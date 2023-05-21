package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.GUI.ChatView;
import cz.cvut.fel.pjv.codenames.model.Client;
import cz.cvut.fel.pjv.codenames.model.Player;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class ChatController {
    private ChatView chatView;

    private Client client;

    private ChatListener chatListen;

    public ChatController(Client client){
        this.client = client;
        this.chatView = new ChatView(this, client);
        this.chatListen = new ChatListener(this, client);
    }

    /**
     * Displays chat window and starts chat listener thread
     */
    public void displayChatWindow(){
        chatView.start(new Stage());

        Thread chatListenerThread = new Thread(this.chatListen);

        chatListenerThread.start();
    }

    /**
     * Disables chat window
     */
    public void setChatDisable(){
        chatView.disableChat();
    }

    /**
     * Enables chat window
     */
    public void setChatEnable(){
        chatView.enableChat();
    }

    /**
     * Sends message to server
     * @param message message to be sent
     */
    public void sendMessage(String message){
        String answer = sendMessageCommand(message);
        AnswerParser answerParser = new AnswerParser(answer);
        if(answerParser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || !answerParser.getArguments()[0].equals("true")){
            System.err.println("Error sending message");
        }
    }

    /**
     * Adds message to chat window
     * @param message message to be added
     */
    public void addMessage(String message){
        chatView.addMessage(message);
    }

    /**
     * Stops chat listener thread and closes chat window
     */
    public void closeChat(){
        chatListen.stop();
        chatView.closeChat();
    }

    /**
     * Sends message command to server
     * @param message message to be sent
     * @return server answer string
     */
    private String sendMessageCommand(String message) {
        String serverAnswer = "1arg;null";

        try {
            Socket sock = new Socket(client.getServerIP(), client.getServerPort());
            OutputStream output = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("message;" + client.getId() + ";" + client.getSessionId().toString() + ";" + client.getId() + ": " + message.replace(";", ","));

            InputStream input = sock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            serverAnswer = reader.readLine();
            return serverAnswer;

        } catch (Exception e) {
            System.out.println(e);
        }

        return serverAnswer;
    }
}
