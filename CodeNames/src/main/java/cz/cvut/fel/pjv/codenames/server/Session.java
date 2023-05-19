package cz.cvut.fel.pjv.codenames.server;

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

    public Session(String host){
        lobby = new Lobby();
        hostId = host;
        sessionId = UUID.randomUUID();
        listeners = new HashMap<String, Socket>();
        chatListeners = new HashMap<String, Socket>();
        gameListeners = new HashMap<String, Socket>();
    }

    public void startNewGame(String dckFile){
        game = new Game(lobby.getListOfPlayers(), dckFile);
    }
    public void startGameWDeck(){
        //game = new Game(Buffer loadedDeck)
        //or probably decode the deck to a ArrayList<String>
    }
    public void startLoadedGame(){
        //game = new Game(Buffer loadedGame)
    }
    public void loadGame(){
        //write serialized game data t a buffer in session for later passing to startLoadedGame
    }

    public HashMap<String, Socket> getListeners() {
        return this.listeners;
    }

    public HashMap<String, Socket> getChatListeners() {
        return this.chatListeners;
    }

    public HashMap<String, Socket> getGameListeners() {
        return this.gameListeners;
    }

    public void addListener(Socket listener, String id) {
        this.listeners.put(id, listener);
    }

    public void addChatListener(Socket listener, String id) {
        this.chatListeners.put(id, listener);
    }

    public void addGameListener(Socket listener, String id) {
        this.gameListeners.put(id, listener);
    }
}
