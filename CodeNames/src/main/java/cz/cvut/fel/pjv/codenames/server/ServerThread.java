package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerThread extends Thread {

    private final Socket socket;

    private final Server server;

    CommandParser parser = new CommandParser();

    public ServerThread(Socket socket, Server server)  {
        this.server = server;
        this.socket = socket;
    }


    private boolean checkRoleAvailable(String playerId, String sessionId, Player.PlayerRole role){
        Session session = server.getActiveSessions().get(sessionId);
        HashMap<String, Player> players = session.getLobby().getListOfPlayers();

        if(players.get(playerId).getTeam() == Player.PlayerTeam.NONE){
            System.err.println("You need to be in a team to select a role!");
            return false;
        }

        for(Map.Entry<String, Player> set : players.entrySet()){
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
                System.out.println("client: " + text);
                parser = new CommandParser(text);

                if(parser.getCommand() == CommandParser.CommandType.UNKNOWN_COMMAND){
                    writer.println("Unknown command!");
                    System.err.println("Unknown command!");
                }

                if(parser.getCommand() == CommandParser.CommandType.SEND_MESSAGE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String message = parser.getArguments()[2];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    //TODO: send message through messaging server
                }

                if(parser.getCommand() == CommandParser.CommandType.MAKE_MOVE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String message = parser.getArguments()[2];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    //TODO: make move through game controller
                }

                if(parser.getCommand() == CommandParser.CommandType.CREATE_SESSION){
                    Session session = new Session(parser.getArguments()[0]);
                    server.addSession(session);
                    writer.println("1arg;" + session.getSessionId());
                }

                if(parser.getCommand() == CommandParser.CommandType.CONNECT){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    String response = "1arg;null;";
                    if(server.getActiveSessions().containsKey(idSession)){
                        Session s = server.getActiveSessions().get(idSession);
                        s.getLobby().getListOfIds().add(idSelf);
                        s.getLobby().getListOfPlayers().put(idSelf, new Player(idSelf));
                        response = "1arg;" + idSession;
                    }

                    System.out.printf("response: %s\n", response);
                    writer.println(response);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_HOST_ID){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

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
                    if(parser.getArguments()[2].equals("spymaster")){
                        role = Player.PlayerRole.SPY_MASTER;
                    }
                    else if(parser.getArguments()[2].equals("operative")){
                        role = Player.PlayerRole.FIELD_OPERATIVE;
                    }
                    else if(parser.getArguments()[2].equals("leader")){
                        role = Player.PlayerRole.FIELD_OPERATIVE_LEADER;
                    }
                    else{
                        role = Player.PlayerRole.NONE;
                    }

                    boolean role_available = checkRoleAvailable(idSelf, idSession, role);
                    String response = "1arg;false";
                    if(role_available){
                        server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().get(idSelf).setRole(role);
                    }

                    System.out.printf("response: %s\n", response);
                    writer.println(response);
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

                    System.out.println("Player selected team: " + team);
                    writer.println(response);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_LOBBY_IDS){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    List<String> ids = s.getLobby().getListOfIds();
                    StringBuilder response = new StringBuilder("idlist;");
                    for(String id : ids){
                        response.append(id).append(";");
                    }

                    writer.println(response);
                    System.out.printf("response: %s\n", response);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_SESSIONS){
                    String idSelf = parser.getArguments()[0];
                }

                if(parser.getCommand() == CommandParser.CommandType.DISCONNECT_PLAYER){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    if(!(server.getActiveSessions().containsKey(idSession) && server.getActiveSessions().get(idSession).getLobby().getListOfPlayers().containsKey(idSelf))){
                        System.err.println("Player not in session!");
                        writer.println("1arg;null");
                        return;
                    }

                    Session s = server.getActiveSessions().get(idSession);
                    s.getLobby().getListOfIds().remove(idSelf);

                    writer.println("1arg;true");
                    System.out.println("Player disconnected: " + idSelf);
                }

            } while(parser.getCommand() != CommandParser.CommandType.TERMINATE_SERVER);
            writer.println("Stopping the server");
            socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
