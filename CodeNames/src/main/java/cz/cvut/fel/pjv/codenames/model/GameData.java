package cz.cvut.fel.pjv.codenames.model;

import java.util.ArrayList;
import java.util.Random;

public class GameData implements java.io.Serializable{
    
    private Board board; //board
    private ArrayList<ArrayList<Key.KeyType>> revealedCardsBoard; //which spaces were already guessed
    private String lastPromptText;
    private int lastPromptCardCount;
    private Player.PlayerTeam currentTurnTeam;
    private Player.PlayerRole currentTurnRole;

    private boolean gameEnded;

    private Player.PlayerTeam winner;

    /**
     * Getter for winner
     * @return winner
     */
    public Player.PlayerTeam getWinner() {
        return winner;
    }

    /**
     * Setter for winner
     * @param winner winner
     */

    public void setWinner(Player.PlayerTeam winner) {
        this.winner = winner;
    }

    /**
     * Getter for current turn team
     * @return current turn team
     */

    public boolean hasGameEnded() {
        return gameEnded;
    }

    /**
     * Setter for game ended
     * @param gameEnded game ended
     */

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Constructor
     * @param board board
     */
    public GameData(Board board){
        this.board = board;
        currentTurnTeam = board.getStartingTeam();
        currentTurnRole = Player.PlayerRole.SPY_MASTER;
        this.revealedCardsBoard = initRevealedCards();
        this.lastPromptText = "";
        this.lastPromptCardCount = 0;
    }

    /**
     * Getter for board
     * @return board
     */
    public Board getBoard() {return board;}

    /**
     * Reveals a card in the revealed cards board
     * @param x x coordinate
     * @param y y coordinate
     * @param type type of the card
     */
    public void revealCardInRevealedCards(int x, int y, Key.KeyType type){ revealedCardsBoard.get(x).set(y, type);}

    public ArrayList<ArrayList<Key.KeyType>> getRevealedCardsBoard() {return revealedCardsBoard;}

    /**
     * Initializes the revealed cards board
     * @return revealed cards board
     */
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

    /**
     * Getter for last prompt text
     * @return last prompt text
     */
    public String getLastPromptText() {
        return lastPromptText;
    }

    /**
     * Getter for last prompt card count
     * @return last prompt card count
     */
    public int getLastPromptCardCount() {
        return lastPromptCardCount;
    }
    /**
     * Getter for current turn team
     */

    public Player.PlayerTeam getCurrentTurnTeam() {
        return currentTurnTeam;
    }

    /**
     * Getter for current turn role
     * @return current turn role
     */
    public Player.PlayerRole getCurrentTurnRole() {
        return currentTurnRole;
    }

    /**
     * Setter for current turn team
     * @param currentTurnTeam current turn team
     */
    public void setCurrentTurnTeam(Player.PlayerTeam currentTurnTeam) {
        this.currentTurnTeam = currentTurnTeam;
    }

    /**
     * Setter for current turn role
     * @param currentTurnRole current turn role
     */
    public void setCurrentTurnRole(Player.PlayerRole currentTurnRole) {
        this.currentTurnRole = currentTurnRole;
    }

    /**
     * Setter for last prompt card count
     * @param lastPromptCardCount last prompt card count
     */
    public void setLastPromptCardCount(int lastPromptCardCount) {
        this.lastPromptCardCount = lastPromptCardCount;
    }

    /**
     * Setter for last prompt text
     * @param lastPromptText last prompt text
     */
    public void setLastPromptText(String lastPromptText) {
        this.lastPromptText = lastPromptText;
    }
}
