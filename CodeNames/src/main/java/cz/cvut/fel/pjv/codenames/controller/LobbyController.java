package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.model.Player;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.model.Client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class LobbyController {

    //variables
    private Client localClient;
    private String hostId;
    private int[] RBNPlayers = {0, 0, 0};
    private int[] playerRoles = {0, 0, 0, 0, 0, 0, 0};
    private int playerCount = 0;
    private String serverIP = "localhost";
    private int serverPort = 1313;
    private ChatController chatController;
    private String deckFile = "Names.dck";
    private ArrayList<String> deck = new ArrayList<String>();
    private boolean customDeck = false;

    //getters
    public ChatController getChatController() {
        return chatController;
    }
    public void setChatController(ChatController chatController) {
        this.chatController = chatController;
    }
    public int getPlayerCount() {return playerCount;}
    public int[] getRBNPlayers() {return RBNPlayers;}
    public int[] getPlayerRoles() {return playerRoles;}
    public Client getLocalClient() {
        return localClient;
    }

    //constructor
    public LobbyController(String id, String serverIP, int serverPort){
        localClient = new Client(id, serverIP, serverPort);
    }

    /**
     * Set game deck file
     * @param deckFile path to deck file
     */
    public void setGameDeck(String deckFile){
        this.customDeck = true;
        this.deckFile = deckFile;
    }

    /**
     * Creates new session on server
     * @param hostId id of host
     * @return true if session was created, false otherwise
     */
    public int createServerSession(String hostId)   {
        //System.out.println("Write a command:");
        System.out.println("server: " + serverIP + serverPort);
        String serverAnswer = localClient.sendCommand("createSession;" + hostId + ';');
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() == AnswerParser.AnswerType.EMPTY){
            System.err.println("Server not running!");
            return 1;
        }

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("null") )){
            System.err.println("Received unexpected server answer!");
            return 2;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.setSessionId(parser.getArguments()[0]);
        }
        return 0;
    }

    /**
     * Connects to session on server
     * @param guestId id of guest
     * @param sessionId id of session
     * @return 0 if connected, 1 if unexpected server answer, 2 if name taken, 3 if session invalid
     */
    public int connectToSession(String guestId, String sessionId) {
        String serverAnswer = localClient.sendCommand("connect;" + guestId + ';' + sessionId + ';');
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG){
            System.err.println("Recieved unexpected server answer!");
            return 1;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("nametaken")){
            System.err.println("Someone with your name is already in session!");
            return 2;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("sessioninvalid")){
            System.err.println("Session does not exist!");
            return 3;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.setSessionId(parser.getArguments()[0]);
        }

        return 0;
    }

    /**
     * Get list of sessions from server
     * @return list of sessions
     */
    public ArrayList<String> getServerSessions(){
        String serverAnswer = localClient.sendCommand("getSessions;" + localClient.getId() + ';');
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.SESSION_LIST){
            System.err.println("Received unexpected server answer!");
            return null;
        }

        return new ArrayList<>(Arrays.asList(parser.getArguments()));
    }

    /**
     * Get list of players from server
     * @return list of players
     */
    public ArrayList<String> getIdList(){
        String serverAnswer = localClient.sendCommand("getlobbyids;" + localClient.getId() + ';' + localClient.getSessionId() + ";");
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ID_LIST){
            System.err.println("Received unexpected server answer!");
            return null;
        }
        return new ArrayList<>(Arrays.asList(parser.getArguments()));
    }

    /**
     * Get host id from server
     * @return host id
     */
    public String getHostId(){
        return hostId;
    }

    /**
     * Set host id
     */
    public void setHostId(){
        String answer = localClient.sendCommand("gethostid;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';');
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            hostId = parser.getArguments()[0];
        }
    }

    /**
     * Updates player count with server
     */
    public void updatePlayerCount()    {
        String answer = localClient.sendCommand("getplayercount;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';');
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() == AnswerParser.AnswerType.PLAYER_COUNT) {
            RBNPlayers[0] = Integer.parseInt(parser.getArguments()[0]);
            RBNPlayers[1] = Integer.parseInt(parser.getArguments()[1]);
            RBNPlayers[2] = Integer.parseInt(parser.getArguments()[2]);
            playerCount = RBNPlayers[0] + RBNPlayers[1] + RBNPlayers[2];
        }

    }

    /**
     * Updates player roles with server
     */
    public void updatePlayerRoles(){
        String answer = localClient.sendCommand("getplayerroles;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';');

        AnswerParser parser = new AnswerParser(answer);
        if(parser.getAnswer() == AnswerParser.AnswerType.PLAYER_COUNT_ROLES) {
            playerRoles[0] = Integer.parseInt(parser.getArguments()[0]);
            playerRoles[1] = Integer.parseInt(parser.getArguments()[1]);
            playerRoles[2] = Integer.parseInt(parser.getArguments()[2]);
            playerRoles[3] = Integer.parseInt(parser.getArguments()[3]);
            playerRoles[4] = Integer.parseInt(parser.getArguments()[4]);
            playerRoles[5] = Integer.parseInt(parser.getArguments()[5]);
            playerRoles[6] = Integer.parseInt(parser.getArguments()[6]);
        }
    }

    /**
     * Selects team for player
     * @param team team to select
     * @return true if successful, false if not
     */
    public boolean chooseTeam(Player.PlayerTeam team)    {
        String answer = localClient.sendCommand("chooseteam;" + localClient.getId()+ ";"+
                        localClient.getSessionId()+ ';'+ team + ';');
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("null"))){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if ((parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("false")))
            return false;

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.getPlayer().setTeam(team);
        }
        return true;
    }

    /**
     * Choose role for player
     * @param role player role
     * @return true if successful, false if not
     */

    public boolean chooseRole(Player.PlayerRole role)    {
        String answer = localClient.sendCommand("chooserole;" + localClient.getId()+ ";"+
                localClient.getSessionId()+ ';'+ role + ';');
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("null"))){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if ((parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("false")))
            return false;

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.getPlayer().setRole(role);
        }
        return true;
    }

    /**
     * Disconnect from server
     */
    public void disconnect(){
        String answer = localClient.sendCommand("disconnect;" + localClient.getId() + ";" +
                localClient.getSessionId() + ';');
        AnswerParser parser = new AnswerParser(answer);
        if(!parser.getArguments()[0].equals("true")){
            System.err.println("Disconnecting was not granted by server!");
        }
    }

    /**
     * Start the game
     * @return true if successful, false if not
     */
    public boolean startTheGame(){
        try {
            List<String> lines;
            if(customDeck){
                lines = Files.readAllLines(Paths.get(deckFile));
            }
            else{
                ClassLoader classLoader = this.getClass().getClassLoader();
                File configFile = new File(classLoader.getResource("Names.dck").getFile());
                lines = Files.readAllLines(configFile.toPath());
            }

            ArrayList<String> linesList = new ArrayList<>(lines);

            StringJoiner joiner = new StringJoiner(";");
            for (String line : linesList) {
                joiner.add(line);
            }

            String answer = localClient.sendCommand("startgame;" + localClient.getId()+ ";"+
                    localClient.getSessionId() + ';' + joiner.toString());

            AnswerParser parser = new AnswerParser(answer);
            if(parser.getArguments()[0].equals("false")){
                System.err.println("Starting the game was not granted by server!");
                return false;
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    /**
     * Load the game from save file and start it
     * @param data save file data
     * @return true if successful, false if not
     */
    public boolean loadTheGame(String data){
        String answer = localClient.sendCommand("loadgame;" + localClient.getId()+ ";"+
                localClient.getSessionId() + ';' + data);

        AnswerParser parser = new AnswerParser(answer);
        if(parser.getArguments()[0].equals("false")){
            System.err.println("Starting the game was not granted by server!");
            return false;
        }

        return true;
    }
}
