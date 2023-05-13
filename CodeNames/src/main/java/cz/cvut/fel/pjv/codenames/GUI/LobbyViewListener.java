package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.model.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LobbyViewListener implements Runnable {

    private Socket socket;

    private boolean running;
    LobbyView lobbyView;

    private Client client;
    public LobbyViewListener(LobbyView lobbyView, Client client){
        this.client = client;
        this.lobbyView = lobbyView;
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("Listener started");
        try {
            Socket socket = new Socket("localhost", 1313);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("listen;" + client.getSessionId().toString() + ";" + client.getId() + ";");
            System.out.println(reader.readLine());

            String message;
            while (running) {
                if ((message = reader.readLine()) != null) {
                    System.out.println("got message: " + message);
                    AnswerParser answerParser = new AnswerParser(message);
                    if (answerParser.getAnswer() == AnswerParser.AnswerType.UPDATE) {
                        lobbyView.update();
                    }
                }
            }

            socket.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void stop(){
        running = false;
    }
    private void update() {
        // Implement your update logic here
        System.out.println("Update function called");
    }
}
