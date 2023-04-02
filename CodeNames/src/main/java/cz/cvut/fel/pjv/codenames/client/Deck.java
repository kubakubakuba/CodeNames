package cz.cvut.fel.pjv.codenames.client;

import java.io.File;
import java.util.Queue;

public class Deck {
    private Queue<Card> deck;

    public Deck(File cards) {
        //add cards into temp array, shuffle them and push them into queue
        //add cards into deck from config file
    }

    public Card getCard(){
        return deck.poll(); //popping the first element
    }

    public void addCard(Card card){
        deck.add(card);
    }
}
