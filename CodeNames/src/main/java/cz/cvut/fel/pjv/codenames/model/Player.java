package cz.cvut.fel.pjv.codenames.model;

public class Player {

    private String ID;
    private PlayerTeam team;
    private PlayerRole role;

    public Player(String ID)    {
        this.ID = ID;
        this.team = PlayerTeam.NONE;
        this.role = PlayerRole.NONE;
    }
    public enum PlayerTeam{
        RED,
        BLUE,
        NONE
    }

    public enum PlayerRole{
        SPY_MASTER,
        FIELD_OPERATIVE,
        FIELD_OPERATIVE_LEADER,
        NONE
    }

    public PlayerTeam getTeam() {
        return team;
    }

    public PlayerRole getRole() {
        return role;
    }

    public void setTeam(PlayerTeam team) {
        this.team = team;
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }
}
