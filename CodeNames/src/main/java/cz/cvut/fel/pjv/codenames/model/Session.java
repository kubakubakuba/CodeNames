package cz.cvut.fel.pjv.codenames.model;

import cz.cvut.fel.pjv.codenames.model.Lobby;
import cz.cvut.fel.pjv.codenames.model.Game;

import java.lang.reflect.Array;
import java.net.Socket;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Session {
    private Lobby lobby;

    private String hostId;
    private UUID sessionId;

    private Game game;
    private Buffer loadedGame;
    private Buffer loadedCards;

    private HashMap<String, Socket> listeners;
    private HashMap<String, Socket> chatListeners;

    private HashMap<String, Socket> gameListeners;
    public Lobby getLobby() {
        return lobby;
    }
    public Game getGame() {return game;}

    public String getHostId() {
        return hostId;
    }
    public UUID getSessionId(){return sessionId;}
    public HashMap<String, Socket> getListeners() {
        return this.listeners;
    }
    public HashMap<String, Socket> getChatListeners() {
        return this.chatListeners;
    }
    public HashMap<String, Socket> getGameListeners() {
        return this.gameListeners;
    }

    public Session(String host){
        lobby = new Lobby();
        hostId = host;
        sessionId = UUID.randomUUID();
        listeners = new HashMap<String, Socket>();
        chatListeners = new HashMap<String, Socket>();
        gameListeners = new HashMap<String, Socket>();
    }

    /**
     * Starts a new game with the current lobby
     * @param dckFile - path to the deck file
     */
    public void startNewGame(ArrayList<String> words){
        game = new Game(lobby.getListOfPlayers(), words);
    }

    /**
     * Loads a game from a string in base64 format
     * @param gameData - data string in base64 format
     */
    public void loadGame(String gameData){
        game = new Game(lobby.getListOfPlayers());
        game.loadGame(gameData);
    }

    /**
     * Adds a listener to the session
     * @param listener - socket to be added
     * @param id - id of the socket
     */
    public void addListener(Socket listener, String id) {
        this.listeners.put(id, listener);
    }

    /**
     * Adds a chat listener to the session
     * @param listener - socket to be added
     * @param id - id of the socket
     */
    public void addChatListener(Socket listener, String id) {
        this.chatListeners.put(id, listener);
    }

    /**
     * Adds a game listener to the session
     * @param listener - socket to be added
     * @param id - id of the socket
     */
    public void addGameListener(Socket listener, String id) {
        this.gameListeners.put(id, listener);
    }

}
