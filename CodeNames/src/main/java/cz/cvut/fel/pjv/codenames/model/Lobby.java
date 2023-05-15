package cz.cvut.fel.pjv.codenames.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lobby {

    private List<String> listOfIds = new ArrayList<String>();
    private HashMap<String, Player> listOfPlayers = new HashMap<String, Player>();

    public List<String> getListOfIds() {
        return listOfIds;
    }
    public HashMap<String, Player> getListOfPlayers() {return listOfPlayers;}
}
