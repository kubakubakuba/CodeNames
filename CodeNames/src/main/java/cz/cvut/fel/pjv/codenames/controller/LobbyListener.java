package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.GUI.GameView;
import cz.cvut.fel.pjv.codenames.GUI.LobbyView;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.model.Client;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LobbyListener implements Runnable {

    private Socket socket;

    private boolean running;
    LobbyView lobbyView;

    private Client client;

    public LobbyListener(LobbyView lobbyView, Client client){
        this.client = client;
        this.lobbyView = lobbyView;
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("Listener started");
        try {
            Socket socket = new Socket(client.getServerIP(), client.getServerPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("listen;" + client.getSessionId().toString() + ";" + client.getId() + ";");
            System.out.println(reader.readLine());  //TODO: check for server answer

            String message;
            while (running) {
                if ((message = reader.readLine()) != null) {
                    System.out.println("got message: " + message);
                    AnswerParser answerParser = new AnswerParser(message);
                    if (answerParser.getAnswer() == AnswerParser.AnswerType.UPDATE) {
                        lobbyView.update();
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.END_LISTEN){
                        stop();
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.START_GAME){
                        lobbyView.startGame();
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
