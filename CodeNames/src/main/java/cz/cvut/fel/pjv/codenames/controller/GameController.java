package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.GUI.GameView;
import cz.cvut.fel.pjv.codenames.model.*;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;

public class GameController {
    private Client localClient;

    //for all (tracking revealed cards)
    private ArrayList<ArrayList<Key.KeyType>> revealedCardsBoard;

    private Key gamekey;

    private Deck gameDeck;

    private String currentTurnText;

    private String currentPromptText;
    private int currentPromptCardCount;

    public ArrayList<ArrayList<Key.KeyType>> getRevealedCardsBoard() {return revealedCardsBoard;}
    public String getCurrentTurnText() {return currentTurnText;}
    public String getCurrentPromptText() {return currentPromptText;}
    public int getCurrentPromptCardCount() {return currentPromptCardCount;}

    private GameView gameView;
    private String serverIP = "localhost";
    private int serverPort = 1313;

    private ChatController chatController;

    private GameListener gameListen;

    public Client getClient() {
        return localClient;
    }

    public GameController(Client client, ChatController chatController){
        this.localClient = client;
        serverIP = localClient.getServerIP();
        serverPort = localClient.getServerPort();
        revealedCardsBoard = initRevealedCards();
    }

    public void displayGameWindow(){
        gameView.start(new Stage());

        System.out.println("starting game listener");
        Thread gameListenerThread = new Thread(gameListen);

        gameListenerThread.start();
    }

    public Key getKey() {return gamekey;}
    public Deck getDeck() {return gameDeck;}

    public ArrayList<ArrayList<Key.KeyType>> initRevealedCards(){
        ArrayList<ArrayList<Key.KeyType>> noReveals = new ArrayList<ArrayList<Key.KeyType>>();
        for (int r = 0; r < 5; r++ ){
            ArrayList<Key.KeyType> row = new ArrayList<Key.KeyType>();
            for (int c = 0; c < 5; c++){
                row.add(Key.KeyType.EMPTY);
            }
            noReveals.add(row);
        }
        return noReveals;
    }
    //commands for spymaster:
    //gets serialized key and Deck, using a DEck and key constructor
    //loads data into instances and passes to gameKey and gameDeck
    public void getSessionKey(){}

    //checks if number of Cards doesn't exceed 8
    public boolean commitPrompt(){return true;}

    //commands for field operative leader:

    //checks if Card hasn't been chosen already
    public boolean makeChoice(String chosenName){return true;}

    //commands for all:
    public void disconnect(){}
    //once someone disconnects it should save the game and close the game for all
    //-> will be implemented in gameListener

    public void getSessionDeck(){}
    public void saveGame(){}
        //TODO: implement getCurrentTurn loads teamcolor and role of current turn to currentTurnText

    public void update(GameData data) {
        //init variable object from data
        String team = (data.getCurrentTurnTeam() == Player.PlayerTeam.RED) ? "RED" : "BLUE";
        String role = (data.getCurrentTurnRole() == Player.PlayerRole.SPY_MASTER) ? "SPYMASTER" : "FIELD OPERATIVES";
        this.currentTurnText = team + " " + role;
        this.gameDeck = data.getBoard().getDeck();
        this.gamekey = data.getBoard().getKey();
        this.revealedCardsBoard = data.getRevealedCardsBoard();
        this.currentPromptText = data.getLastPromptText();
        this.currentPromptCardCount = data.getLastPromptCardCount();
    }

}


}
