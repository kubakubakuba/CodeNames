package cz.cvut.fel.pjv.codenames.model;


public class Game {
    private Board board;
    private Deck deck;
    private Key key;
    private Player[] players;
    private String CurrentPrompt;
    private int PromptNumOfCards;



    public Game() {
        board = new Board();
        key = new Key();
    }
}
