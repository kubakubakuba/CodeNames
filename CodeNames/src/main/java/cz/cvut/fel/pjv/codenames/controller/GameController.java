package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.model.Client;
import cz.cvut.fel.pjv.codenames.model.Deck;
import cz.cvut.fel.pjv.codenames.model.Key;

import java.util.ArrayList;

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


    private String serverIP = "localhost";
    private int serverPort = 1313;
    

    public Client getClient() {
        return localClient;
    }

    public GameController(Client client) {
        this.localClient = client;
        serverIP = localClient.getServerIP();
        serverPort = localClient.getServerPort();
        revealedCardsBoard = initRevealedCards();
    }

    //only for spymaster
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

    //checks if numberofCards dosen't exceed 8
    public boolean commitPrompt(){return true;}

    //commands for fieldoperative leader:

    //checks if Card hasnt been chosen already
    public boolean makeChoice(String chosenName){return true;}

    //commands for all:
    public void disconnect(){}
    //once someone disconnects it should save the game and close the game for all
    //-> will be implemented in gameListener

    public void getSessionDeck(){}
    public void saveGame(){}
    public void getCurrentTurn(){
        //Todo implement getCurrentTurn loads teamcolor and role of current turn to currentTurnText
    }
    public void getCurrentPrompt(){} //gets prompt string and prompt cardcount to CurrentPrompt and Currentpromptcardcount


}
