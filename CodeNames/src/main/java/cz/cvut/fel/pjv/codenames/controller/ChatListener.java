package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.GUI.LobbyView;
import cz.cvut.fel.pjv.codenames.model.Client;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatListener implements Runnable {

    private boolean running;
    private Client client;
    private ChatController chatControl;

    /**
     * Constructor
     * @param chatControl the chat controller
     * @param client the client
     */
    public ChatListener(ChatController chatControl, Client client){
        this.chatControl = chatControl;
        this.client = client;
        this.running = true;
    }

    /**
     * Stops the thread
     */
    @Override
    public void run() {
        System.out.println("Listener started");
        try {
            Socket socket = new Socket(client.getServerIP(), client.getServerPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("listenchat;" + client.getSessionId().toString() + ";" + client.getId() + ";");
            System.out.println(reader.readLine()); //TODO: check for server answer

            String message;
            while (running) {
                if ((message = reader.readLine()) != null) {
                    System.out.println("got message: " + message);
                    AnswerParser answerParser = new AnswerParser(message);
                    if (answerParser.getAnswer() == AnswerParser.AnswerType.MESSAGE){
                        chatControl.addMessage(answerParser.getArguments()[0]);
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.CHAT_ENABLE){
                        chatControl.setChatEnable();
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.CHAT_DISABLE){
                        chatControl.setChatDisable();
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.END_LISTEN){
                        stop();
                    }
                }
            }

            System.out.println("Listener stopped");
            socket.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop(){
        running = false;
    }
}
