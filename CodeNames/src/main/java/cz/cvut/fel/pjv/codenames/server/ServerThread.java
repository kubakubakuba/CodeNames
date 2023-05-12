package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ServerThread extends Thread {

    private final Socket socket;

    private final Server server;

    CommandParser parser = new CommandParser();

    public ServerThread(Socket socket, Server server)  {
        this.server = server;
        this.socket = socket;
    }


    private boolean checkRoleAvailable(String playerId, String sessionId, Player.PlayerRole role, Player.PlayerTeam team){
        List<Session> sessions = server.getActiveSessions();
        for(Session s : sessions){
            if(s.getSessionId().toString().equals(sessionId)){ //get session with same
                for(Player p: s.getLobby().getListOfPlayers()){
                    if(p.getTeam() == team && p.getRole() == role){
                        return false;
                    }
                }
            }
        }
        return false;

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
                }

                if(parser.getCommand() == CommandParser.CommandType.SEND_MESSAGE){
                    writer.println("Player has sent a message: " + parser.getArguments()[0]);
                }

                if(parser.getCommand() == CommandParser.CommandType.MAKE_MOVE){
                    writer.println("Player has made a move with following arguments: " + parser.getArguments()[0] + " " + parser.getArguments()[1] + " " + parser.getArguments()[2]);
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
                    for (Session s : server.getActiveSessions()){
                        if(idSession.equals(s.getSessionId().toString())){
                            List<String> ids = s.getLobby().getListOfIds();
                            ids.add(idSelf);
                            s.getLobby().getListOfPlayers().add(new Player(idSelf));
                            response = "1arg;" + idSession;
                        }
                    }

                    System.out.printf("response: %s\n", response);
                    writer.println(response);
                }

                if(parser.getCommand() == CommandParser.CommandType.GET_HOST_ID){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];

                    String response = "1arg;null";
                    for(Session s : server.getActiveSessions()){
                        if(idSession.equals(s.getSessionId().toString()) && s.getLobby().getListOfIds().contains(idSelf)){
                            response = "1arg;" + s.getHostId();
                        }
                    }
                    writer.println(response);
                }

                if(parser.getCommand() == CommandParser.CommandType.CHOOSE_ROLE){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                    String role = parser.getArguments()[2];

                    String response = "1arg;null";
                }
                if(parser.getCommand() == CommandParser.CommandType.CHOOSE_TEAM){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                }
                if(parser.getCommand() == CommandParser.CommandType.GET_LOBBY_IDS){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
                }
                if(parser.getCommand() == CommandParser.CommandType.GET_SESSIONS){
                    String idSelf = parser.getArguments()[0];
                }
                if(parser.getCommand() == CommandParser.CommandType.DISCONNECT_PLAYER){
                    String idSelf = parser.getArguments()[0];
                    String idSession = parser.getArguments()[1];
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
