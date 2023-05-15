package cz.cvut.fel.pjv.codenames.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {
    private Board board;
    private Player[] players;
    private String current_prompt;
    private int prompt_num_cards;
    private HashMap<String, Player> listOfPlayers;
    public HashMap<String,Player> getListOfPlayers() {return listOfPlayers;};
    public void saveGame(){};
    public void loadGame(){};

    public Game(HashMap<String,Player> listOfPlayers) {
        board = new Board();
        this.listOfPlayers = listOfPlayers;
    }
    private void loadLoadedGame(){

    }

    private void loadLoadedDeck(){

    }


}
