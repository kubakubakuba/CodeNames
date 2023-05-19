package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.model.Player;
import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.model.Client;

import java.util.ArrayList;
import java.util.Arrays;

public class LobbyController {

    private Client localClient;

    private String hostId;

    private int[] RBNPlayers = {0, 0, 0};
    private int[] playerRoles = {0, 0, 0, 0, 0, 0, 0};
    private int playerCount = 0;

    private String serverIP = "localhost";
    private int serverPort = 1313;

    private ChatController chatController;

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

    public LobbyController(String id, String serverIP, int serverPort){
        localClient = new Client(id, serverIP, serverPort);
    }

    /*public void parseServeIPString(String serverIP){
        String[] parts = serverIP.split(":");
        this.serverIP = parts[0];
        this.serverPort = Integer.parseInt(parts[1]);

        System.out.println(this.serverIP + this.serverPort);
    }*/

    //implement start client and start server
    public boolean createServerSession(String hostId)   {
        //System.out.println("Write a command:");
        System.out.println("server: " + serverIP + serverPort);
        String serverAnswer = localClient.sendCommand("createSession;" + hostId + ';', serverIP, serverPort);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("null") )){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.setSessionId(parser.getArguments()[0]);
        }
        return true;
    }

    public boolean connectToSession(String guestId, String sessionId) {
        String serverAnswer = localClient.sendCommand("connect;" + guestId + ';' + sessionId + ';', serverIP, serverPort);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0].equals("null"))){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.setSessionId(parser.getArguments()[0]);
        }

        return true;
    }

    public ArrayList<String> getServerSessions(){
        String serverAnswer = localClient.sendCommand("getSessions;" + localClient.getId() + ';', serverIP, serverPort);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.SESSION_LIST){
            System.err.println("Received unexpected server answer!");
            return null;
        }

        return new ArrayList<>(Arrays.asList(parser.getArguments()));
    }

    public ArrayList<String> getIdList(){
        String serverAnswer = localClient.sendCommand("getlobbyids;" + localClient.getId() + ';' + localClient.getSessionId() + ";", serverIP, serverPort);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ID_LIST){
            System.err.println("Received unexpected server answer!");
            return null;
        }
        return new ArrayList<>(Arrays.asList(parser.getArguments()));
    }

    public String getHostId(){
        return hostId;
    }

    public void setHostId(){
        String answer = localClient.sendCommand("gethostid;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';', serverIP, serverPort);
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            hostId = parser.getArguments()[0];
        }
    }

    public void updatePlayerCount()    {
        String answer = localClient.sendCommand("getplayercount;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';', serverIP, serverPort);
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() == AnswerParser.AnswerType.PLAYER_COUNT) {
            RBNPlayers[0] = Integer.parseInt(parser.getArguments()[0]);
            RBNPlayers[1] = Integer.parseInt(parser.getArguments()[1]);
            RBNPlayers[2] = Integer.parseInt(parser.getArguments()[2]);
            playerCount = RBNPlayers[0] + RBNPlayers[1] + RBNPlayers[2];
        }

    }

    public void updatePlayerRoles(){
        String answer = localClient.sendCommand("getplayerroles;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';', serverIP, serverPort);

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

    public boolean chooseTeam(Player.PlayerTeam team)    {
        String answer = localClient.sendCommand("chooseteam;" + localClient.getId()+ ";"+
                        localClient.getSessionId()+ ';'+ team + ';', serverIP, serverPort);
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

    public boolean chooseRole(Player.PlayerRole role)    {
        String answer = localClient.sendCommand("chooserole;" + localClient.getId()+ ";"+
                localClient.getSessionId()+ ';'+ role + ';', serverIP, serverPort);
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

    public void disconnect(){
        String answer = localClient.sendCommand("disconnect;" + localClient.getId() + ";" +
                localClient.getSessionId() + ';', serverIP, serverPort);
    }


    public void startTheGame(){
        String answer = localClient.sendCommand("startgame;" + localClient.getId()+ ";"+
                localClient.getSessionId() + ';' + "src/main/resources/cz/cvut/fel/pjv/codenames/Names.dck;", serverIP, serverPort);

        AnswerParser parser = new AnswerParser(answer);
        if(parser.getArguments()[0].equals("null")){
            System.err.println("Starting the game was not granted by server!");
        }

    }

    //implement feedback from server to model

}
