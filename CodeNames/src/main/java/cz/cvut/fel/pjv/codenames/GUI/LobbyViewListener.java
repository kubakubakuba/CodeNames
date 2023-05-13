package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.server.AnswerParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class LobbyViewListener implements Runnable {

    private Socket socket;

    private boolean running;
    LobbyView lobbyView;
    public LobbyViewListener(Socket socket, LobbyView lobbyView){
        this.socket = socket;
        this.lobbyView = lobbyView;
    }

    @Override
    public void run() {
        System.out.println("Listener started");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            System.out.println("Listener running");
            while (running) {
                if ((message = reader.readLine()) != null) {
                    System.out.println("got message: " + message);
                    AnswerParser answerParser = new AnswerParser(message);
                    if (answerParser.getAnswer() == AnswerParser.AnswerType.UPDATE) {
                        lobbyView.update();
                    }
                }
            }

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
