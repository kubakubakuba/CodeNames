package cz.cvut.fel.pjv.codenames.model;

public class Player {



    enum PlayerTeam{
        RED,
        BLUE,
        NONE
    }

    enum PlayerRole{
        SPY_MASTER,
        FIELD_OPERATIVE,
        FIELD_OPERATIVE_LEADER,
        NONE
    }

    private PlayerTeam team = PlayerTeam.NONE;
    private PlayerRole role = PlayerRole.NONE;

    private String ID;

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
