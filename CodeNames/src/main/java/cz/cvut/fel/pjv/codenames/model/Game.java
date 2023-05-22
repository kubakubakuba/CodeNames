package cz.cvut.fel.pjv.codenames.model;


import cz.cvut.fel.pjv.codenames.server.ServerThread;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game {

    private HashMap<String, Player> listOfPlayers;

    /**
     * Getter for list of players
     * @return map list of players
     */
    public HashMap<String,Player> getListOfPlayers() {return listOfPlayers;};

    private GameData gameData;
    private final Logger LOGGER = Logger.getLogger(Game.class.getName());

    /**
     * Constructor
     * @param listOfPlayers list of players
     * @param words list of words
     */
    public Game(HashMap<String,Player> listOfPlayers, ArrayList<String> words) {
        Board board = new Board(words);
        this.gameData = new GameData(board);
        this.listOfPlayers = listOfPlayers;
    }

    /**
     * Constructor
     * @param listOfPlayers list of players
     */
    public Game(HashMap<String,Player> listOfPlayers) {
        this.listOfPlayers = listOfPlayers;
    }

    /**
     * Loads the game from the data string
     * @param gameData base64 encoded game data
     */
    public void loadGame(String gameData){
        byte[] decodedGameData = Base64.getDecoder().decode(gameData);
        GameData gameDataObject = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(decodedGameData);

            ObjectInputStream ois = new ObjectInputStream(bais);

            gameDataObject = (GameData)ois.readObject();

            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (gameData != null) {
            this.gameData = gameDataObject;
        }
        else{
            LOGGER.log(Level.SEVERE, "Game data is null");
        }
    }

    /**
     * Getter for game data
     * @return game data
     */
    public GameData getGameData(){
        return this.gameData;
    }

    /**
     * Returns the number of cards left to be revealed
     * @param color color of the cards
     * @return number of cards left to be revealed
     */
    public int getColorCardsLeft(Key.KeyType color){
        int ret = 0;
        for(ArrayList< Key.KeyType> array : gameData.getBoard().getKey().getSolution()){
            for(Key.KeyType keyType : array){
                if(keyType == color){
                    ret++;
                }
            }
        }
        for (ArrayList<Key.KeyType> array: gameData.getRevealedCardsBoard()) {
            for (Key.KeyType keyType: array) {
                if (keyType == color) {
                    ret--;
                }
            }
        }

        return ret;
    }


}
