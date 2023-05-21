package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.model.Session;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server implements Runnable{

    private final int PORT = 1515;

    private HashMap<String, Session> activeSessions = new HashMap<String, Session>();

    public HashMap<String, Session> getActiveSessions() {return activeSessions;}

    public Server(int port){
        //host, socket,
    }

    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = ss.accept();

                new ServerThread(socket, this).start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adds a session to the active sessions
     * @param session session id as string to be added
     */
    public void addSession(Session session){
        activeSessions.put(session.getSessionId().toString(), session);
    }

    public void shutDown()    {

    }

}


