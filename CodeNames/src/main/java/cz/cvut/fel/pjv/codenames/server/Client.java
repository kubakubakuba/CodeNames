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

    private Player player;

    public Client(String id) {
        this.id = id;
        this.player = new Player(id);
    }

    public String getId() {return id;}
    public Player getPlayer() {return player;}

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        String hostname = "localhost";
        int port = 1313;

        try (Socket socket = new Socket(hostname, port)){
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true);

             String text;
             String msg;
             do{
                 text = sc.nextLine().toString();
                 writer.println(text);

                 InputStream input = socket.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(input));

                 msg = reader.readLine();
                 System.out.println(msg);
             } while(!msg.equals("Stopping the server"));
             socket.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
