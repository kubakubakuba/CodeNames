package cz.cvut.fel.pjv.codenames.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    Session s = new Session("localhost");

//    @BeforeAll
//    public void setUp(){
//        s.
//    }
    @Test
    void addListener() {
        s.addListener(new Socket(),"test");
        assertNotEquals(0, s.getListeners().size());
    }

    @Test
    void addChatListener() {
        s.addChatListener(new Socket(),"test");
        assertNotEquals(0, s.getChatListeners().size());
    }

    @Test
    void addGameListener() {
        s.addGameListener(new Socket(),"test");
        assertNotEquals(0, s.getGameListeners().size());
    }
}