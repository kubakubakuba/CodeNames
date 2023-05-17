package cz.cvut.fel.pjv.codenames.model;

import java.util.Random;

public class GameData implements java.io.Serializable{
    private Board board; //board
    private int round; //who plays now
    private int[] guessedSpaceIndexes; //which spaces were already guessed

    private String lastPrompt;

    public GameData(Board board){
        this.board = board;
        this.round = new Random().nextInt(2);
        this.guessedSpaceIndexes = new int[25];
        this.lastPrompt = "";
    }

    public GameData(String game_file){
        //load game from file
    }

}
