package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Lobby;
import cz.cvut.fel.pjv.codenames.model.Game;

import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {
    private Lobby lobby;

    private String hostId;
    private UUID sessionId;

    private Game game;

    private ArrayList<Socket> listeners;
    public Lobby getLobby() {
        return lobby;
    }
    public Game getGame() {return game;}

    public String getHostId() {
        return hostId;
    }
    public UUID getSessionId(){return sessionId;}

    public Session(String host){
        lobby = new Lobby();
        hostId = host;
        sessionId = UUID.randomUUID();
        listeners = new ArrayList<Socket>();
    }

    public boolean connectToSession(String guest)   {
        lobby.getListOfIds().add(guest);
        return true;
    }

    public boolean disconnectFromSession(String guest)   {
        lobby.getListOfIds().remove(guest);
        return true;
    }

    public ArrayList<Socket> getListeners() {
        return this.listeners;
    }

    public void addListener(Socket listener) {
        this.listeners.add(listener);
    }
}
