package cz.cvut.fel.pjv.codenames.model;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.UUID;

public class Client {
    private String id;

    private UUID sessionId = null;

    private String serverIP = "localhost";
    private int serverPort = 1313;
    private Player player;

    private Socket socket;

    /**
     * Constructor
     * @param id id of the client
     * @param serverIP server ip
     * @param serverPort server port
     */
    public Client(String id, String serverIP, int serverPort){
        this.id = id;
        this.player = new Player(id);
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    /**
     * Get the session id of the client
     * @return session id
     */
    public UUID getSessionId() {return sessionId;}

    /**
     * Getter for id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for id
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * Getter for player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sends a command to the server and returns the answer
     * @param command command to send
     * @return answer from the server
     */
    public String sendCommand(String command){
        try{
            Socket sock = new Socket(serverIP, serverPort);
            sock.setSoTimeout(1000);
            this.socket = sock;
            OutputStream output = sock.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String serverAnswer;

            writer.println(command);

            InputStream input = sock.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            serverAnswer = reader.readLine();
            System.out.println(serverAnswer);

            return serverAnswer;
        }
        catch (SocketTimeoutException ex){
            return "1arg;null";
        }
        catch (Exception e){
            System.out.println(e);
        }

        return null;
    }

    /**
     * Getter for server ip
     * @return server ip
     */
    public String getServerIP() {
        return serverIP;
    }

    /**
     * Getter for server port
     * @return server port
     */
    public int getServerPort() {
        return serverPort;
    }

    /**
     * Sets the session id of the client
     * @param argument session id
     */
    public void setSessionId(String argument) {
        try {
            sessionId = UUID.fromString(argument);
        }
        catch (Exception e){
            System.out.println("Invalid session id, caused by connecting to lobby with username that is already in use.");
        }
    }
}
