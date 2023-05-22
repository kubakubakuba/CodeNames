package cz.cvut.fel.pjv.codenames.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Lobby {

    private List<String> listOfIds = new ArrayList<String>();
    private HashMap<String, Player> listOfPlayers = new HashMap<String, Player>();

    /**
     * Constructor
     * @return list of ids
     */
    public List<String> getListOfIds() {
        return listOfIds;
    }

    /**
     * Getter for list of players
     * @return list of players
     */
    public HashMap<String, Player> getListOfPlayers() {return listOfPlayers;}
}
