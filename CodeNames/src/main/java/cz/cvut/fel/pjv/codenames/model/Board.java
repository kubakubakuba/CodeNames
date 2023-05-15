package cz.cvut.fel.pjv.codenames.model;

import java.util.ArrayList;
import java.util.Random;

public class Board {

    private ArrayList<ArrayList<Card>> Cards;
    private Key key;
    private Deck deck;
    private Player.PlayerTeam startingTeam;

    public Board() {
        startingTeam = initStartingTeam();
        key = new Key(startingTeam);
        deck = new Deck();
        transferKeyToCards(key,deck);
    }

    //load Board

    private void transferKeyToCards(Key key, Deck deck){
        ArrayList<ArrayList<Key.KeyType>> solution = key.getSolution();
        for(int r =0; r<5; r++){
            for(int c = 0; c<5; c++){
                deck.getCards().get(r).get(c).setCardType(solution.get(r).get(c));
            }
        }
    }
    private Player.PlayerTeam initStartingTeam()    {
        int randIdx  = new Random().nextInt(2);
        Player.PlayerTeam [] teams= {Player.PlayerTeam.RED, Player.PlayerTeam.BLUE};
        return teams[randIdx];
    }

}
