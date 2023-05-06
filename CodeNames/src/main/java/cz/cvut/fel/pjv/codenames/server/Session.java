package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Lobby;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private Lobby lobby;
    private String hostId;
    private List<Client> connectedClients = new ArrayList<Client>();

    public Session(Client host){
        lobby = new Lobby();
        connectedClients.add(host);
        hostId = host.getId();
        lobby.setHostId(hostId);
        lobby.getListOfIds().add(host.getId());
    }

    public boolean connectToSession(Client guest)   {
        connectedClients.add(guest);
        lobby.getListOfIds().add(guest.getId());

        return true;
    }

}
