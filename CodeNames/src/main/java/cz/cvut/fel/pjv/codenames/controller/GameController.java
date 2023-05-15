package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.model.Client;
import cz.cvut.fel.pjv.codenames.model.Deck;
import cz.cvut.fel.pjv.codenames.model.Key;

public class GameController {
    private Client localClient;
    private Key gamekey;
    private Deck gameDeck;

    private String serverIP = "localhost";
    private int serverPort = 1313;
    

    public Client getClient() {
        return localClient;
    }

    public GameController(Client client) {
        this.localClient = client;
        serverIP = localClient.getServerIP();
        serverPort = localClient.getServerPort();
    }

    //only for spymaster
    public Key getKey() {return gamekey;}
    public Deck getDeck() {return gameDeck;}

    //commands for spymaster:
    //gets serialized key and Deck, using a DEck and key constructor
    //loads data into instances and passes to gameKey and gameDeck
    public void getSessionKey(){}
    public void getSessionDeck(){}

    //checks if numberofCards dosen't exceed 8
    public boolean commitPrompt(){return true;}

    //commands for fieldoperative leader:

    //checks if Card hasnt been chosen already
    public boolean makeChoice(){return true;}

    //commands for all:
    public void disconnect(){}
    //once someone disconects it should save the game and close the game for all
    //-> will be implemented in gameListener

    public void saveGame(){}

}
