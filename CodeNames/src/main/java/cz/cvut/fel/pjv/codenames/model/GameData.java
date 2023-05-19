package cz.cvut.fel.pjv.codenames.model;

import java.util.Random;

public class GameData implements java.io.Serializable{
    private Board board; //board
    private ArrayList<ArrayList<Key.KeyType>> revealedCardsBoard; //which spaces were already guessed
    private String lastPromptText;
    private int lastPromptCardCount;
    private Player.PlayerTeam currentTurnTeam;
    private Player.PlayerRole currentTurnRole;

    public GameData(Board board){
        this.board = board;
        this.round = new Random().nextInt(2);
        this.guessedSpaceIndexes = new int[25];
        this.lastPrompt = "";
    }

    public GameData(String game_file){
        //load game from file
    }

    public Board getBoard() {return board;}

    public void revealCardInRevealedCards(int x, int y, Key.KeyType type){ revealedCardsBoard.get(x).set(y, type);}

    public ArrayList<ArrayList<Key.KeyType>> getRevealedCardsBoard() {return revealedCardsBoard;}

    public ArrayList<ArrayList<Key.KeyType>> initRevealedCards(){
        ArrayList<ArrayList<Key.KeyType>> noReveals = new ArrayList<ArrayList<Key.KeyType>>();
        for (int r = 0; r < 5; r++ ){
            ArrayList<Key.KeyType> row = new ArrayList<Key.KeyType>();
            for (int c = 0; c < 5; c++){
                row.add(Key.KeyType.EMPTY);
            }
            noReveals.add(row);
        }
        return noReveals;
    }

    public String getLastPromptText() {
        return lastPromptText;
    }
    public int getLastPromptCardCount() {
        return lastPromptCardCount;
    }

    public Player.PlayerTeam getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    public Player.PlayerRole getCurrentTurnRole() {
        return currentTurnRole;
    }

    public void setCurrentTurnTeam(Player.PlayerTeam currentTurnTeam) {
        this.currentTurnTeam = currentTurnTeam;
    }

    public void setCurrentTurnRole(Player.PlayerRole currentTurnRole) {
        this.currentTurnRole = currentTurnRole;
    }

    public void setLastPromptCardCount(int lastPromptCardCount) {
        this.lastPromptCardCount = lastPromptCardCount;
    }

    public void setLastPromptText(String lastPromptText) {
        this.lastPromptText = lastPromptText;
    }
}
