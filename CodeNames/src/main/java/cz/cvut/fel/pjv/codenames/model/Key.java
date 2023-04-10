package cz.cvut.fel.pjv.codenames.model;

public class Key {
    enum KeyType{
        RED,
        BLUE,
        ASSASSIN,
        CIVILIAN
    }

    private KeyType[] solution;

    private KeyType[] test = {
            KeyType.RED, KeyType.RED, KeyType.RED, KeyType.RED, KeyType.RED,
            KeyType.RED, KeyType.RED, KeyType.RED, KeyType.RED, KeyType.BLUE,
            KeyType.BLUE, KeyType.BLUE, KeyType.BLUE, KeyType.BLUE, KeyType.BLUE,
            KeyType.BLUE, KeyType.BLUE, KeyType.ASSASSIN, KeyType.CIVILIAN, KeyType.CIVILIAN,
            KeyType.CIVILIAN, KeyType.CIVILIAN, KeyType.CIVILIAN, KeyType.CIVILIAN, KeyType.CIVILIAN
    };

    public Key()    {
        solution = test;
    }



    public KeyType[] getSolution() {
        return solution;
    }


}
