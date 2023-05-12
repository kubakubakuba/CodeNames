package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Lobby;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private Lobby lobby;
    private String hostId;
    private List<Client> connectedClients = new ArrayList<Client>();

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

}
