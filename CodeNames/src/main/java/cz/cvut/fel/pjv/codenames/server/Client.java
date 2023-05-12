package cz.cvut.fel.pjv.codenames.server;

import cz.cvut.fel.pjv.codenames.GUI.GUI_Wireframe;
import cz.cvut.fel.pjv.codenames.GUI.StartView;
import cz.cvut.fel.pjv.codenames.model.Player;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;

public class Client {


    private String id;



    private UUID sessionId = null;

    private Player player;

    public Client(String id) {
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

    public void writeCommand(String hostname, int port){
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        sendCommand(command, hostname, port);
    }

    public String sendCommand(String command, String hostname, int port){
        try (Socket socket = new Socket(hostname, port)){
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            String serverAnswer;

            command += this.getId();
            if (sessionId != null){
                command += this.getSessionId().toString();
            }
            writer.println(command);


            InputStream input = socket.getInputStream();
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
}
