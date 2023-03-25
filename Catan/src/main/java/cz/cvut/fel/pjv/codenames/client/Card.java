package cz.cvut.fel.pjv.codenames.client;

public class Card {
    private enum color{ RED, BLUE }
    private final String name;
    private final Card.color color;
    public Card(String name, color c) {
        this.name = name;
        this.color = c;
    }

    public String getName() {
        return name;
    }
}
