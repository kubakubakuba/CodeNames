package cz.cvut.fel.pjv.codenames.model;


import java.util.HashMap;

public class Game {
    private Board board;
    private Deck deck;
    private Key key;
    private Player[] players;
    private String current_prompt;
    private int prompt_num_cards;
    private HashMap<String, Player> listOfPlayers = new HashMap<String, Player>();

    public void setListOfPlayers(HashMap<String,Player> listOfPlayers) {this.listOfPlayers = listOfPlayers;};

    public HashMap<String,Player> getListOfPlayers() {return listOfPlayers;};

    public Game() {
        board = new Board();
        key = new Key();
    }
}
