package cz.cvut.fel.pjv.codenames.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Game {

    private HashMap<String, Player> listOfPlayers;
    public HashMap<String,Player> getListOfPlayers() {return listOfPlayers;};

    private GameData gameData;

    public void saveGame(){};
    public void loadGame(){};

    public Game(HashMap<String,Player> listOfPlayers, String dckFile) {
        Board board = new Board(dckFile);
        this.gameData = new GameData(board);
        this.listOfPlayers = listOfPlayers;
    }
    private void loadLoadedGame(){

    }

    private void loadLoadedDeck(){

    }

    public GameData getGameData(){
        return this.gameData;
    }

}
