package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.model.Lobby;
import cz.cvut.fel.pjv.codenames.model.Player;
import cz.cvut.fel.pjv.codenames.server.Client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LobbyController {

    private Lobby lobby;
    private List<Client> connectedClients = new ArrayList<Client>();



    public LobbyController()    {
        this.lobby = new Lobby();
    }


    //implement start client and start server
    public void createServerSession(String id)   {

    }
    public void createClient(String id)  {
        Client client = new Client(id);
        connectedClients.add(client);
    }

    public Player getPlayer(String ID)  {
        for(Client client : connectedClients)  {
            if (ID.equals(client.getId()))  {
                return client.getPlayer();
            }
        }
        return null;
    }

    //implement feedback from gui to server and model
    public int getNumPLayers()    {
        return lobby.getNumOfPlayers();
    }
    public int getNumRedPlayers()   {
        return lobby.getNumOfRED();
    }
    public int getNumBluePlayers()   {
        return lobby.getNumOfBLUE();
    }

    public String getHostId(){
        return lobby.getHostId();
    }
    public void setHostId(String id){
        lobby.setHostId(id);
    }

    public  void updateNumPlayers(String command, String ID)  {
        switch (command) {
            case "chooseRed":
                getPlayer(ID).setTeam(Player.PlayerTeam.RED);
                updateCommonStats();
                break;
            case "chooseBlue":
                getPlayer(ID).setTeam(Player.PlayerTeam.BLUE);
                updateCommonStats();
                break;
        }
    }

    public boolean validChoice()  {

        return true;
    }

    public void updateLocalStats(String id){
        for(Client client : connectedClients)  {
            if (id.equals(client.getId()))  {
                Player p = client.getPlayer();

            }
        }
    }
    public void updateCommonStats(){
        int Totp = 0;
        int redp = 0;
        int bluep = 0;
        for(Client client : connectedClients)  {
            Player p = client.getPlayer();
            Totp++;
            if (p.getTeam().equals(Player.PlayerTeam.RED))
                redp++;
            if (p.getTeam().equals(Player.PlayerTeam.BLUE))
                bluep++;
        }
        lobby.setNumOfPlayers(Totp);
        lobby.setNumOfRED(redp);
        lobby.setNumOfBLUE(bluep);
    }


    //implement feedback from server to model

}
