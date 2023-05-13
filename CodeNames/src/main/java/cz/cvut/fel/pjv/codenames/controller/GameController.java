package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.model.Client;

public class GameController {
    private Client localClient;
    

    public Client getClient() {
        return localClient;
    }

    public GameController(Client client) {
        this.localClient = client;

    }

}
