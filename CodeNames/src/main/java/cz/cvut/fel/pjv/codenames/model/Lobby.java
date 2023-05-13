package cz.cvut.fel.pjv.codenames.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lobby {

    private int NumOfPlayers = 0;
    private int NumOfRED = 0;
    private int NumOfBLUE = 0;

    private List<String> listOfIds = new ArrayList<String>();
    private HashMap<String, Player> listOfPlayers = new HashMap<String, Player>();
    private String hostId;

    public String getHostId() {return hostId;}
    public List<String> getListOfIds() {
        return listOfIds;
    }
    public void setHostId(String hostId) {this.hostId = hostId;}

    public int getNumOfPlayers() {
        return NumOfPlayers;
    }

    public int getNumOfRED() {
        return NumOfRED;
    }

    public int getNumOfBLUE() {
        return NumOfBLUE;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        NumOfPlayers = numOfPlayers;
    }

    public void setNumOfRED(int numOfRED) {
        NumOfRED = numOfRED;
    }

    public void setNumOfBLUE(int numOfBLUE) {NumOfBLUE = numOfBLUE;}

    public HashMap<String, Player> getListOfPlayers() {return listOfPlayers;}
}
