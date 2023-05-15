package cz.cvut.fel.pjv.codenames.model;

public class Card {
    private final String name;
    private Key.KeyType cardType;
    public Card(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }
    public Key.KeyType getCardType() {return cardType;}
    public void setCardType(Key.KeyType cardType) {this.cardType = cardType;}

}
