package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
public class ServerThread extends Thread {

    private final Socket socket;

    private final Server server;
    private final static Logger LOGGER = Logger.getLogger(ServerThread.class.getName());
    CommandParser parser = new CommandParser();

    public ServerThread(Socket socket, Server server)  {
        this.server = server;
        this.socket = socket;
    }

    private void sendUpdates(String session){
        for(Socket s : server.getActiveSessions().get(session).getListeners().values()){
            PrintWriter playerWriter = null;
            try {
                playerWriter = new PrintWriter(s.getOutputStream(), true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            playerWriter.println("update;");
        }
    }

    private boolean checkRoleAvailable(String playerId, String sessionId, Player.PlayerRole role){
        Session session = server.getActiveSessions().get(sessionId);
        HashMap<String, Player> players = session.getLobby().getListOfPlayers();

        if(players.get(playerId).getTeam() == Player.PlayerTeam.NONE){
            System.err.println("You need to be in a team to select a role!");
            return false;
        }

        if(role == Player.PlayerRole.FIELD_OPERATIVE || role == Player.PlayerRole.NONE){
            return true;
        }

        for(Map.Entry<String, Player> set : players.entrySet()){
            if(set.getValue().getID().equals(playerId)){
                continue; //skip self
            }
            if(set.getValue().getRole() == role){
                System.err.println("Role in team already taken!");
                return false;
            }
        }

        return true;
    }

    public void run(){
        try{
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String text;
            do{
                text = reader.readLine();
                if(text == null){
                    continue;
                }
                //System.out.println("client: " + text);
                parser = new CommandParser(text);

                if(parser.getCommand() == CommandParser.CommandType.UNKNOWN_COMMAND){
                    LOGGER.severe("Unknown command!");
                    writer.println("Unknown command!");
                    //System.err.println("Unknown command!");
                }

                if(parser.getCommand() == CommandParser.CommandType.LISTEN){
                    String session = parser.getArguments()[0];
                    String id = parser.getArguments()[1];
                    Session s = server.getActiveSessions().get(session);
                    LOGGER.log(Level.INFO, "Player " + parser.getArguments()[1] + " is listening to session " + session);

                    if(s.getLobby().getListOfPlayers().containsKey(id)){
                        s.addListener(socket, id);
                        writer.println("accept;"); //accept the listener
                    }
                    else{
                        writer.println("decline;"); //accept the listener
                    }
                }

                if(parser.getCommand() == CommandParser.CommandType.SEND_MESSAGE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String message = parser.getArguments()[2];

                    LOGGER.log(Level.INFO, "Player " + idSelf + " sent message: " + message + " to session " + idSession);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);

                    for(Player p : s.getLobby().getListOfPlayers().values()){
                        if(p.getTeam() == s.getLobby().getListOfPlayers().get(idSelf).getTeam()){
                            Socket playerSocket = p.getSocket();
                            PrintWriter playerWriter = new PrintWriter(playerSocket.getOutputStream(), true);
                            playerWriter.println("message;" + message.replace(";", ","));
                        }
                    }

                    sendUpdates(idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.MAKE_MOVE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String move = parser.getArguments()[2];

                    LOGGER.log(Level.INFO, "Player " + idSelf + " made move: " + move + " at session " + idSession);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    //TODO: make move through game controller

                    sendUpdates(idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.CREATE_SESSION){
                    Session session = new Session(parser.getArguments()[0]);
                    server.addSession(session);

                    LOGGER.log(Level.INFO, "Session " + session.getSessionId() + " created!");

                    writer.println("1arg;" + session.getSessionId());
                }

                if(parser.getCommand() == CommandParser.CommandType.CONNECT){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];


                    LOGGER.log(Level.INFO, "Player " + idSelf + " connected to session " + idSession);

                    String response = "1arg;null;";
                    if(server.getActiveSessions().containsKey(idSession)){
                        Session s = server.getActiveSessions().get(idSession);
                        s.getLobby().getListOfIds().add(idSelf);
                        s.getLobby().getListOfPlayers().put(idSelf, new Player(idSelf, this.socket));
                        response = "1arg;" + idSession;
                    }

                    System.out.printf("response: %s\n", response);
                    writer.println(response);

                    sendUpdates(idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_HOST_ID){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested host id of session " + idSession);

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    String response = "1arg;" + s.getHostId();

                    System.out.printf("response: %s\n", response);
                    writer.println(response);
                }


                if(parser.getCommand() == CommandParser.CommandType.CHOOSE_ROLE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;false");
                        return;
                    }

                    Player.PlayerRole role;
                    if(parser.getArguments()[2].equals("SPY_MASTER")){
                        role = Player.PlayerRole.SPY_MASTER;
                    }
                    else if(parser.getArguments()[2].equals("FIELD_OPERATIVE")){
                        role = Player.PlayerRole.FIELD_OPERATIVE;
                    }
                    else if(parser.getArguments()[2].equals("FIELD_OPERATIVE_LEADER")){
                        role = Player.PlayerRole.FIELD_OPERATIVE_LEADER;
                    }
                    else{
                        role = Player.PlayerRole.NONE;
                    }

                    boolean role_available = checkRoleAvailable(idSelf, idSession, role);
                    String response = "1arg;false";
                    if(role_available){
                        server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().get(idSelf).setRole(role);
                        response = "1arg;true";
                    }

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested to choose role " + role + " at session " + idSession + ",granted: " + role_available);
                    System.out.printf("response: %s\n", response);
                    writer.println(response);

                    sendUpdates(idSession);
                }

                if(parser.getCommand() == CommandParser.CommandType.CHOOSE_TEAM){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String team = parser.getArguments()[2];

                    String response = "1arg;false";
                    if(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf)){
                        Session s = server.getActiveSessions().get(idSession);
                        s.getLobby().getListOfPlayers().get(idSelf).setRole(Player.PlayerRole.NONE); //we need to reset the role
                        s.getLobby().getListOfPlayers().get(idSelf).setTeam(Player.PlayerTeam.valueOf(team));
                        response = "1arg;true";
                    }

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested to choose team " + team + " at session " + idSession + ",granted: " + response.split(";")[1]);
                    System.out.println("Player selected team: " + team);
                    writer.println(response);

                    sendUpdates(idSession);
                }


                if(parser.getCommand() == CommandParser.CommandType.GET_SESSIONS){
                    String idSelf = parser.getArguments()[0];
                    LOGGER.log(Level.INFO, "Player "+ idSelf +" requested session list");
                    StringBuilder result = new StringBuilder("sessionlist;");
                    for(Session s : server.getActiveSessions().values()){
                        result.append(s.getSessionId()).append(";");
                    }

                    writer.println(result);
                    System.out.printf("response: %s\n", result);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_PLAYER_COUNT){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    LOGGER.log(Level.INFO, "Player "+ idSelf + " requested player count");
                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }
                    Session s = server.getActiveSessions().get(idSession);
                    HashMap<String,Player> players = s.getLobby().getListOfPlayers();

                    int[] RBNplayers = {0,0,0};

                    for (Map.Entry<String, Player> entry : players.entrySet())    {
                        Player p = entry.getValue();
                        if (p.getTeam() == Player.PlayerTeam.NONE)
                            RBNplayers[2]++;
                        if(p.getTeam() == Player.PlayerTeam.BLUE)
                            RBNplayers[1]++;
                        if(p.getTeam() == Player.PlayerTeam.RED)
                            RBNplayers[0]++;
                    }
                    String response = "playercount;"+ RBNplayers[0]+ ";"+RBNplayers[1] +";"+ RBNplayers[2];
                    writer.println(response);
                    System.out.printf("response: %s\n", response);
                }

                if (parser.getCommand() == CommandParser.CommandType.GET_LOBBY_IDS)
                {
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested list of connected ids from session " + idSession);
                    System.out.println(server.getActiveSessions().toString() + server.getActiveSessions().get(idSession).getLobby().getListOfPlayers());
                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }
                    Session s = server.getActiveSessions().get(idSession);
                    HashMap<String,Player> players = s.getLobby().getListOfPlayers();

                    StringBuilder response = new StringBuilder("idlist;");
                    for (Map.Entry<String, Player> entry : players.entrySet())    {
                        String id = entry.getKey();
                        response.append(id).append(";");
                    }
                    writer.println(response);
                    System.out.printf("response: %s\n", response);
                }

                if(parser.getCommand() == CommandParser.CommandType.DISCONNECT_PLAYER){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    LOGGER.log(Level.INFO, "Player " + idSelf + " requested to disconnect from session " + idSession);
                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))) {
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }
                    Session s = server.getActiveSessions().get(idSession);
                    s.getLobby().getListOfIds().remove(idSelf);
                    s.getLobby().getListOfPlayers().remove(idSelf);

                    writer.println("1arg;true");
                    System.out.println("Player disconnected: " + idSelf);

                    OutputStream endOutputStream = s.getListeners().get(idSelf).getOutputStream();
                    PrintWriter endPrintWriter = new PrintWriter(endOutputStream, true);
                    endPrintWriter.println("endlisten;");

                    s.getListeners().remove(idSelf);

                    sendUpdates(idSession);
                }



            } while(parser.getCommand() != CommandParser.CommandType.TERMINATE_SERVER);
            LOGGER.log(Level.INFO, "Server thread terminated");
            writer.println("Stopping the server");
            //socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
