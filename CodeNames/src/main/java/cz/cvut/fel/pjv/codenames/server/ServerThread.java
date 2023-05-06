package cz.cvut.fel.pjv.codenames.server;

import javafx.scene.text.FontWeight;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread    {

    private Socket socket;

    CommandHandler handle = new CommandHandler();

    public ServerThread(Socket socket)  {
        this.socket = socket;
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
                handle = new CommandHandler(text);

                if(handle.getCommand() == CommandHandler.CommandType.UNKNOWN_COMMAND){
                    writer.println("Unknown command!");
                }

                if(handle.getCommand() == CommandHandler.CommandType.SEND_MESSAGE){
                    writer.println("Player has sent a message: " + handle.getArguments()[0]);
                }

                if(handle.getCommand() == CommandHandler.CommandType.MAKE_MOVE){
                    writer.println("Player has made a move with following arguments: " + handle.getArguments()[0] + " " + handle.getArguments()[1] + " " + handle.getArguments()[2]);
                }

                if(handler.getCommand() == CommandHandler.CommandType.CREATE_SESSION){
                    Session session = new Session(handler.getArguments()[0]);
                    server.addSession(session);
                    writer.println("generic1arg;" + session.getSessionId());
                }

                if(handler.getCommand() == CommandHandler.CommandType.CONNECT){
                    String idSelf = handler.getArguments()[0];
                    String idSession = handler.getArguments()[1];

                    String response = "generic1arg;null";
                    for (Session s : server.getActiveSessions()){
                        if(idSession == s.getSessionId().toString()){
                            List<String> ids = s.getLobby().getListOfIds();
                            ids.add(idSelf);
                            response = "generic1arg;" + idSession;
                        }
                    }
                    writer.println(response);
                }

                if(handler.getCommand() == CommandHandler.CommandType.GET_HOST_ID){
                    String idSelf = handler.getArguments()[0];
                    String idSession = handler.getArguments()[1];

                    String response = "generic1arg;null";
                    for(Session s : server.getActiveSessions()){
                        if(idSession == s.getSessionId().toString() && s.getLobby().getListOfIds().contains(idSelf)){
                            response = "generic1arg;" + s.getHostId();
                        }
                    }
                    writer.println(response);
                }

            } while(handle.getCommand() != CommandHandler.CommandType.TERMINATE_SERVER);
            writer.println("Stopping the server");
            socket.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
