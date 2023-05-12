package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Lobby;
import cz.cvut.fel.pjv.codenames.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {
    private Lobby lobby;



    private String hostId;
    private UUID sessionId;

    private List<String> connectedIds = new ArrayList<String>();
    private Game game;

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
        //connectedIds.add(host);
        hostId = host;
        lobby.setHostId(hostId);
        sessionId = UUID.randomUUID();
        // lobby.getListOfIds().add(host);
    }

    public boolean connectToSession(String guest)   {
        connectedIds.add(guest);
        lobby.getListOfIds().add(guest);
        return true;
    }

    public boolean disconnectFromSession(String guest)   {
        connectedIds.remove(guest);
        lobby.getListOfIds().remove(guest);
        return true;
    }
}