package cz.cvut.fel.pjv.codenames.model;

public class Card {
    private enum color{ RED, BLUE, WHITE, BLACK }
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
