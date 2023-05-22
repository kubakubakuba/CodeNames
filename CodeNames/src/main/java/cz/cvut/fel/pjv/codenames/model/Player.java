package cz.cvut.fel.pjv.codenames.model;

import java.net.Socket;

public class Player {

    private String ID;
    private PlayerTeam team;
    private PlayerRole role;
    private Socket socket;


    public Player(String ID){
        this.ID = ID;
        this.team = PlayerTeam.NONE;
        this.role = PlayerRole.NONE;
    }

    public Player(String ID, Socket socket){
        this.ID = ID;
        this.team = PlayerTeam.NONE;
        this.role = PlayerRole.NONE;
        this.socket = socket;
    }

    /**
     * Enum for player team
     */
    public enum PlayerTeam{
        RED,
        BLUE,
        NONE
    }

    /**
     * Enum for player role
     */
    public enum PlayerRole{
        SPY_MASTER,
        FIELD_OPERATIVE,
        FIELD_OPERATIVE_LEADER,
        NONE
    }

    /**
     * Getter for team
     * @return team
     */
    public PlayerTeam getTeam() {
        return team;
    }

    /**
     * Getter for role
     * @return role
     */

    public PlayerRole getRole() {
        return role;
    }

    /**
     * Getter for socket
     * @return socket
     */

    public Socket getSocket() { return socket; }

    /**
     * Getter for ID
     * @return ID
     */

    public String getID() {
        return ID;
    }

    /**
     * Setter for team
     * @param team team
     */
    public void setTeam(PlayerTeam team) {
        this.team = team;
    }

    /**
     * Setter for role
     * @param role role
     */
    public void setRole(PlayerRole role) {
        this.role = role;
    }


}
