package cz.cvut.fel.pjv.codenames.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Board implements Serializable {

    private Key key;
    private Deck deck;
    private Player.PlayerTeam startingTeam;

    /**
     * Constructor
     * @param words words to use in the game
     */
    public Board(ArrayList<String> words) {
        startingTeam = initStartingTeam();
        key = new Key(startingTeam);
        deck = new Deck(words);
        transferKeyToCards(key,deck);
    }

    /**
     * Transfers the key type from solution to cards
     * @param key key to transfer
     * @param deck deck to transfer to
     */
    void transferKeyToCards(Key key, Deck deck){
        ArrayList<ArrayList<Key.KeyType>> solution = key.getSolution();
        for(int r =0; r<5; r++){
            for(int c = 0; c<5; c++){
                deck.getCards().get(r).get(c).setCardType(solution.get(r).get(c));
            }
        }
    }

    /**
     * Initializes the starting team
     * @return starting team
     */
    public Player.PlayerTeam initStartingTeam(){
        int randIdx  = new Random().nextInt(2);
        Player.PlayerTeam [] teams= {Player.PlayerTeam.RED, Player.PlayerTeam.BLUE};
        return teams[randIdx];
    }

    /**
     * Getter for key
     * @return key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Getter for deck
     * @return deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Getter for starting team
     * @return starting team
     */
    public Player.PlayerTeam getStartingTeam() {
        return startingTeam;
    }
}
