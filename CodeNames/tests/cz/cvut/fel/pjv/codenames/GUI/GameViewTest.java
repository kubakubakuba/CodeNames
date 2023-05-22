package cz.cvut.fel.pjv.codenames.GUI;

import cz.cvut.fel.pjv.codenames.controller.ChatController;
import cz.cvut.fel.pjv.codenames.controller.GameController;
import cz.cvut.fel.pjv.codenames.model.Client;
import cz.cvut.fel.pjv.codenames.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameViewTest {

    @Test
    void determineView() throws Exception {
        Client cl = new Client("test", "localhost", 1515);
        GameController c = new GameController(cl, new ChatController(cl));
        GameView gW = new GameView(c);
        gW.determineView(Player.PlayerRole.SPY_MASTER);

        assertNotNull (gW.getViewS(), "Expected SpymasterView instance to be created" );
        assertNull(gW.getViewF(), "Expected FieldOperativeView instance to be null");
        assertNull(gW.getViewL(), "Expected FieldOperativeLeaderView instance to be null");
    }

    @Test
    void gameEnd() {
        Client cl = new Client("test", "localhost", 1515);
        GameController c = new GameController(cl, new ChatController(cl));
        GameView gW = new GameView(c);
        gW.gameEnd();

    }
}