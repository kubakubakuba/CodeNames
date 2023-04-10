package cz.cvut.fel.pjv.codenames.model;

public class Board {

    private Card[] board;

    public Card[] getBoard() {
        return board;
    }

    public Board() {
        constructBoard();
    }

    private Card[] constructBoard(){
        //shuffle deck
        return new Card[25]; //get 25 top cards of deck
    }



}
