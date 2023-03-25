package cz.cvut.fel.pjv.codenames.client;

public class Board {
    private Card[] board;

    public Card[] getBoard() {
        return board;
    }

    public Board() {
        //shuffle cards
        //get 25 of them and make a board
    }

    private Card[] constructBoard(){
        //shuffle deck
        return new Card[25]; //get 25 top cards of deck
    }


}
