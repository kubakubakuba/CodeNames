package cz.cvut.fel.pjv.codenames.model;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private String id;

    private UUID sessionId = null;

    private Player player;

    private Socket socket;

    public Client(String id){
        this.id = id;
        this.player = new Player(id);
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

    public String sendCommand(String command, String hostname, int port){
        try{
            Socket sock = new Socket("localhost", 1313);
            this.socket = sock;
            OutputStream output = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String serverAnswer;

            command += this.getId();
            if (sessionId != null){
                command += this.getSessionId().toString();
            }
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

    public void setSessionId(String argument) {
        sessionId = UUID.fromString(argument);
    }

    public Socket getSocket() {
        return socket;
    }
}
