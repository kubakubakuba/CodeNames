package cz.cvut.fel.pjv.codenames.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private ArrayList<String> words = new ArrayList<>(Arrays.asList(
            "word1", "word2", "word3", "word4", "word5",
            "word6", "word7", "word8", "word9", "word10",
            "word11", "word12", "word13", "word14", "word15",
            "word16", "word17", "word18", "word19", "word20",
            "word21", "word22", "word23", "word24", "word25"
    ));

    @Test
    void transferKeyToCards() {
        Board b = new Board(words);
        b.transferKeyToCards(b.getKey(),b.getDeck());
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                assertEquals(b.getKey().getSolution().get(r).get(c), b.getDeck().getCards().get(r).get(c).getCardType());
            }
        }
    }

    @Test
    void initStartingTeam(){
        Board b = new Board(words);
        assertNotNull(b.initStartingTeam()); 
    }
}