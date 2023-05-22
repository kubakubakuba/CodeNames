package cz.cvut.fel.pjv.codenames.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameDataTest {

    private ArrayList<String> words = new ArrayList<>(Arrays.asList(
            "word1", "word2", "word3", "word4", "word5",
            "word6", "word7", "word8", "word9", "word10",
            "word11", "word12", "word13", "word14", "word15",
            "word16", "word17", "word18", "word19", "word20",
            "word21", "word22", "word23", "word24", "word25"
    ));
    private ArrayList<ArrayList<Key.KeyType>> result;
    GameData gD;

    @BeforeEach
    public void setUp() {
        gD = new GameData(new Board(words));
        result = gD.initRevealedCards();
    }


    @Test
    void revealCardInRevealedCards() {
        int x = 4;
        int y = 4;
        gD.revealCardInRevealedCards(x,y, Key.KeyType.BLUE);
        assertEquals(Key.KeyType.BLUE, gD.getRevealedCardsBoard().get(x).get(y));
    }

    @Test
    void initRevealedCards() {
        assertEquals(5, result.size());

        for (ArrayList<Key.KeyType> row : result) {
            assertEquals(5, row.size());
        }

        for (ArrayList<Key.KeyType> row : result) {
            for (Key.KeyType keyType : row) {
                assertEquals(Key.KeyType.EMPTY, keyType);
            }
        }
    }
}