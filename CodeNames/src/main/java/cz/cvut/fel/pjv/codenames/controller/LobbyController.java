package cz.cvut.fel.pjv.codenames.controller;

import cz.cvut.fel.pjv.codenames.server.AnswerParser;
import cz.cvut.fel.pjv.codenames.server.Client;

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
        AnswerParser handler = new AnswerParser(serverAnswer);

        if(handler.getAnswer() != AnswerParser.AnswerType.GENERIC_ONE_ARG || (handler.getAnswer() == AnswerParser.AnswerType.GENERIC_ONE_ARG && handler.getArguments()[0] == "null")){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if(handler.getAnswer() == AnswerParser.AnswerType.GENERIC_ONE_ARG){
            localClient.setSessionId(handler.getArguments()[0]);
        }
        return true;
    }

    public boolean connectToSession(String guestId, String sessionId) {
        String serverAnswer = localClient.sendCommand("connect;" + guestId + ';' + sessionId + ';', "localhost", 1313);
        AnswerParser handler = new AnswerParser(serverAnswer);

        if(handler.getAnswer() != AnswerParser.AnswerType.GENERIC_ONE_ARG || (handler.getAnswer() == AnswerParser.AnswerType.GENERIC_ONE_ARG && handler.getArguments()[0] == "null")){
            System.err.println("Received unexpected server answer!");
            return false;
        }

        if(handler.getAnswer() == AnswerParser.AnswerType.GENERIC_ONE_ARG){
            localClient.setSessionId(handler.getArguments()[0]);
        }

        return true;
    }


//    //implement feedback from gui to server and model
//    public int getNumPLayers()    {
//        return lobby.getNumOfPlayers();
//    }
//    public int getNumRedPlayers()   {
//        return lobby.getNumOfRED();
//    }
//    public int getNumBluePlayers()   {
//        return lobby.getNumOfBLUE();
//    }
//
    public String getHostId(){
        return hostId;
    }
    public void setHostId(){
        String answer = localClient.sendCommand("gethostid;" + localClient.getId()+ ";"+
                                                localClient.getSessionId()+ ';', "localhost", 1313);
        AnswerParser handler = new AnswerParser(answer);

        if(handler.getAnswer() == AnswerParser.AnswerType.GENERIC_ONE_ARG){
            hostId = handler.getArguments()[0];
        }
    }

//    public  void updateNumPlayers(String command, String ID)  {
//        switch (command) {
//            case "chooseRed":
//                getPlayer(ID).setTeam(Player.PlayerTeam.RED);
//                updateCommonStats();
//                break;
//            case "chooseBlue":
//                getPlayer(ID).setTeam(Player.PlayerTeam.BLUE);
//                updateCommonStats();
//                break;
//        }
//    }
//
//    public boolean validChoice()  {
//
//        return true;
//    }
//
//    public void updateLocalStats(String id){
//        for(Client client : connectedClients)  {
//            if (id.equals(client.getId()))  {
//                Player p = client.getPlayer();
//
//            }
//        }
//    }
//    public void updateCommonStats(){
//        int Totp = 0;
//        int redp = 0;
//        int bluep = 0;
//        for(Client client : connectedClients)  {
//            Player p = client.getPlayer();
//            Totp++;
//            if (p.getTeam().equals(Player.PlayerTeam.RED))
//                redp++;
//            if (p.getTeam().equals(Player.PlayerTeam.BLUE))
//                bluep++;
//        }
//        lobby.setNumOfPlayers(Totp);
//        lobby.setNumOfRED(redp);
//        lobby.setNumOfBLUE(bluep);
//    }
//

    //implement feedback from server to model

}
