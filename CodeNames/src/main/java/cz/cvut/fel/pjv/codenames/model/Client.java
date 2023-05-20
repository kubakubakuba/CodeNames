package cz.cvut.fel.pjv.codenames.model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private String id;

    private UUID sessionId = null;

    private String serverIP = "localhost";
    private int serverPort = 1313;
    private Player player;

    private Socket socket;

    public Client(String id, String serverIP, int serverPort){
        this.id = id;
        this.player = new Player(id);
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    public UUID getSessionId() {return sessionId;}
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public Player getPlayer() {
        return player;
    }

    public String sendCommand(String command){
        try{
            Socket sock = new Socket(serverIP, serverPort);
            this.socket = sock;
            OutputStream output = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String serverAnswer;

            /*command += this.getId();
            if (sessionId != null){
                command += this.getSessionId().toString();
            }*/
            writer.println(command);

            InputStream input = sock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            serverAnswer = reader.readLine();
            System.out.println(serverAnswer);

            return serverAnswer;
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    public String getServerIP() {
        return serverIP;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setSessionId(String argument) {
        try {
            sessionId = UUID.fromString(argument);
        }
        catch (Exception e){
            System.out.println("Invalid session id, caused by connecting to lobby with username that is already in use.");
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
