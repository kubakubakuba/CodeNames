package cz.cvut.fel.pjv.codenames.model;

public class FieldOperative extends Player  {
    public FieldOperative(PlayerTeam team) {
        setRole(PlayerRole.FIELD_OPERATIVE);
        setTeam(team);
    }
}
