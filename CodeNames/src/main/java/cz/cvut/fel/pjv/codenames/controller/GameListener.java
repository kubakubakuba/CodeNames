package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.GUI.GameView;
import cz.cvut.fel.pjv.codenames.model.Client;
import cz.cvut.fel.pjv.codenames.model.GameData;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class GameListener implements Runnable {

    private boolean running;

    private GameController gameControl;

    private GameView gameView;

    public GameListener(GameView view, GameController gameControl){
        this.gameControl = gameControl;
        this.gameView = view;
        this.running = true;
    }

    @Override
    public void run() {
        System.out.println("Game listener started");
        try {
            Socket socket = new Socket(gameControl.getClient().getServerIP(), gameControl.getClient().getServerPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.println("listengame;" + gameControl.getClient().getSessionId().toString() + ";" + gameControl.getClient().getId() + ";");
            System.out.println(reader.readLine());

            //gameControl.getGameData();

            String message;
            while (running) {
                if ((message = reader.readLine()) != null) {
                    System.out.println("got message: " + message);
                    AnswerParser answerParser = new AnswerParser(message);

                    if (answerParser.getAnswer() == AnswerParser.AnswerType.UPDATE){
                        gameView.update();
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.END_LISTEN){
                        stop();
                    }
                    if(answerParser.getAnswer() == AnswerParser.AnswerType.END){
                        gameView.update();
                        gameView.gameEnd();
                        stop();
                    }
                }
            }

            System.out.println("Game listener stopped");
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
