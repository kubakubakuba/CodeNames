package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Player;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable{

    private final int PORT = 1313;



    private ArrayList<Session> activeSessions = new ArrayList<Session>();

    public ArrayList<Session> getActiveSessions() {return activeSessions;}

    public void Server()    {
        //host, socket,
    }

    @Override
    public void run(){
        try(ServerSocket ss = new ServerSocket(PORT)){
            while(true){
                Socket socket = ss.accept();
                System.out.println("new client");

                new ServerThread(socket).start();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addSession(Session session){
        activeSessions.add(session);
    }

    public void shutDown()    {

    }

}
