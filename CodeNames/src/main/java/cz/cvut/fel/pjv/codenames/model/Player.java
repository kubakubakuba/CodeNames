package cz.cvut.fel.pjv.codenames.model;

public class Player {

    private String ID;

    public Player(String ID)    {
        this.ID = ID;
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

    private PlayerTeam team = PlayerTeam.NONE;
    private PlayerRole role = PlayerRole.NONE;

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
