package cz.cvut.fel.pjv.codenames.model;


public class Game {
    private Board board;
    private Deck deck;
    private Key key;
    private Player[] players;
    private String current_prompt;
    private int prompt_num_cards;



    public Game() {
        board = new Board();
        key = new Key();
    }
}
