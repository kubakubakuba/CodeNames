package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.server.Client;

import java.util.ArrayList;
import java.util.Arrays;

public class LobbyController {

    private Client localClient;

    private String hostId;

    public Client getLocalClient() {
        return localClient;
    }

    public LobbyController(String id)    {
        localClient = new Client(id);
    }

    //implement start client and start server
    public boolean createServerSession(String hostId)   {
        //System.out.println("Write a command:");
        String serverAnswer = localClient.sendCommand("createSession;" + hostId + ';', "localhost", 1313);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0] == "null")){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.setSessionId(parser.getArguments()[0]);
        }
        return true;
    }

    public boolean connectToSession(String guestId, String sessionId) {
        String serverAnswer = localClient.sendCommand("connect;" + guestId + ';' + sessionId + ';', "localhost", 1313);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.ONE_ARG || (parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG && parser.getArguments()[0] == "null")){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            localClient.setSessionId(parser.getArguments()[0]);
        }

        return true;
    }

    public ArrayList<String> getServerSessions(){
        String serverAnswer = localClient.sendCommand("getSessions;" + localClient.getId() + ';', "localhost", 1313);
        AnswerParser parser = new AnswerParser(serverAnswer);

        if(parser.getAnswer() != AnswerParser.AnswerType.SESSION_LIST){
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
                                                localClient.getSessionId()+ ';', "localhost", 1313);
        AnswerParser parser = new AnswerParser(answer);

        if(parser.getAnswer() == AnswerParser.AnswerType.ONE_ARG){
            hostId = parser.getArguments()[0];
        }
    }



    //implement feedback from server to model

}
