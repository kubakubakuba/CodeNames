package cz.cvut.fel.pjv.codenames.model;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

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
            System.out.println("Game data is null");
        }
    }

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
