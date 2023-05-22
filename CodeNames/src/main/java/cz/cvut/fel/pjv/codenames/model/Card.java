package cz.cvut.fel.pjv.codenames.model;

import java.io.Serializable;

public class Card implements Serializable {
    private final String name;
    private Key.KeyType cardType;

    /**
     * Constructor
     * @param name name of the card
     */
    public Card(String name) {
        this.name = name;
    }

    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for card type
     * @return card type
     */
    public Key.KeyType getCardType() {return cardType;}

    /**
     * Setter for card type
     * @param cardType card type
     */
    public void setCardType(Key.KeyType cardType) {this.cardType = cardType;}

}
